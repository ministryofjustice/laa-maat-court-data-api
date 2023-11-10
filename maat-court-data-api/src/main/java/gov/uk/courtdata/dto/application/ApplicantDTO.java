package gov.uk.courtdata.dto.application;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ApplicantDTO extends GenericDTO
{
	private SysGenLong                          id;
	private String 								firstName;
	private String 								otherNames;
	private String 								surname;
	private Date 								dob;
	private String 								NiNumber;
	private	String								disabled;
	private Collection<DisabilityDTO>			disabilities;

	private DisabilityStatementDTO				disabilityStatementDTO;

	private EmploymentStatusDTO					employmentStatusDTO;
	private String 								email;
	private String 								mobileTelephone;
	private String 								workTelephone;
	private String 								homeTelephone;
	private String								status;
	private EthnicityDTO						ethnicity;
	private String 								gender;
	private AddressDTO 							homeAddressDTO;
	private AddressDTO 							postalAddressDTO;
    private Boolean 							noFixedAbode;
	private Boolean								useHomeAddress;
	private String								foreignId;	
	private Boolean								useSupplierAddrForPost;
	private Long                                applicantHistoryId;
	private ApplicantPaymentDetailsDTO			paymentDetailsDTO;
    private Boolean 							hasPartner;
    private Boolean 							contraryInterest;
    private Date								specialInvestigation;

}