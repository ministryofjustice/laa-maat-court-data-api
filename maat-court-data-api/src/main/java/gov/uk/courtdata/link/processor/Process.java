package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.LaaModelManager;

public interface Process {

      void process(LaaModelManager saveAndLinkModel);
}
