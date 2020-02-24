package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.CreateLinkDto;

public interface Process {

      void process(CreateLinkDto saveAndLinkModel);
}
