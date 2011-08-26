package com.sightlyinc.ratecred.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * A token assigned to publishers that enables our code to use SimpleGeo
 * on the publishers domain.
 *
 * The plan for now is to set up a list of "star" tokens, which are
 * tokens that can be used on any domain. These will be associated
 * with date ranges. A publisher who signs up will be assigned the
 * star token covering the date that the publisher signs up. At
 * the specified "end" date the star token will no longer be assigned
 * to new publishers. The star token will be manually converted
 * to work only for the domains of the publishers who signed up
 * between the start and end dates and got assigned that token.
 *
 * @author sam
 * @version $Id$
 */
@Entity
@Table(name="simple_geo_json_token")
public class SimpleGeoJsonToken extends BaseEntity {

    @Column(name = "json_token")
    private String jsonToken;
    @Column(name = "start_assignment_date")
    private long startAssignmentDate;
    @Column(name = "end_assignment_date")
    private long endAssignmentDate;

    public String getJsonToken() {
        return jsonToken;
    }

    public void setJsonToken(String jsonToken) {
        this.jsonToken = jsonToken;
    }

    /**
     * Start assigning this token to new publishers on this date.
     *
     * @return
     */
    public long getStartAssignmentDate() {
        return startAssignmentDate;
    }

    public void setStartAssignmentDate(long startAssignmentDate) {
        this.startAssignmentDate = startAssignmentDate;
    }

    /**
     * Stop assigning this token to new publishers on this date.
     * @return
     */
    public long getEndAssignmentDate() {
        return endAssignmentDate;
    }

    public void setEndAssignmentDate(long endAssignmentDate) {
        this.endAssignmentDate = endAssignmentDate;
    }
}
