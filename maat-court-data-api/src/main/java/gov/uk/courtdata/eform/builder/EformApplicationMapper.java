package gov.uk.courtdata.eform.builder;

import gov.uk.courtdata.eform.dto.EformStagingDTO;
import gov.uk.courtdata.eform.model.EformApplication;

public interface EformApplicationMapper {
    EformStagingDTO map(EformApplication eformApplication);
}
