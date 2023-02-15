package gov.uk.courtdata.model.eformsApplication.xmlModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "fielddata")
public class EformsApplicationFieldData {

    @JsonProperty("Formtype")
    private String formType;
    @JsonProperty("Provider_uniquename")
    private String providerUniqueName;
    @JsonProperty("Provider_displayname")
    private String providerDisplayName;
    @JsonProperty("Provider_firm_id")
    private Integer providerFirmId;
    @JsonProperty("Usn")
    private Long usn;
    @JsonProperty("Date_received")
    private LocalDateTime dateReceived;
    @JsonProperty("Time_received")
    private LocalDateTime timeReceived;
    @JsonProperty("Submitter_language")
    private String submitterLanguage;
    @JsonProperty("Datestamp_usn")
    private Long datestampUsn;
    @JsonProperty("Datestamp_date")
    private LocalDateTime datestampDate;
    @JsonProperty("Datestamp_time")
    private LocalDateTime datestampTime;
    @JsonProperty("Datestamp_clientname")
    private String datestampClientName;
    @JsonProperty("Datestamp_clientdob")
    private LocalDateTime datestampClientDob;
    @JsonProperty("Application_type")
    private String applicationType;
    @JsonProperty("Prev_app_maat_cifc")
    private String prevAppMaatCifc;
    @JsonProperty("Summary")
    private boolean summary;
    @JsonProperty("Either_way")
    private boolean eitherWay;
    @JsonProperty("Indictable")
    private boolean indictable;
    @JsonProperty("Trial_in_crown_court")
    private boolean trialInCrownCourt;
    @JsonProperty("Committal")
    private boolean committal;
    @JsonProperty("Appeal_to_crown_court")
    private boolean appealToCrownCourt;
    @JsonProperty("Appeal_no_changes")
    private boolean appealNoChanges;
    @JsonProperty("Prev_app_maat")
    private String prevAppMaat;
    @JsonProperty("Court_originating")
    private String courtOriginating;
    @JsonProperty("Court_originating_display")
    private String courtOriginatingDisplay;
    @JsonProperty("Court_name")
    private String courtName;
    @JsonProperty("Court_name_display")
    private String courtNameDisplay;
    @JsonProperty("Priority")
    private String priority;
    @JsonProperty("Custody")
    private boolean custody;
    @JsonProperty("Vulnerable")
    private boolean vulnerable;
    @JsonProperty("Youth")
    private boolean youth;
    @JsonProperty("Late_application_cc")
    private boolean lateApplicationCc;
    @JsonProperty("Hearing_date_imminent")
    private boolean hearingDateImminent;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Title_other")
    private String titleOther;
    @JsonProperty("Forenames")
    private String forenames;
    @JsonProperty("Other_names")
    private String otherNames;
    @JsonProperty("Surname")
    private String surname;
    @JsonProperty("Date_of_birth")
    private LocalDateTime dateOfBirth;
    @JsonProperty("Ni_number")
    private String niNumber;
    @JsonProperty("Arc_number")
    private String arcNumber;
    @JsonProperty("Welsh_corr_flag")
    private boolean welshCorrFlag;
    @JsonProperty("Have_home_address")
    private String haveHomeAddress;
    @JsonProperty("Home_address_1")
    private String homeAddress1;
    @JsonProperty("Home_address_2")
    private String homeAddress2;
    @JsonProperty("Home_address_3")
    private String homeAddress3;
    @JsonProperty("Home_postcode")
    private String homePostcode;
    @JsonProperty("What_contact_address")
    private String whatContactAddress;
    @JsonProperty("Contact_address_1")
    private String contactAddress1;
    @JsonProperty("Contact_address_2")
    private String contactAddress2;
    @JsonProperty("Contact_address_3")
    private String contactAddress3;
    @JsonProperty("Contact_postcode")
    private String contactPostcode;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("Phone_landline")
    private String phoneLandline;
    @JsonProperty("Phone_mobile")
    private String phoneMobile;
    @JsonProperty("Phone_work")
    private String phoneWork;
    @JsonProperty("Home_address_type")
    private String homeAddressType;
    @JsonProperty("Relationship_to_home_owner")
    private String relationshipToHomeOwner;
    @JsonProperty("Under_18")
    private String under18;
    @JsonProperty("Charged_with_adult")
    private String chargedWithAdult;
    @JsonProperty("Have_partner")
    private short havePartner;
    @JsonProperty("Marital_status")
    private String maritalStatus;
    @JsonProperty("Marital_status_2")
    private String maritalStatus2;
    @JsonProperty("Have_partner_copy")
    private short havePartnerCopy;
    @JsonProperty("Partner_title")
    private String partnerTitle;
    @JsonProperty("Partner_title_other")
    private String partnerTitleOther;
    @JsonProperty("Partner_forenames")
    private String partnerForenames;
    @JsonProperty("Partner_other_names")
    private String partnerOtherNames;
    @JsonProperty("Partner_surname")
    private String partnerSurname;
    @JsonProperty("Partner_ni")
    private String partnerNi;
    @JsonProperty("Partner_arc")
    private String partnerArc;
    @JsonProperty("Partner_involved_in_case")
    private String partnerInvolvedInCase;
    @JsonProperty("Partner_involvement")
    private String partnerInvolvement;
    @JsonProperty("Partner_conflict_of_interest")
    private String partnerConflictOfInterest;
    @JsonProperty("Partner_different_home")
    private String partnerDifferentHome;
    @JsonProperty("Partner_usual_address_1")
    private String partnerUsualAddress1;
    @JsonProperty("Partner_usual_address_2")
    private String partnerUsualAddress2;
    @JsonProperty("Partner_usual_address_3")
    private String partnerUsualAddress3;
    @JsonProperty("Partner_usual_postcode")
    private String partnerUsualPostcode;
    @JsonProperty("Offence_type")
    private String offenceType;
    @JsonProperty("Any_codefendants")
    private String anyCodefendants;
    @JsonProperty("Codefendants_details")
    private List<String> codefendantsDetails;
    @JsonProperty("Why_not_rep_same_solicitor")
    private String whyNotRepSameSolicitor;
    @JsonProperty("Why_not_rep_same_solicitor_reasons")
    private String whyNotRepSameSolicitorReasons;
    @JsonProperty("Other_cases")
    private String otherCases;
    @JsonProperty("Other_case_charges")
    private String otherCaseCharges;
    @JsonProperty("Other_case_court")
    private String otherCaseCourt;
    @JsonProperty("Partner_other_case_charges")
    private String partnerOtherCaseCharges;
    @JsonProperty("Partner_other_case_court")
    private String partnerOtherCaseCourt;
    @JsonProperty("Hearing_date")
    private LocalDateTime hearingDate;
    @JsonProperty("Case_type_calc")
    private short caseTypeCalc;
    @JsonProperty("Lose_liberty")
    private boolean loseLiberty;
    @JsonProperty("Lose_liberty_details")
    private String loseLibertyDetails;
    @JsonProperty("Suspended_sentence")
    private boolean suspendedSentence;
    @JsonProperty("Suspended_sentence_details")
    private String suspendedSentenceDetails;
    @JsonProperty("Lose_livelihood")
    private boolean loseLivelihood;
    @JsonProperty("Lose_livelihood_details")
    private String loseLivelihoodDetails;
    @JsonProperty("Damage_reputation")
    private boolean damageReputation;
    @JsonProperty("Damage_reputation_details")
    private String damageReputationDetails;
    @JsonProperty("Question_of_law")
    private boolean questionOfLaw;
    @JsonProperty("Question_of_law_details")
    private String questionOfLawDetails;
    @JsonProperty("Cant_present_own_case")
    private boolean cantPresentOwnCase;
    @JsonProperty("Cant_present_own_case_details")
    private String cantPresentOwnCaseDetails;
    @JsonProperty("Witness_trace")
    private boolean witnessTrace;
    @JsonProperty("Witness_trace_details")
    private String witnessTraceDetails;
    @JsonProperty("Expert_cross_exam")
    private boolean expertCrossExam;
    @JsonProperty("Expert_cross_exam_details")
    private String expertCrossExamDetails;
    @JsonProperty("Interests_of_another")
    private boolean interestsOfAnother;
    @JsonProperty("Interests_of_another_details")
    private String interestsOfAnotherDetails;
    @JsonProperty("Other_reason_to_be_represented")
    private boolean otherReasonToBeRepresented;
    @JsonProperty("Other_reason_to_be_represented_details")
    private String otherReasonToBeRepresentedDetails;
    @JsonProperty("Remanded_in_custody")
    private short remandedInCustody;
    @JsonProperty("Heard_in_magistrates_court")
    private String heardInMagistratesCourt;
    @JsonProperty("Indictable_offence")
    private short indictableOffence;
    @JsonProperty("Firm_office")
    private String firmOffice;
    @JsonProperty("Legal_rep_laa_account")
    private String legalRepLaaAccount;
    @JsonProperty("Solicitor_title")
    private String solicitorTitle;
    @JsonProperty("Solicitor_title_other")
    private String solicitorTitleOther;
    @JsonProperty("Legal_rep_fullname")
    private String legalRepFullName;
    @JsonProperty("Solicitor_firm")
    private String solicitorFirm;
    @JsonProperty("Solicitor_address_1")
    private String solicitorAddress1;
    @JsonProperty("Solicitor_address_2")
    private String solicitorAddress2;
    @JsonProperty("Solicitor_address_3")
    private String solicitorAddress3;
    @JsonProperty("Solicitor_postcode")
    private String solicitorPostcode;
    @JsonProperty("Solicitor_phone_landline")
    private String solicitorPhoneLandline;
    @JsonProperty("Solicitor_phone_mobile")
    private String solicitorPhoneMobile;
    @JsonProperty("Solicitor_firm_dx")
    private String solicitorFirmDx;
    @JsonProperty("Solicitor_fax")
    private String solicitorFax;
    @JsonProperty("Solicitor_email")
    private String solicitorEmail;
    @JsonProperty("Firm_administrator_email")
    private String firmAdministratorEmail;
    @JsonProperty("Legal_rep_declaration")
    private String legalRepDeclaration;
    @JsonProperty("Legal_rep_declaration_2")
    private String legalRepDeclaration2;
    @JsonProperty("Legal_rep_declaration_confirm")
    private boolean legalRepDeclarationConfirm;
    @JsonProperty("Legal_rep_sign")
    private String legalRepSign;
    @JsonProperty("Legal_rep_sign_date")
    private LocalDateTime legalRepSignDate;
    @JsonProperty("Charges_brought")
    private List<Charge> chargesBrought;


}
