package gov.uk.courtdata.dto.application;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.Date;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ApplicantDTO extends GenericDTO {
    private Long id;
    private String firstName;
    private String otherNames;
    private String surname;
    private Date dob;
    private String NiNumber;
    private String disabled;
    private Collection<DisabilityDTO> disabilities;

    private String email;
    private String mobileTelephone;
    private String workTelephone;
    private String homeTelephone;
    private String status;
    private String gender;
    private String foreignId;
    private Boolean useSupplierAddrForPost;
    private Long applicantHistoryId;
    private ApplicantPaymentDetailsDTO paymentDetailsDTO;
    private Date specialInvestigation;

    @Builder.Default
    private DisabilityStatementDTO disabilityStatementDTO = new DisabilityStatementDTO();
    @Builder.Default
    private EmploymentStatusDTO employmentStatusDTO = new EmploymentStatusDTO();
    @Builder.Default
    private EthnicityDTO ethnicity = new EthnicityDTO();
    @Builder.Default
    private AddressDTO homeAddressDTO = new AddressDTO();
    @Builder.Default
    private AddressDTO postalAddressDTO = new AddressDTO();
    @Builder.Default
    private Boolean noFixedAbode = false;
    @Builder.Default
    private Boolean useHomeAddress = false;
    @Builder.Default
    private Boolean hasPartner = false;
    @Builder.Default
    private Boolean contraryInterest = false;
}