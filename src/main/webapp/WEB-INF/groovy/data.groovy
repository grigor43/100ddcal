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
            "rank": "1,378",
            "avgSteps": "12",
            "members": [
                {
                    "name": "RAHUL SOMASUNDERAM",
                    "score": "-"
                },
                {
                    "name": "YUAN YAO",
                    "score": "-"
                },
                {
                    "name": "TIAN LAI",
                    "score": "-"
                },
                {
                    "name": "HUAXIA WANG",
                    "score": "-"
                },
                {
                    "name": "SREENIVASULU GUNDLURI",
                    "score": "-"
                }
            ]
        },
        {
            "name": "Zippity",
            "rank": "1,521",
            "avgSteps": "12",
            "members": [
                {
                    "name": "JOHN REED",
                    "score": "-"
                },
                {
                    "name": "CAITLYN YBARRA",
                    "score": "-"
                },
                {
                    "name": "LILAC CHANG",
                    "score": "-"
                },
                {
                    "name": "NICOLE MATZINGER",
                    "score": "-"
                },
                {
                    "name": "JOHN TRACY",
                    "score": "-"
                },
                {
                    "name": "ERSEL KULLOLLI",
                    "score": "-"
                },
                {
                    "name": "JULIE FROLEN",
                    "score": "-"
                },
                {
                    "name": "STEPHEN JR KEMPF",
                    "score": "-"
                }
            ]
        }
    ],
    "date": "2015-06-03T18:18:52+0000",
    "id": 5736126123868160
}'''
}
