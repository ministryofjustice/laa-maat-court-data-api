package gov.uk.courtdata.billing.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "APPLICANTS", schema = "TOGDATA")
@Data
public class ApplicantEntity {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "OTHER_NAMES")
    private String otherNames;

    @Column(name = "DOB")
    private LocalDate dob;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "NI_NUMBER")
    private String niNumber;

    @Column(name = "FOREIGN_ID")
    private String foreignId;

    @Column(name = "NO_FIXED_ABODE")
    private String noFixedAbode;

    @Column(name = "HOME_ADDR_ID")
    private Integer homeAddrId;

    @Column(name = "EMST_CODE")
    private String emstCode;

    @Column(name = "ETHN_ID")
    private Integer ethnId;

    @Column(name = "DISABLED")
    private String disabled;

    @Column(name = "PHONE_HOME")
    private String phoneHome;

    @Column(name = "PHONE_MOBILE")
    private String phoneMobile;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "SORT_CODE")
    private String sortCode;

    @Column(name = "BANK_ACCOUNT_NO")
    private String bankAccountNo;

    @Column(name = "BANK_ACCOUNT_NAME")
    private String bankAccountName;

    @Column(name = "SEND_TO_CCLF")
    private String sendToCclf;

    @Column(name = "DATE_CREATED", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "USER_CREATED", nullable = false)
    private String userCreated;

    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Column(name = "USER_MODIFIED")
    private String userModified;

    @Column(name = "PAME_PAYMENT_METHOD")
    private String pamePaymentMethod;

    @Column(name = "PREF_PAYMENT_DAY")
    private String prefPaymentDay;

    @Column(name = "SPECIAL_INVESTIGATION")
    private LocalDate specialInvestigation;

    @Column(name = "PHONE_WORK")
    private String phoneWork;
}