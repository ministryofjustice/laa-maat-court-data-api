package gov.uk.courtdata.entity;


import lombok.*;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_WQ_DEFENDANT",schema = "MLA")
public class WQDefendant {

    @Id
    @Column(name = "TX_ID")
    private Integer txId;
    @Column(name = "CASE_ID")
    private Integer caseId;
    @Column(name = "FORENAME")
    private String forename;
    @Column(name = "SURNAME")
    private String surname;
    @Column(name = "ORGANISATION")
    private String organisation;
    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;
    @Column(name = "ADDRESS_LINE1")
    private String addressLine1;
    @Column(name = "ADDRESS_LINE2")
    private String addressLine2;
    @Column(name = "ADDRESS_LINE3")
    private String addressLine3;
    @Column(name = "ADDRESS_LINE4")
    private String addressLine4;
    @Column(name = "ADDRESS_LINE5")
    private String addressLine5;
    @Column(name = "POST_CODE")
    private String postCode;
    @Column(name = "NINO")
    private String nino;
    @Column(name = "TELEPHONE_HOME")
    private String telephoneHome;
    @Column(name = "TELEPHONE_WORK")
    private String telephoneWork;
    @Column(name = "TELEPHONE_MOBILE")
    private String telephoneMobile;
    @Column(name = "EMAIL1")
    private String email1;
    @Column(name = "EMAIL2")
    private String email2;

}
