import groovy.json.JsonSlurper
import h100.DataHelper
import h100.Report

import java.text.SimpleDateFormat

log.info "Params: ${params}"

def startDate = new SimpleDateFormat('yyyy-MM-dd').parse(params.startDate ?: '2015-06-12')
def searchQuery = (params.q.split(',') ?: ['rahul', 'Lovelyn', 'Sreeni']) as List
def startDay = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse('2015-06-15T00:00:00-0500')

Map<String, List<Report>> reports = DataHelper.reports.
        findAll { Report r -> r.date > startDate }.
        sort { Report r -> r.date.time }.
        groupBy { Report r -> r.date.format('yyyy-MM-dd') }.
        sort { a, b -> a.key <=> b.key }

def persons = reports.values().flatten().
        collectMany { Report r ->
            def json = new JsonSlurper().parseText(r.data)
            json.data.collectMany {it.members}.collect {it.name}
        }.
        unique().
        findAll {name -> searchQuery.any {q -> name.toLowerCase().contains(q.toLowerCase())} }

log.info "Start Date: $startDate, Persons: $persons"

def rows = reports.
        collect { k, v ->
            List<Report> r2 = v
            Report r = r2.sort { Report r -> -r.date.time }.first()
            def json = new JsonSlurper().parseText(r.data)
            def personalScores = json.data.collectMany { it.members }
            List<String> scores = persons.collect { p ->
                personalScores.find { it.name == p }?.score ?: '0'
            }
            int divisor = use(groovy.time.TimeCategory) { (r.date - startDay).days } ?: 1
            def s2 = scores.collect { (it.replace(',', '').replace('-', '0').toInteger()) / divisor as int }
            s2.add(0, k)
            return s2
        }*.value

log.info "Available: ${rows.size()}"

response.setHeader("Access-Control-Allow-Origin", "*")
response.setHeader("Access-Control-Allow-Methods", "GET")
response.setHeader("Access-Control-Allow-Credentials", "true")
response.setHeader("Content-Type", "application/json")

persons.add(0, 'date')
out.println persons.join('\t')
rows.each {
    out.println it.join('\t')
}
