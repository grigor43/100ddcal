package h100

import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType

/**
 * Created by rahul on 6/19/15.
 */
class DataHelper {

    public static final Closure<Long> byTime = { Report r -> -r.date.time }

    static List<Report> getReports() {
        CacheHelper.get('data') { Report.findAll().sort byTime }
    }
}
