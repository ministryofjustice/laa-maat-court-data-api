package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.CourtDataDTO;

public interface Process {

      void process(CourtDataDTO saveAndLinkModel);
}
