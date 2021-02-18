package gov.uk.courtdata.entity;

import javax.persistence.Column;
import java.util.Date;

public class BaseEntity {

    private String createdBy;

    @Column(name = "PLEA_DATE")
    private Date createdOn;

    @Column(name = "UPDATEDON")
    private Date upDatedOn;
}
