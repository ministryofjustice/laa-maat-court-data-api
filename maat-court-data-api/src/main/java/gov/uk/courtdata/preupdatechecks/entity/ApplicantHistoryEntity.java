package gov.uk.courtdata.preupdatechecks.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "APPLICANT_HISTORY", schema = "TOGDATA")
public class ApplicantHistoryEntity {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "applicant_history_gen_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "applicant_history_gen_seq")
    private Integer id;

    @Column(name = "APPL_ID")
    private Integer applId;

    @Column(name = "AS_AT_DATE")
    private LocalDate asAtDate;

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
    private Integer bankAccountNo;

    @Column(name = "BANK_ACCOUNT_NAME")
    private String bankAccountName;

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

    @Column(name = "SEND_TO_CCLF")
    private String sendToCclf;

    @Column(name = "SPECIAL_INVESTIGATION")
    private LocalDate specialInvestigation;

    @Column(name = "PHONE_WORK")
    private String phoneWork;

}
