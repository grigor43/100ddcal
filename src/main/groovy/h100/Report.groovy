package h100

import groovyx.gaelyk.datastore.Entity

/**
 * Created by rahulsomasunderam on 8/18/14.
 */
@Entity
class Report implements Serializable {
    Date date
    String data
}
