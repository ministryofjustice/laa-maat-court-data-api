package gov.uk.courtdata.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import gov.uk.courtdata.converter.AbstractEnumConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import jakarta.persistence.Converter;


@Getter
@AllArgsConstructor
public enum HardshipReviewDetailCode implements PersistableEnum<String> {

    UNSECURED_LOAN("UNSECURED LOAN", "Unsecured Loan"),
    SECURED_LOAN("SECURED LOAN", "Secured Loan"),
    CAR_LOAN("CAR LOAN", "Car Loan"),
    IVA("IVA", "IVA"),
    CARDS("CARDS", "Credit/Store Card Payment"),
    DEBTS("DEBTS", "Debts"),
    FINES("FINES", "Fines"),
    RENT_ARREARS("RENT ARREARS", "Rent Arrears"),
    BAILIFF("BAILIFF", "Bailiff Costs"),
    DWP_OVERPAYMENT("DWP OVERPAYMENT", "DWP Overpayment"),
    STUDENT_LOAN("STUDENT LOAN", "Student Loan"),
    ADD_MORTGAGE("ADD MORTGAGE", "Mortgage on additional Property"),
    UNI_HOUSING("UNI HOUSING", "University Housing Costs"),
    PRESCRIPTION("PRESCRIPTION", "Prescription Costs"),
    PENSION_PAY("PENSION PAY", "Pension Payments"),
    // Typo in the data that is already in the table
    MEDICAL_COSTS("MEDIAL COSTS", "Medical Costs"),
    OTHER("OTHER", "Other"),
    MEDICAL_GROUNDS("MEDICAL GROUNDS", "Medical Grounds"),
    SUSPENDED_WORK("SUSPENDED WORK", "Suspended from work"),
    OTHER_INC("OTHER INC", "Other");

    @JsonValue
    private final String code;
    private final String description;

    @Override
    public String getValue() {
        return this.code;
    }

    @Converter(autoApply = true)
    private static class HardshipReviewDetailCodeConverter extends AbstractEnumConverter<HardshipReviewDetailCode, String> {
        protected HardshipReviewDetailCodeConverter() {
            super(HardshipReviewDetailCode.class);
        }
    }
}
