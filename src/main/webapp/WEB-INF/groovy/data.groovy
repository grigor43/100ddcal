import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import h100.DataHelper
import h100.Report

log.info "Params: ${params}"

def offset = params.offset?.toInteger() ?: 0

List<Report> reports = DataHelper.reports

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
    if (offset > 0) {
        response.setHeader("Cache-Control", "public, max-age=14400")
    }
    json.id = report.id
    log.info "Id: ${report.id}"
    out.print new JsonBuilder(json).toPrettyString()
} else {
    response.sendError(404)
}
