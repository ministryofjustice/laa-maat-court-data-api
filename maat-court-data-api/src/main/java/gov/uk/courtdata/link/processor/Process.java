package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.model.SaveAndLinkModel;

public interface Process {

      void process(SaveAndLinkModel saveAndLinkModel);
}
