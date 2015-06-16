import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import h100.CacheHelper
import h100.Report

log.info "Params: ${params}"

def offset = params.offset?.toInteger() ?: 0

List<Report> reports = CacheHelper.get('data') {
  Report.findAll().sort { Report r ->
    - r.date.time
  }
}

log.info "Available: ${reports.size()}"

if (offset < reports.size()) {

  def report = reports[-offset]

    def json = new JsonSlurper().parseText(report.data)
    log.info "JSON: ${json}"
  log.info "report.data: ${report.data}"
  response.setHeader("Access-Control-Allow-Origin", "*")
  response.setHeader("Access-Control-Allow-Methods", "GET")
  response.setHeader("Access-Control-Allow-Credentials", "true")
  response.setHeader("Content-Type", "application/json")
  json.id = report.id
  log.info "Id: ${report.id}"
  out.print new JsonBuilder(json).toPrettyString()
} else if (offset) {
  response.sendError(404)
} else {
  response.setHeader("Access-Control-Allow-Origin", "*")
  response.setHeader("Access-Control-Allow-Methods", "GET")
  response.setHeader("Access-Control-Allow-Credentials", "true")
  response.setHeader("Content-Type", "application/json")
  out.println '''{
    "data": [
        {
            "name": "Everything Is Groovy",
            "rank": "1,624",
            "avgSteps": "2,343",
            "members": [
                {
                    "name": "RAHUL SOMASUNDERAM",
                    "score": "19,988"
                },
                {
                    "name": "JAMES GOUGH",
                    "score": "9,963"
                },
                {
                    "name": "TIAN LAI",
                    "score": "4,542"
                },
                {
                    "name": "HUAXIA WANG",
                    "score": "6,638"
                },
                {
                    "name": "SREENIVASULU GUNDLURI",
                    "score": "2,782"
                },
                {
                    "name": "YUAN YAO",
                    "score": "1,808"
                },
                {
                    "name": "CHUN CHI TUNG",
                    "score": "1,129"
                },
                {
                    "name": "QIAOLIN JIN",
                    "score": "-"
                },
                {
                    "name": "BING ZHANG",
                    "score": "-"
                },
                {
                    "name": "GREG MANOUKIAN",
                    "score": "-"
                }
            ]
        },
        {
            "name": "Zippity",
            "rank": "1,036",
            "avgSteps": "3,472",
            "members": [
                {
                    "name": "LILAC CHANG",
                    "score": "17,885"
                },
                {
                    "name": "JOHN TRACY",
                    "score": "8,157"
                },
                {
                    "name": "NICOLE MATZINGER",
                    "score": "7,131"
                },
                {
                    "name": "STEPHEN JR KEMPF",
                    "score": "5,772"
                },
                {
                    "name": "CAITLYN YBARRA",
                    "score": "11,080"
                },
                {
                    "name": "DEREK TAKEGAMI",
                    "score": "10,755"
                },
                {
                    "name": "ERSEL KULLOLLI",
                    "score": "5,017"
                },
                {
                    "name": "JOHN REED",
                    "score": "3,640"
                },
                {
                    "name": "JULIE FROLEN",
                    "score": "-"
                },
                {
                    "name": "GEORGE MORRIS",
                    "score": "-"
                }
            ]
        },
        {
            "name": "Sole Searchers",
            "rank": "1,296",
            "avgSteps": "2,901",
            "members": [
                {
                    "name": "DANIEL SKEMPTON",
                    "score": "25,423"
                },
                {
                    "name": "ANNA SERRANO",
                    "score": "3,079"
                },
                {
                    "name": "THOMAS KENDIG",
                    "score": "503"
                },
                {
                    "name": "JULIE PRESTRIDGE",
                    "score": "-"
                },
                {
                    "name": "STEVEN PASKI",
                    "score": "-"
                }
            ]
        },
        {
            "name": "Campbell's 100 day dashers!",
            "rank": "2,652",
            "avgSteps": "77",
            "members": [
                {
                    "name": "RICHARD KUHLER",
                    "score": "1,077"
                },
                {
                    "name": "AMIR AHMED",
                    "score": "-"
                },
                {
                    "name": "CARRIE THOMAS",
                    "score": "-"
                },
                {
                    "name": "ANUPAMA KRISHNAN",
                    "score": "-"
                },
                {
                    "name": "JOSETTE NOBRIGA",
                    "score": "-"
                },
                {
                    "name": "ARTI KULKARNI",
                    "score": "-"
                },
                {
                    "name": "PEAUN LEE",
                    "score": "-"
                }
            ]
        },
        {
            "name": "San Diego Ala Carte",
            "rank": "1,786",
            "avgSteps": "2,079",
            "members": [
                {
                    "name": "JOHN MALPEDE",
                    "score": "10,472"
                },
                {
                    "name": "GLENN VOYLES",
                    "score": "6,799"
                },
                {
                    "name": "DEAN GANZER",
                    "score": "11,378"
                },
                {
                    "name": "SHANNON SHIH",
                    "score": "3,835"
                },
                {
                    "name": "MARK OSTRUM",
                    "score": "3,104"
                },
                {
                    "name": "CHRISTA HAMASHIN",
                    "score": "1,574"
                },
                {
                    "name": "MARIA SARAH ELL OSTRUM",
                    "score": "256"
                },
                {
                    "name": "JIAN SHI",
                    "score": "-"
                },
                {
                    "name": "THOMAS VAZAKAS",
                    "score": "-"
                }
            ]
        },
        {
            "name": "Red Hot Chili Steppers",
            "rank": "208",
            "avgSteps": "6,823",
            "members": [
                {
                    "name": "RICHARD GRIGSBY",
                    "score": "19,250"
                },
                {
                    "name": "LOVELYN LAYUG",
                    "score": "16,274"
                },
                {
                    "name": "ROBERT BADERE",
                    "score": "10,349"
                },
                {
                    "name": "WILLIE KIM",
                    "score": "9,894"
                },
                {
                    "name": "ERIKA LANE",
                    "score": "8,232"
                },
                {
                    "name": "KEVIN HERDMAN",
                    "score": "2,826"
                },
                {
                    "name": "MICHAEL WITT",
                    "score": "1,403"
                },
                {
                    "name": "KEVIN SCHRAMM",
                    "score": "-"
                },
                {
                    "name": "MARJORIE SWINGLE",
                    "score": "-"
                },
                {
                    "name": "J VERSTEEG",
                    "score": "-"
                }
            ]
        },
        {
            "name": "Borho's Bunch",
            "rank": "900",
            "avgSteps": "3,805",
            "members": [
                {
                    "name": "FREDERICK BORHO",
                    "score": "17,784"
                },
                {
                    "name": "DARRYL VAN CLEAVE",
                    "score": "14,844"
                },
                {
                    "name": "MICHAEL BECKHAM",
                    "score": "6,272"
                },
                {
                    "name": "DHANESH FRANCIS",
                    "score": "7,615"
                },
                {
                    "name": "NOVINE HENRY",
                    "score": "5,532"
                },
                {
                    "name": "AIDE CORONA",
                    "score": "1,222"
                },
                {
                    "name": "FRANCISCO MARTINEZ",
                    "score": "-"
                }
            ]
        },
        {
            "name": "SD Steppers - 100 DD",
            "rank": "2,195",
            "avgSteps": "1,354",
            "members": [
                {
                    "name": "KEITH GLASSFORD",
                    "score": "9,473"
                },
                {
                    "name": "ROBERT NEWLIN JR",
                    "score": "1,355"
                },
                {
                    "name": "RUI WEN",
                    "score": "-"
                },
                {
                    "name": "DAVID ROBERTS",
                    "score": "-"
                },
                {
                    "name": "SUDHAKAR PUTTA",
                    "score": "-"
                },
                {
                    "name": "YVONNE BENTLEY",
                    "score": "-"
                },
                {
                    "name": "SCOTT STEVENS",
                    "score": "-"
                },
                {
                    "name": "MUHAMMAD DASTAGIR",
                    "score": "-"
                }
            ]
        },
        {
            "name": "Wanna Step Outside",
            "rank": "1,791",
            "avgSteps": "2,069",
            "members": [
                {
                    "name": "LUONG LE",
                    "score": "10,876"
                },
                {
                    "name": "ANTHONY CHIKA",
                    "score": "6,291"
                },
                {
                    "name": "RESHMA SINGH",
                    "score": "3,525"
                },
                {
                    "name": "THOMAS GREENE",
                    "score": "-"
                },
                {
                    "name": "PENNY LISCHER",
                    "score": "-"
                },
                {
                    "name": "SHANNON OBRIEN",
                    "score": "-"
                },
                {
                    "name": "AURORA HAY",
                    "score": "-"
                },
                {
                    "name": "TRAVIS ZANDER",
                    "score": "-"
                },
                {
                    "name": "CHRISTIAN ROCAMORA",
                    "score": "-"
                },
                {
                    "name": "CHRISTOPHER RUDY",
                    "score": "-"
                }
            ]
        }
    ],
    "date": "2015-06-16T04:31:51+0000",
    "id": 5725485073956864
}'''
}
