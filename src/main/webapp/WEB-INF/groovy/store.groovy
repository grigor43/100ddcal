import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import h100.CacheHelper
import h100.Report

import java.text.SimpleDateFormat

log.info "Setting attribute datetime"

request.setAttribute 'datetime', new Date().toString()
def jsonString = request.reader.text
def j = new JsonSlurper().parseText(jsonString) as Map

Report r = new Report()
SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");


r.date = ISO8601DATEFORMAT.parse(j.date)
r.data = jsonString
log.info "r.data: ${r.data}"

r.save()
CacheHelper.clearAll()

log.info "R Saved as ${r.id}"

log.info "Forwarding to the template"

forward '/WEB-INF/pages/datetime.gtpl'
