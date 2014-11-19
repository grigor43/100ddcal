@Grab("org.codehaus.geb:geb-core:0.7.2")
@Grab("org.seleniumhq.selenium:selenium-firefox-driver:2.43.1")
@Grab("org.seleniumhq.selenium:selenium-support:2.43.1")
@Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.6')

import geb.Browser
import groovy.json.JsonBuilder
import groovyx.net.http.HTTPBuilder

class Humana {
  boolean waitFor(double timeout, double interval, Closure condition) {
    if (timeout < 0) {
      println "F"
      return false
    }
    boolean passed = condition()
    if (passed) {
      println "T"
      return true
    } else {
      System.out.print "."
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
    Browser.drive {
      if (driver.class.canonicalName == 'HtmlUnitDriver') {
        driver.javascriptEnabled = true
      }

      println "Loading..."
      go 'https://www.humana.com/logon'
      waitFor {
        title.contains 'Sign In'
      }
      println "[Title] ${title}"

      println "Logging in..."
      $('#UserName') << username
      $('#Password') << password
      $('button')[1].click()
      waitFor {
        title.contains 'Dashboard'
      }
      dealWithPopup(browser)
      println "[Title] ${title}"

      println "Loading challenges"
      go "https://www.humana.com/members/get-healthy/challenges/"
      waitFor {
        title.contains 'Challenges'
      }
      dealWithPopup(browser)
      println "[Title] ${title}"

      println "Loading 100DD"
      $('.challenge-detail a span', text: '100 Day Dash').parent().click()
      waitFor {
        title.contains 'Challenge '
      }
      dealWithPopup(browser)
      println "[Title] ${title}"

      def theUrl = driver.currentUrl
      println "The URL: ${theUrl}"

      retval = teams.collect { team ->
        if (idx++) {
          go theUrl
          waitFor {
            title.contains 'Challenge '
          }
        }
        dealWithPopup(browser)
        println "Loading team '$team'"
        if ($('.alphabetical-list-item span', text: team.toUpperCase()[0])) {
          println "Active letter"
        } else {
          println "Clicking ${team.toUpperCase()[0]}..."
          driver.executeScript(
            """\$.each(\$('.alphabetical-list-item a'), function(a,b) {
              if (b.innerHTML == '${team.toUpperCase()[0]}') {
                b.click();
              }
            });""")
          // $('.alphabetical-list-item a', text: team.toUpperCase()[0]).click()
          println "Done clicking letter"
          waitFor {
            $('.alphabetical-list-item span').text() == team.toUpperCase()[0]
          }
        }
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
        println "[Title] ${title}"

        waitFor {
          $('.team-rank .block').text()
        }
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
        println teamret
        teamret
      }
      quit()
    }
    println retval
    return retval
  }


}

def env = System.getenv()
def humana = new Humana()
List<Map> maps

maps = humana.run(
    env['HUM_USER'], env['HUM_PASS'],
    [
        'Eloves Walk it out Crew', "Elove's Walk it out Crew",
        'Aerobic Task Force', 'One Hit Runners',
        'Sole Searchers', 'Zippity',
        "Team RC"
    ]
)

TimeZone.setDefault(TimeZone.getTimeZone('UTC'))
def dataMap = [
    data: maps,
    date: new Date().format("yyyy-MM-dd'T'HH:mm:ssZ")
]
def json = new JsonBuilder(dataMap).toString()
println json

if (json.contains('null') || json.contains('""')) {
  throw new RuntimeException("Null value found")
}
def h = new HTTPBuilder('http://h100cal.appspot.com/')
h.post(
    path: '/store',
    body: dataMap,
    requestContentType: groovyx.net.http.ContentType.JSON
)
