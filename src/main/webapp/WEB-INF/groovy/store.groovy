import groovy.json.JsonSlurper
import h100.Member
import h100.Report
import h100.Team

import java.text.SimpleDateFormat

log.info "Setting attribute datetime"

request.setAttribute 'datetime', new Date().toString()
def j = new JsonSlurper().parse(request.reader) as Map

Report r = new Report()
SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");


r.date = ISO8601DATEFORMAT.parse(j.date)
r.teams = j.data.collect { t ->
    def t1 = new Team()
    t1.name = t.name
    t1.rank = Integer.parseInt(t.rank.replace(',',''))
    t1.avgSteps = Long.parseLong(t.avgSteps.replace(',',''))
    t1.members = t.members.collect { m ->
        def m1 = new Member()
        m1.name = m.name

        String numeric = m.score.replace(',', '')
        m1.steps = numeric.isNumber() ? Long.parseLong(numeric) : -1
        m1
    }
    t1
}

r.save()
log.info "R Saved as ${r.id}"

log.info "Forwarding to the template"

forward '/WEB-INF/pages/datetime.gtpl'