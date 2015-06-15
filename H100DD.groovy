@Grab('org.gebish:geb-core:0.10.0')
@Grab("org.seleniumhq.selenium:selenium-firefox-driver:2.45.0")
@Grab("org.seleniumhq.selenium:selenium-support:2.45.0")
@Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.6')

import com.thoughtworks.selenium.SeleniumException
import geb.Browser
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovyx.net.http.HTTPBuilder
import org.openqa.selenium.WebDriverException

class UrlCache {

    public static final String FILE_NAME = "${System.getProperty('user.home')}/h100dd-urls.json"

    @Delegate
    Map<String, String> delegate = [:]

    void persist() {
        new File(FILE_NAME).text = new JsonBuilder(delegate).toPrettyString()
    }

    UrlCache() {
        if (new File(FILE_NAME).exists()) {
            delegate = new JsonSlurper().parse(new File(FILE_NAME)) as Map<String, String>
        } else {
            println "\n\nNo cache exists. This run will be particularly slow.\n\n"
        }
    }

}

class Humana {

    Humana(Browser browser, UrlCache urlCache) {
        println "Thank you for trusting random scripts from the internet!"
        this.theBrowser = browser
        this.urlCache = urlCache
    }

    private Browser theBrowser
    private UrlCache urlCache

    private static final ESC = 0x1b as char

    static int lineLength = 0

    static void puts(String data) {
        System.out.print data
        lineLength += data.length()
    }

    private static String ansi(int code) {
        "${ESC}[${code}m"
    }

    static void ok() { status("${ansi(32)}[  OK  ]${ansi(0)}") }

    static void fail() { status("${ansi(31)}[ FAIL ]${ansi(0)}") }

    static void status(String status) {
        println ''.padRight(80 - lineLength) + status
        lineLength = 0
    }

    static void title(String title) {
        println "        ${ansi(34)}[Title] ${title}${ansi(0)}"
    }

    boolean waitFor(double timeout, double interval, Closure condition) {
        if (timeout < 0) {
            fail()
            return false
        }
        boolean passed = false
        try {
            passed = condition()
        } catch (SeleniumException ignored) {

        } catch (WebDriverException ignored) {

        }

        if (passed) {
            ok()
            return true
        } else {
            puts "."
            sleep((long) (interval * 1000))
            waitFor(timeout - interval, interval, condition)
        }
    }

    boolean waitFor(Closure condition) {
        waitFor(10, 0.5, condition)
    }

    void dealWithPopup(Browser b) {
        sleep 500
        Browser.drive(b) {
            if ($('.fsrDeclineButton').isDisplayed()) {
                $('.fsrDeclineButton').click()
            }
        }
    }

    void initChallengeHome(String challengeName) {
        if (urlCache['challengeUrl']) {
            return
        }
        Browser.drive(theBrowser) {
            puts "Loading challenges"
            go "https://www.humana.com/members/get-healthy/challenges/"
            assert waitFor {
                title.contains 'Challenges'
            }
            dealWithPopup(browser)
            title(title)

            puts "Loading 100DD"
            $('.challenge-detail a span', text: challengeName).parent().click()
            assert waitFor {
                title.contains 'Challenge '
            }
            dealWithPopup(browser)
            title(title)

            urlCache['challengeUrl'] = driver.currentUrl
            urlCache.persist()
            println "The URL: ${driver.currentUrl}"
        }
    }

    void login(String username, String password) {
        Browser.drive(theBrowser) {
            browser.config.reportsDir = new File('build')
            driver.manage().window().maximize()
            if (driver.class.simpleName == 'HtmlUnitDriver') {
                driver.javascriptEnabled = true
            }

            puts "Loading..."
            go 'https://www.humana.com/logon'
            assert waitFor {
                title.contains 'Sign In'
            }
            title(title)

            puts "Logging in..."
            $('#UserName') << username
            $('#Password') << password
            $('#form-submit').click()
            assert waitFor {
                title.contains 'Dashboard'
            }
            dealWithPopup(browser)
            title(title)
        }
    }

    private void loadLeadChar(String team) {
        def leadChar = getLeadChar(team)
        Browser.drive(theBrowser) {
            if ($('.alphabetical-list-item span', text: leadChar)) {
                println "Active letter"
            } else {
                puts "Clicking ${leadChar}"
                while (!$('.alphabetical-list-item a', text: leadChar).isDisplayed()) {
                    $('.slide-next').find { it.isDisplayed() }.click()
                }
                $('.alphabetical-list-item a', text: leadChar).click()
                assert waitFor {
                    $('.team-name a span')*.text().contains(team)
                }
            }
        }
    }

