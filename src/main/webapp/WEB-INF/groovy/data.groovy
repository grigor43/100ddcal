import groovy.json.JsonSlurper
import h100.CacheHelper
import h100.Report

log.info "Params: ${params}"

def offset = params.offset?.toInteger() ?: 0

List<Report> reports = CacheHelper.get('data') {
  Report.findAll().sort { Report r ->
    r.date
  }
}

log.info "${reports}"

if (offset < reports.size()) {

  def report = reports[-offset]
  log.info "JSON: ${new JsonSlurper().parseText(report.data)}"
  log.info "report.data: ${report.data}"
  response.setHeader("Access-Control-Allow-Origin", "*")
  response.setHeader("Access-Control-Allow-Methods", "GET")
  response.setHeader("Access-Control-Allow-Credentials", "true")
  response.setHeader("Content-Type", "application/json")

  out.print report.data
} else if (offset) {
  response.sendError(404)
} else {
  response.setHeader("Access-Control-Allow-Origin", "*")
  response.setHeader("Access-Control-Allow-Methods", "GET")
  response.setHeader("Access-Control-Allow-Credentials", "true")
  response.setHeader("Content-Type", "application/json")
  out.println '{"data":[{"name":"Aerobic Task Force","rank":"643","avgSteps":"4,218","members":[{"name":"RAHUL SOMASUNDERAM","score":"9,203"},{"name":"TIAN LAI","score":"7,936"},{"name":"YUAN YAO","score":"7,672"},{"name":"JONATHAN SHULTIS","score":"1,945"},{"name":"ERIC FAN","score":"1,781"},{"name":"SONGDUK PARK","score":"988"},{"name":"JAMES GOUGH","score":"-"}]},{"name":"One Hit Runners","rank":"98","avgSteps":"8,017","members":[{"name":"NICOLE MATZINGER","score":"12,839"},{"name":"DEREK TAKEGAMI","score":"9,636"},{"name":"CAITLYN YBARRA","score":"9,595"},{"name":"ANDREW BOWMAN","score":"-"}]},{"name":"Sole Searchers","rank":"1,651","avgSteps":"1,694","members":[{"name":"LINDA DUGGER","score":"7,927"},{"name":"AIDE CORONA","score":"5,520"},{"name":"THO QUACH","score":"108"},{"name":"ANNA SERRANO","score":"-"},{"name":"THOMAS KENDIG","score":"-"},{"name":"PEAUN LEE","score":"-"},{"name":"JOSETTE NOBRIGA","score":"-"},{"name":"STEVEN PASKI","score":"-"}]},{"name":"Zippity","rank":"258","avgSteps":"6,058","members":[{"name":"ERSEL KULLOLLI","score":"16,245"},{"name":"JULIE FROLEN","score":"12,198"},{"name":"GEORGE MORRIS","score":"10,154"},{"name":"DANIEL SKEMPTON","score":"7,804"},{"name":"JOHN TRACY","score":"7,726"},{"name":"KRISTIN NISIUS","score":"394"},{"name":"JOHN REED","score":"-"},{"name":"JING FANG","score":"-"},{"name":"JULIE PRESTRIDGE","score":"-"}]}],"date":"2014-08-19T21:03:31+0000"}'
}
