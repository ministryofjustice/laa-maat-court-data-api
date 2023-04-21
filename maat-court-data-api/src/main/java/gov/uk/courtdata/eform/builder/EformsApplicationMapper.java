package gov.uk.courtdata.eform.builder;

import gov.uk.courtdata.dto.EformsStagingDTO;
import gov.uk.courtdata.eform.model.EformsApplication;

public interface EformsApplicationMapper {
    EformsStagingDTO map(EformsApplication eformsApplication);
}