    private void loadTeam(String team) {
        Browser.drive(theBrowser) {
            if (urlCache["team.${team}"]) {
                puts "Loading team '$team'"
                ok()
                go urlCache["team.${team}"]
                dealWithPopup(browser)
                title(title)

                puts "Waiting for page to load"
                assert waitFor {
                    $('.team-rank .block').text()
                }
            } else {
                puts "Loading Challenge Page"
                go urlCache['challengeUrl']
                waitFor {
                    title.contains 'Challenge '
                }

                dealWithPopup(browser)
                loadLeadChar(team)

                puts "Loading team '$team'"
                $('.team-name a span', text: team).click()
                assert waitFor {
                    title.contains 'Team Detail'
                }
                dealWithPopup(browser)
                title(title)

                puts "Waiting for page to load"
                assert waitFor {
                    $('.team-rank .block').text()
                }

                urlCache["team.${team}"] = driver.currentUrl
                urlCache.persist()
            }
            def timestamp = new Date().format("yyyy-MM-dd'T'HH:mm:ss")
            def teamName = team.replaceAll(/[^a-zA-Z0-9]/, '-')
            report "${timestamp}_${teamName}"
        }
    }

    Map fetchTeamData(String team) {
        Map retval = null
        Browser.drive(theBrowser) {
            println "\n\n"
            loadTeam(team)

            def rank = $('.team-rank .block').text()
            def avgSteps = $('.team-statistics .block')[1].text()
            println "${team} - ${rank} - ${avgSteps}"
            def memberCount = $('.module-team-members li').size()

            def members = (1..memberCount).collect { mbr ->
                def name = $('.module-team-members li .p1-left > span')[mbr - 1].text()
                def score = $('.module-team-members li span.steps')[mbr - 1].text()
                [name: name, score: score]
            }
            retval = [name: team, rank: rank, avgSteps: avgSteps, members: members]
            println retval.members.collect { "    - ${it.name.padRight(30)} ${it.score.padLeft(9)}" }.join('\n')
        }
        retval
    }

    private String getLeadChar(String team) {
        def firstChar = team.toUpperCase()[0]
        if (firstChar =~ /[0-9]/) {
            '123'
        } else if (firstChar =~ /[A-Z]/) {
            firstChar
        } else {
            'Other'
        }
    }

}

def env = System.getenv()

def teamNames = [
        'Everything Is Groovy',
        'Zippity',
        'Sole Searchers',
        "Campbell's 100 day dashers!",
        'San Diego Ala Carte',
        'Red Hot Chili Steppers',
        "Borho's Bunch",
        'SD Steppers - 100 DD',
        'Wanna Step Outside'
]
def username = env['HUM_USER']
def password = env['HUM_PASS']
if ((!username || !password) && System.console().istty()) {
    if (!username) {
        for (int i = 0; i < 3; i++) {
            username = System.console().readLine('What is your vitality username? ').trim()
            if (username) {
                break
            }
        }
    }
    if (!username) {
        System.exit(2)
    }
    if (!password) {
        for (int i = 0; i < 3; i++) {
            def readPassword = System.console().readPassword('What is your vitality password? ')
            password = new String(readPassword ?: '' as char[]).trim()
            if (password) {
                break
            }
        }
    }
}
if (!username || !password) {
    System.exit(1)
}

List<Map> maps = []
Browser.drive {
    def humana = new Humana(browser, new UrlCache())
    humana.login(username, password)
    humana.initChallengeHome("Humana's 100 DD - 2015")
    maps = teamNames.collect { humana.fetchTeamData(it) }
    browser.quit()
}

TimeZone.setDefault(TimeZone.getTimeZone('UTC'))
def dataMap = [data: maps, date: new Date().format("yyyy-MM-dd'T'HH:mm:ssZ")]

Humana.puts('Building JSON')
def json = new JsonBuilder(dataMap).toPrettyString()
Humana.ok()

if (json.contains('null') || json.contains('""')) {
    throw new RuntimeException("Null value found")
}
Humana.puts "Submitting data"
def h = new HTTPBuilder('https://h100cal.appspot.com/')
h.post(
        path: '/store',
        body: dataMap,
        requestContentType: groovyx.net.http.ContentType.JSON
)
Humana.ok()
