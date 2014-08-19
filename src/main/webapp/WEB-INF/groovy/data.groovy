import groovy.json.JsonSlurper
import h100.CacheHelper
import h100.Report

log.info "Params: ${params}"

def offset = params.offset?.toInteger() ?: 0

List<Report> reports = CacheHelper.get('data') {
  Report.findAll()
}

log.info "${reports}"

def report = reports[-offset]
log.info "JSON: ${new JsonSlurper().parseText(report.data)}"
log.info "report.data: ${report.data}"
response.setHeader("Access-Control-Allow-Origin", "*")
response.setHeader("Access-Control-Allow-Methods", "GET")
response.setHeader("Access-Control-Allow-Credentials", "true")
response.setHeader("Content-Type", "application/json")

out.print report.data
