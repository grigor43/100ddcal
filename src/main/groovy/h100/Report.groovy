package h100

import groovyx.gaelyk.datastore.Entity

/**
 * Created by rahulsomasunderam on 8/18/14.
 */
@Entity
class Report {
    Date date
    List<Team> teams
}

class Team {
    String name
    Integer rank
    Long avgSteps
    List<Member> members
}

class Member {
    String name
    Long steps
}