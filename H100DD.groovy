@Grab(group = 'org.gebish', module = 'geb-core', version = '0.10.0')
@Grab("org.seleniumhq.selenium:selenium-firefox-driver:2.45.0")
//@Grab("org.seleniumhq.selenium:selenium-htmlunit-driver:2.45.0")
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

    static void ok() { status("${ESC}[32m[  OK  ]${ESC}[0m") }

    static void fail() { status("${ESC}[31m[ FAIL ]${ESC}[0m") }

    static void status(String status) {
        println ''.padRight(80 - lineLength) + status
        lineLength = 0
    }

    static void title(String title) {
        println "        [Title] ${title}"
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
            waitFor {
                title.contains 'Sign In'
            }
            title(title)

            puts "Logging in..."
            $('#UserName') << username
            $('#Password') << password
            $('#form-submit').click()
            waitFor {
                title.contains 'Dashboard'
            }
            dealWithPopup(browser)
            title(title)

            puts "Loading challenges"
            go "https://www.humana.com/members/get-healthy/challenges/"
            waitFor {
                title.contains 'Challenges'
            }
            dealWithPopup(browser)
            title(title)

            puts "Loading 100DD"
            $('.challenge-detail a span', text: "Humana's 100 DD - 2015").parent().click()
            waitFor {
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
                if ($('.alphabetical-list-item span', text: team.toUpperCase()[0])) {
                    println "Active letter"
                } else {
                    puts "Clicking ${team.toUpperCase()[0]}"
                    driver.executeScript(
            """\$.each(\$('.alphabetical-list-item a'), function(a,b) {
              if (b.innerHTML == '${team.toUpperCase()[0]}') {
                b.click();
              }
            });""")
                    waitFor {
            driver.executeScript("""return \$('.alphabetical-list-item span').text();""") == team.toUpperCase()[0]
                    }
                }

                puts "Loading team '$team'"
                driver.executeScript(
          """\$.each(\$('.team-name a span'), function(a,b){
            if (b.innerHTML == "${team}") {
              b.click();
            }
          });""")
                waitFor {
                    title.contains 'Team Detail'
                }
                dealWithPopup(browser)
                title(title)

                puts "Waiting for page to load"
                waitFor {
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
def humana = new Humana()
List<Map> maps

LogFactory.factory.setAttribute "org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog"
Logger.getLogger("com.gargoylesoftware.htmlunit").level = Level.OFF
Logger.getLogger("org.apache.commons.httpclient").level = Level.OFF

maps = humana.run(
        env['HUM_USER'], env['HUM_PASS'],
        [
                'Everything Is Groovy', 'Zippity'
        ]
)

TimeZone.setDefault(TimeZone.getTimeZone('UTC'))
def dataMap = [
        data: maps,
        date: new Date().format("yyyy-MM-dd'T'HH:mm:ssZ")
]

Humana.puts('Building JSON')
def json = new JsonBuilder(dataMap).toPrettyString()
Humana.ok()
//println json

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
