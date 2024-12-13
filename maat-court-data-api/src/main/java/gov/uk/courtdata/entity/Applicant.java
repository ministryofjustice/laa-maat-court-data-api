package gov.uk.courtdata.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "APPLICANTS", schema = "TOGDATA")
public class Applicant {
    @Id
    @SequenceGenerator(name = "applicants_gen_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "applicants_gen_seq")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "FIRST_NAME", length = 40)
    private String firstName;

    @Column(name = "LAST_NAME", length = 40)
    private String lastName;

    @Column(name = "OTHER_NAMES", length = 40)
    private String otherNames;

    @Column(name = "DOB")
    private LocalDate dob;

    @Column(name = "GENDER", length = 20)
    private String gender;

    @Column(name = "NI_NUMBER", length = 10)
    private String niNumber;

    @Column(name = "FOREIGN_ID", length = 20)
    private String foreignId;

    @Column(name = "NO_FIXED_ABODE", length = 1)
    private String noFixedAbode;

    @Column(name = "PHONE_HOME", length = 20)
    private String phoneHome;

    @Column(name = "PHONE_MOBILE", length = 20)
    private String phoneMobile;

    @Column(name = "EMAIL", length = 50)
    private String email;

    @Column(name = "SORT_CODE", length = 8)
    private String sortCode;

    @Column(name = "BANK_ACCOUNT_NO", length = 15)
    private String bankAccountNo;

    @Column(name = "BANK_ACCOUNT_NAME", length = 50)
    private String bankAccountName;

    @Column(name = "SEND_TO_CCLF", length = 1)
    private String sendToCclf;

    @CreationTimestamp
    @Column(name = "DATE_CREATED", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "USER_CREATED", nullable = false, length = 100)
    private String userCreated;

    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Column(name = "USER_MODIFIED", length = 100)
    private String userModified;

    @Column(name = "PREF_PAYMENT_DAY", length = 2)
    private String prefPaymentDay;

    @Column(name = "SPECIAL_INVESTIGATION")
    private LocalDateTime specialInvestigation;

    @Column(name = "PHONE_WORK", length = 20)
    private String phoneWork;

    @Column(name = "HOME_ADDR_ID")
    private Integer homeAddressId;

    @Column(name = "EMST_CODE")
    private String emstCode;

    @Column(name = "ETHN_ID")
    private Integer ethnId;

    @Column(name = "DISABLED")
    private String disabled;

    @Column(name = "PAME_PAYMENT_METHOD")
    private String pamePaymentMethod;

}