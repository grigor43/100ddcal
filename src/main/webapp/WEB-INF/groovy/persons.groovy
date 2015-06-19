import groovy.json.JsonSlurper
import h100.CacheHelper
import h100.Report

log.info "Params: ${params}"

def offset = params.offset?.toInteger() ?: 288
def persons = (params.q ?: ['RAHUL SOMASUNDERAM']) as List

log.info "Offset: $offset, Persons: $persons"
def reports =
        CacheHelper.
                get('data') { Report.findAll().sort { Report r -> -r.date.time } }.
                sort { Report r -> r.date.time }.
                drop(offset - 1).
                groupBy { Report r -> r.date.format('yyyy-MM-dd') }.
                sort { a, b -> a.key <=> b.key }.
                collect { k, v ->
                    List<Report> r2 = v
                    Report r = r2.sort { Report r -> -r.date.time }.first()
                    def json = new JsonSlurper().parseText(r.data)
                    def personalScores = json.data.collectMany { it.members }
                    // log.info "PS: ${personalScores}"
                    log.info "Date: $k, PS: ${persons.collect { p -> personalScores.find { it.name == p }?.score }}"
                    List<String> scores = persons.collect { p -> personalScores.find { it.name.toLowerCase() == p.toLowerCase() }?.score ?: '0' }
                    def s2 = scores.collect { it.replace(',', '').replace('-', '0').toInteger() }
                    s2.add(0, k)
                    return s2
                }*.value

log.info "Available: ${reports.size()}"

response.setHeader("Access-Control-Allow-Origin", "*")
response.setHeader("Access-Control-Allow-Methods", "GET")
response.setHeader("Access-Control-Allow-Credentials", "true")
response.setHeader("Content-Type", "application/json")

persons.add(0, 'Date')
out.println persons.join('\t')
reports.each {
    out.println it.join('\t')
}
