package gov.uk.courtdata.model.eformsApplication.xmlModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlSchemaType;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "fielddata")
public class EformsApplicationFieldData {

    @JsonProperty("Fc_form_is_read_only")
    private boolean fcFormIsReadOnly;
    @JsonProperty("Fc_caseworker_completing_funding_decision_manually")
    private boolean fcCaseWorkerCompletingFundingDecisionManually;
    @JsonProperty("Fc_eligible_for_staging_inject")
    private boolean fcEligibleForStagingInject;
    @JsonProperty("Fc_eligible_for_return_inject")
    private boolean fcEligibleForReturnInject;
    @JsonProperty("Fc_eligible_for_assessment_result_inject")
    private boolean fcEligibleForAssessmentResultInject;
    @JsonProperty("Fc_maat_id_injected")
    private boolean fcMaatIdInjected;
    @JsonProperty("Fc_staging_inject_result")
    private Object fcStagingInjectResult;
    @JsonProperty("Fc_return_inject_result")
    private Object fcReturnInjectResult;
    @JsonProperty("Fc_assessment_result_inject_result")
    private String fcAssessmentResultInjectResult;
    @JsonProperty("Fc_currentstage")
    private String fcCurrentStage;
    @JsonProperty("Formtype")
    private String formType;
    @JsonProperty("Xsd_name")
    private String xsdName;
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
    @JsonProperty("Employed")
    private short employed;
    @JsonProperty("Partner_employed")
    private String partnerEmployed;
    @JsonProperty("Receive_benefits")
    private String receiveBenefits;
    @JsonProperty("Income_support")
    private boolean incomeSupport;
    @JsonProperty("Esa")
    private boolean esa;
    @JsonProperty("State_pension")
    private boolean statePension;
    @JsonProperty("Jsa")
    private boolean jsa;
    @JsonProperty("Income_support_partner")
    private boolean incomeSupportPartner;
    @JsonProperty("Esa_partner")
    private boolean esaPartner;
    @JsonProperty("State_pension_partner")
    private boolean statePensionPartner;
    @JsonProperty("Jsa_partner")
    private boolean jsaPartner;
    @JsonProperty("Dwp_check_result")
    private String dwpCheckResult;
    @JsonProperty("Who_dwp_checked")
    private String whoDwpChecked;
    @JsonProperty("Do_you_have_proof")
    private String doYouHaveProof;
    @JsonProperty("Private_company")
    private String privateCompany;
    @JsonProperty("Partner_private_company")
    private String partnerPrivateCompany;
    @JsonProperty("Income_over_set_amount")
    private String incomeOverSetAmount;
    @JsonProperty("Wage_paid_every")
    private String wagePaidEvery;
    @JsonProperty("Wage_tax")
    private String wageTax;
    @JsonProperty("Partner_wage_paid_every")
    private String partnerWagePaidEvery;
    @JsonProperty("Partner_wage_tax")
    private String partnerWageTax;
    @JsonProperty("Child_benefit")
    private short childBenefit;
    @JsonProperty("Child_benefit_paid_every")
    private short childBenefitPaidEvery;
    @JsonProperty("Partner_child_benefit")
    private short partnerChildBenefit;
    @JsonProperty("Partner_child_benefit_paid_every")
    private short partnerChildBenefitPaidEvery;
    @JsonProperty("Tax_credits_amount")
    private short taxCreditsAmount;
    @JsonProperty("Tax_credits_paid_every")
    private short taxCreditsPaidEvery;
    @JsonProperty("Partner_tax_credits_amount")
    private short partnerTaxCreditsAmount;
    @JsonProperty("Partner_tax_credits_paid_every")
    private short partnerTaxCreditsPaidEvery;
    @JsonProperty("Universal_credit")
    private short universalCredit;
    @JsonProperty("Universal_credit_paid_every")
    private short universalCreditPaidEvery;
    @JsonProperty("Partner_universal_credit")
    private short partnerUniversalCredit;
    @JsonProperty("Partner_universal_credit_paid_every")
    private short partnerUniversalCreditPaidEvery;
    @JsonProperty("Other_benefits_paid_every")
    private String otherBenefitsPaidEvery;
    @JsonProperty("Partner_other_benefits_paid_every")
    private String partnerOtherBenefitsPaidEvery;
    @JsonProperty("Maintenance")
    private short maintenance;
    @JsonProperty("Maintenance_paid_every")
    private short maintenancePaidEvery;
    @JsonProperty("Partner_maintenance")
    private short partnerMaintenance;
    @JsonProperty("Partner_maintenance_paid_every")
    private String partnerMaintenancePaidEvery;
    @JsonProperty("Pensions_paid_every")
    private String pensionsPaidEvery;
    @JsonProperty("Partner_pensions_paid_every")
    private String partnerPensionsPaidEvery;
    @JsonProperty("Other_income_paid_every")
    private String otherIncomePaidEvery;
    @JsonProperty("Other_income_source")
    private String otherIncomeSource;
    @JsonProperty("Partner_other_income_paid_every")
    private String partnerOtherIncomePaidEvery;
    @JsonProperty("Partner_other_income_source")
    private String partnerOtherIncomeSource;
    @JsonProperty("Freezing_order")
    private String freezingOrder;
    @JsonProperty("Own_land_or_property")
    private String ownLandOrProperty;
    @JsonProperty("Savings_or_investments")
    private String savingsOrInvestments;
    @JsonProperty("How_pay_bills")
    private String howPayBills;
    @JsonProperty("Employers_crm15")
    private short employersCrm15;
    @JsonProperty("Self_assessment_tax_received")
    private String selfAssessmentTaxReceived;
    @JsonProperty("Partner_self_assessment_tax_received")
    private String partnerSelfAssessmentTaxReceived;
    @JsonProperty("Tax_liability_paid_every")
    private String taxLiabilityPaidEvery;
    @JsonProperty("Partner_tax_liability_paid_every")
    private String partnerTaxLiabilityPaidEvery;
    @JsonProperty("Non_cash_benefit")
    private String nonCashBenefit;
    @JsonProperty("Partner_non_cash_benefit")
    private String partnerNonCashBenefit;
    @JsonProperty("Receiving_benefits_2")
    private String receivingBenefits2;
    @JsonProperty("Partner_receiving_benefits_2")
    private String partnerReceivingBenefits2;
    @JsonProperty("State_pension_crm15_paid_every")
    private String statePensionCrm15PaidEvery;
    @JsonProperty("Child_benefit_crm15")
    private short childBenefitCrm15;
    @JsonProperty("Child_benefit_crm15_paid_every")
    private short childBenefitCrm15PaidEvery;
    @JsonProperty("Tax_credit_crm15")
    private short taxCreditCrm15;
    @JsonProperty("Tax_credit_crm15_paid_every")
    private short taxCreditCrm15PaidEvery;
    @JsonProperty("Universal_credit_crm15")
    private short universalCreditCrm15;
    @JsonProperty("Universal_credit_crm15_paid_every")
    private String universalCreditCrm15PaidEvery;
    @JsonProperty("Incapacity_benefit_crm15_paid_every")
    private String incapacityBenefitCrm15PaidEvery;
    @JsonProperty("Disablement_benefit_crm15_paid_every")
    private String disablementBenefitCrm15PaidEvery;
    @JsonProperty("Jsa_crm15_paid_every")
    private String jsaCrm15PaidEvery;
    @JsonProperty("Other_benefits_crm15_details")
    private String otherBenefitsCrm15Details;
    @JsonProperty("Other_benefits_crm15_paid_every")
    private String otherBenefitsCrm15PaidEvery;
    @JsonProperty("Partner_state_pension_crm15_paid_every")
    private String partnerStatePensionCrm15PaidEvery;
    @JsonProperty("Partner_child_benefit_crm15")
    private short partnerChildBenefitCrm15;
    @JsonProperty("Partner_child_benefit_crm15_paid_every")
    private String partnerChildBenefitCrm15PaidEvery;
    @JsonProperty("Partner_tax_credit_crm15")
    private short partnerTaxCreditCrm15;
    @JsonProperty("Partner_tax_credit_crm15_paid_every")
    private String partnerTaxCreditCrm15PaidEvery;
    @JsonProperty("Partner_universal_credit_crm15")
    private short partnerUniversalCreditCrm15;
    @JsonProperty("Partner_universal_credit_crm15_paid_every")
    private String partnerUniversalCreditCrm15PaidEvery;
    @JsonProperty("Partner_incapacity_benefit_crm15_paid_every")
    private String partnerIncapacityBenefitCrm15PaidEvery;
    @JsonProperty("Partner_disablement_benefit_crm15_paid_every")
    private String partnerDisablementBenefitCrm15PaidEvery;
    @JsonProperty("Partner_jsa_crm15_paid_every")
    private String partnerJsaCrm15PaidEvery;
    @JsonProperty("Partner_other_benefits_crm15_details")
    private String partnerOtherBenefitsCrm15Details;
    @JsonProperty("Partner_other_benefits_crm15_paid_every")
    private String partnerOtherBenefitsCrm15PaidEvery;
    @JsonProperty("Year_8_to_10")
    private short year8To10;
    @JsonProperty("Year_13_to_15")
    private short year13To15;
    @JsonProperty("Receive_pension")
    private String receivePension;
    @JsonProperty("Partner_receive_pension")
    private String partnerReceivePension;
    @JsonProperty("Total_pension_paid_every")
    private String totalPensionPaidEvery;
    @JsonProperty("Partner_total_pension_paid_every")
    private String partnerTotalPensionPaidEvery;
    @JsonProperty("Receive_maintenance_payments")
    private String receiveMaintenancePayments;
    @JsonProperty("Partner_receive_maintenance_payments")
    private String partnerReceiveMaintenancePayments;
    @JsonProperty("Maintenance_payment")
    private short maintenancePayment;
    @JsonProperty("Maintenance_payment_paid_every")
    private short maintenancePaymentPaidEvery;
    @JsonProperty("Partner_maintenance_payment")
    private short partnerMaintenancePayment;
    @JsonProperty("Partner_maintenance_payment_paid_every")
    private String partnerMaintenancePaymentPaidEvery;
    @JsonProperty("Receive_interest")
    private String receiveInterest;
    @JsonProperty("Partner_receive_interest")
    private String partnerReceiveInterest;
    @JsonProperty("Interest_paid_every")
    private String interestPaidEvery;
    @JsonProperty("Partner_interest_paid_every")
    private String partnerInterestPaidEvery;
    @JsonProperty("Income_from_other_sources")
    private String incomeFromOtherSources;
    @JsonProperty("Partner_income_from_other_sources")
    private String partnerIncomeFromOtherSources;
    @JsonProperty("Student_loan")
    private boolean studentLoan;
    @JsonProperty("Family_rent")
    private boolean familyRent;
    @JsonProperty("Other_rent")
    private boolean otherRent;
    @JsonProperty("Other_financial_support")
    private boolean otherFinancialSupport;
    @JsonProperty("Other_income_crm15")
    private String otherIncomeCrm15;
    @JsonProperty("Total_amount_received_every")
    private String totalAmountReceivedEvery;
    @JsonProperty("Partner_student_loan")
    private boolean partnerStudentLoan;
    @JsonProperty("Partner_family_rent")
    private boolean partnerFamilyRent;
    @JsonProperty("Partner_other_rent")
    private boolean partnerOtherRent;
    @JsonProperty("Partner_other_financial_support")
    private boolean partnerOtherFinancialSupport;
    @JsonProperty("Partner_other_income_crm15")
    private String partnerOtherIncomeCrm15;
    @JsonProperty("Partner_total_amount_received_every")
    private String partnerTotalAmountReceivedEvery;
    @JsonProperty("Prev_answers_no_other_income")
    private String prevAnswersNoOtherIncome;
    @JsonProperty("How_pay_bills_crm15")
    private String howPayBillsCrm15;
    @JsonProperty("Home_or_rent_payment")
    private String homeOrRentPayment;
    @JsonProperty("Mortgage_rent_paid_every")
    private String mortgageRentPaidEvery;
    @JsonProperty("Council_tax_paid_every")
    private String councilTaxPaidEvery;
    @JsonProperty("Board_and_lodgings")
    private short boardAndLodgings;
    @JsonProperty("Board_and_lodgings_paid_every")
    private short boardAndLodgingsPaidEvery;
    @JsonProperty("Food_bill")
    private short foodBill;
    @JsonProperty("Food_bill_every")
    private short foodBillEvery;
    @JsonProperty("Board_and_lodgings_landlord")
    private String boardAndLodgingsLandlord;
    @JsonProperty("Board_lodgings_landlord_relationship")
    private String boardLodgingsLandlordRelationship;
    @JsonProperty("Childcare_costs")
    private String childcareCosts;
    @JsonProperty("Childcare_costs_paid_every")
    private String childcareCostsPaidEvery;
    @JsonProperty("Pay_maintenance")
    private String payMaintenance;
    @JsonProperty("Maintenance_amount_paid_every")
    private String maintenanceAmountPaidEvery;
    @JsonProperty("Contribute_legal_aid")
    private String contributeLegalAid;
    @JsonProperty("Legal_aid_contribution_paid_every")
    private String legalAidContributionPaidEvery;
    @JsonProperty("Legal_aid_contribution_ref")
    private String legalAidContributionRef;
    @JsonProperty("Paid_40_percent_tax")
    private String paid40PercentTax;
    @JsonProperty("Partner_paid_40_percent_tax")
    private String partnerPaid40PercentTax;
    @JsonProperty("Indictable_offence")
    private short indictableOffence;
    @JsonProperty("Own_property")
    private String ownProperty;
    @JsonProperty("Partner_own_property")
    private String partnerOwnProperty;
    @JsonProperty("Salary_paid_into_account")
    private String salaryPaidIntoAccount;
    @JsonProperty("Partner_salary_paid_into_account")
    private String partnerSalaryPaidIntoAccount;
    @JsonProperty("Salary_account")
    private String salaryAccount;
    @JsonProperty("Partner_salary_account")
    private String partnerSalaryAccount;
    @JsonProperty("Premium_bonds")
    private String premiumBonds;
    @JsonProperty("Partner_premium_bonds")
    private String partnerPremiumBonds;
    @JsonProperty("National_savings_certificates")
    private String nationalSavingsCertificates;
    @JsonProperty("Partner_national_savings_certificates")
    private String partnerNationalSavingsCertificates;
    @JsonProperty("Trust_fund")
    private String trustFund;
    @JsonProperty("Partner_trust_fund")
    private String partnerTrustFund;
    @JsonProperty("Freezing_order_crm15")
    private String freezingOrderCrm15;
    @JsonProperty("Partner_freezing_order_crm15")
    private String partnerFreezingOrderCrm15;
    @JsonProperty("Own_car")
    private String ownCar;
    @JsonProperty("Total_file_size_bytes")
    private short totalFileSizeBytes;
    @JsonProperty("Total_file_size_mb")
    private short totalFileSizeMb;
    @JsonProperty("Additional_attachments")
    private boolean additionalAttachments;
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
    @JsonProperty("Gender")
    private String gender;
    @JsonProperty("Disabled")
    private String disabled;
    @JsonProperty("Disabled_definition")
    private String disabledDefinition;
    @JsonProperty("British")
    private boolean british;
    @JsonProperty("Irish")
    private boolean irish;
    @JsonProperty("White_other")
    private boolean whiteOther;
    @JsonProperty("White_and_black_caribbean")
    private boolean whiteAndBlackCaribbean;
    @JsonProperty("White_and_black_african")
    private boolean whiteAndBlackAfrican;
    @JsonProperty("White_and_asian")
    private boolean whiteAndAsian;
    @JsonProperty("Mixed_other")
    private boolean mixedOther;
    @JsonProperty("Indian")
    private boolean indian;
    @JsonProperty("Pakistani")
    private boolean pakistani;
    @JsonProperty("Bangladeshi")
    private boolean bangladeshi;
    @JsonProperty("Asian_other")
    private boolean asianOther;
    @JsonProperty("Black_caribbean")
    private boolean blackCaribbean;
    @JsonProperty("Black_african")
    private boolean blackAfrican;
    @JsonProperty("Black_other")
    private boolean blackOther;
    @JsonProperty("Chinese")
    private boolean chinese;
    @JsonProperty("Gypsy_or_traveller")
    private boolean gypsyOrTraveller;
    @JsonProperty("Other_ethnicity")
    private boolean otherEthnicity;
    @JsonProperty("Prefer_not_to_say_ethnicity")
    private boolean preferNotToSayEthnicity;
    @JsonProperty("Applicant_confirm_read")
    private boolean applicantConfirmRead;
    @JsonProperty("User_signed_name")
    private String userSignedName;
    @JsonProperty("User_signed_date")
    private LocalDateTime userSignedDate;
    @JsonProperty("Partner_confirm_read")
    private boolean partnerConfirmRead;
    @JsonProperty("Why_partner_no_sign")
    private String whyPartnerNoSign;
    @JsonProperty("Partner_sign_fullname")
    private String partnerSignFullName;
    @JsonProperty("Partner_sign_date")
    private LocalDateTime partnerSignDate;
    @JsonProperty("Provider_sign")
    private String providerSign;
    @JsonProperty("Charges_brought")
    private List<Charges> chargesBrought;
    @JsonProperty("Employment_details")
    private EmploymentDetails employmentDetails;
    @JsonProperty("Tblprocessedattachments")
    private List<TblProcessedAttachments> tblProcessedAttachments;


}
