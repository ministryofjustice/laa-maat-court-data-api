package gov.uk.courtdata.laaStatus.processor;

import gov.uk.courtdata.link.processor.OffenceInfoProcessor;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.repository.OffenceRepository;
import gov.uk.courtdata.util.CourtDataUtil;
import org.springframework.stereotype.Component;

@Component
public class UpdateOffenceInfoProcessor extends OffenceInfoProcessor {

    public UpdateOffenceInfoProcessor(CourtDataUtil courtDataUtil, OffenceRepository offenceRepository) {
        super(courtDataUtil, offenceRepository);
    }

    @Override
    protected Integer getWQOffence(Offence offence) {
        return offence.getWqOffence();
    }
    @Override
    protected Integer getIojDecision(Offence offence) {
        return offence.getIojDecision();
    }
}
