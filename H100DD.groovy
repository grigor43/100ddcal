@Grab(group = 'org.gebish', module = 'geb-core', version = '0.10.0')
//@Grab("org.seleniumhq.selenium:selenium-htmlunit-driver:2.45.0")
@Grab("org.seleniumhq.selenium:selenium-firefox-driver:2.45.0")
@Grab("org.seleniumhq.selenium:selenium-support:2.45.0")
@Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.6')

import geb.Browser
import groovy.json.JsonBuilder
import groovyx.net.http.HTTPBuilder
import org.apache.commons.logging.LogFactory
import org.openqa.selenium.WebDriverException
import com.thoughtworks.selenium.SeleniumException

import java.util.logging.Level
import java.util.logging.Logger

class Humana {
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

    List<Map> run(String username, String password, List<String> teams) {
        println "Thank you for trusting random scripts from the internet!"
        int idx = 0
        List retval
        def timestamp = new Date().format("yyyy-MM-dd'T'HH:mm:ss")
        Browser.drive {
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

            puts "Loading challenges"
            go "https://www.humana.com/members/get-healthy/challenges/"
            assert waitFor {
                title.contains 'Challenges'
            }
            dealWithPopup(browser)
            title(title)

            puts "Loading 100DD"
            $('.challenge-detail a span', text: "Humana's 100 DD - 2015").parent().click()
            assert waitFor {
                title.contains 'Challenge '
            }
            dealWithPopup(browser)
            title(title)

            def theUrl = driver.currentUrl
            println "The URL: ${theUrl}"

            retval = teams.collect { team ->
                println "\n\n"
                if (idx++) {
                    puts "Loading Challenge Page"
                    go theUrl
                    waitFor {
                        title.contains 'Challenge '
                    }
                }
                dealWithPopup(browser)

                def leadChar = team.toUpperCase()[0]
                if (leadChar =~ /[0-9]/) {
                    leadChar = '123'
                } else if (leadChar =~ /[A-Z]/) {
                    // do nothing
                } else {
                    leadChar = 'Other'
                }

                if ($('.alphabetical-list-item span', text: leadChar)) {
                    println "Active letter"
                } else {
                    puts "Clicking ${leadChar}"
                    while (!$('.alphabetical-list-item a', text: leadChar).isDisplayed()) {
                        $('.slide-next').find {it.isDisplayed()}.click()
                    }
                    $('.alphabetical-list-item a', text: leadChar).click()
                    assert waitFor {
                        $('.team-name a span')*.text().contains(team)
                    }
                }

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

                def teamName = team.replaceAll(/[^a-zA-Z0-9]/, '-')
                report "${timestamp}_${teamName}"
                def rank = $('.team-rank .block').text()
                def avgSteps = $('.team-statistics .block')[1].text()
                println "${team} - ${rank} - ${avgSteps}"
                def memberCount = $('.module-team-members li').size()

                def members = (1..memberCount).collect { mbr ->
                    def name = $('.module-team-members li .p1-left > span')[mbr - 1].text()
                    def score = $('.module-team-members li span.steps')[mbr - 1].text()
                    [name: name, score: score]
                }
                def teamret = [name: team, rank: rank, avgSteps: avgSteps, members: members]
                println teamret.members.collect { "    - ${it.name.padRight(30)} ${it.score.padLeft(9)}" }.join('\n')
                teamret
            }
            quit()
            println "\n\n"
        }
        return retval
    }


}

def env = System.getenv()

LogFactory.factory.setAttribute "org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog"
Logger.getLogger("com.gargoylesoftware.htmlunit").level = Level.OFF
Logger.getLogger("org.apache.commons.httpclient").level = Level.OFF

ArrayList<String> teamNames = [
        'Everything Is Groovy', 'Zippity', 'Sole Searchers',
        "Campbell's 100 day dashers!"
]
def username = env['HUM_USER']
def password = env['HUM_PASS']
if ( (!username || !password) && System.console().istty()) {
    if (!username) {
        for (int i = 0; i < 3; i ++) {
            username = System.console().readLine ('What is your vitality username? ').trim()
            if (username) {
                break
            }
        }
    }
    if (!username) {
        System.exit(2)
    }
    if (!password) {
        for (int i = 0; i < 3; i ++) {
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
def maps = new Humana().run(username, password, teamNames)

TimeZone.setDefault(TimeZone.getTimeZone('UTC'))
def dataMap = [
        data: maps,
        date: new Date().format("yyyy-MM-dd'T'HH:mm:ssZ")
]

Humana.puts('Building JSON')
def json = new JsonBuilder(dataMap).toPrettyString()
Humana.ok()

if (json.contains('null') || json.contains('""')) {
    throw new RuntimeException("Null value found")
}
Humana.puts "Submitting data"
def h = new HTTPBuilder('http://h100cal.appspot.com/')
h.post(
        path: '/store',
        body: dataMap,
        requestContentType: groovyx.net.http.ContentType.JSON
)
Humana.ok()
