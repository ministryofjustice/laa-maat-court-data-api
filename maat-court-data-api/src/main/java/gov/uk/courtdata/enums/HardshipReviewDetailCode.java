package gov.uk.courtdata.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum HardshipReviewDetailCode {
    UNSECURED_LOAN("UNSECURED LOAN", "Unsecured Loan"),
    SECURED_LOAN("SECURED LOAN", "Secured Loan"),
    CAR_LOAN("CAR LOAN","Car Loan"),
    IVA("IVA","IVA"),
    CARDS("CARDS","Credit/Store Card Payment"),
    DEBTS("DEBTS","Debts"),
    FINES("FINES","Fines"),
    RENT_ARREARS("RENT ARREARS","Rent Arrears"),
    BAILIFF("BAILIFF","Bailiff Costs"),
    DWP_OVERPAYMENT("DWP OVERPAYMENT","DWP Overpayment"),
    STUDENT_LOAN("STUDENT LOAN","Student Loan"),
    ADD_MORTGAGE("ADD MORTGAGE","Mortgage on additional Property"),
    UNI_HOUSING("UNI HOUSING","University Housing Costs"),
    PRESCRIPTION("PRESCRIPTION","Prescription Costs"),
    PENSION_PAY("PENSION PAY","Pension Payments"),
    // Please note this is a typo in the data that is already in the table
    MEDICAL_COSTS("MEDIAL COSTS","Medical Costs"),
    OTHER("OTHER","Other"),
    MEDICAL_GROUNDS("MEDICAL GROUNDS","Medical Grounds"),
    SUSPENDED_WORK("SUSPENDED WORK","Suspended from work"),
    OTHER_INC("OTHER INC","Other")
    ;

    private String code;
    private String description;

    @JsonValue
    public String getCode() {
        return code;
    }

}
