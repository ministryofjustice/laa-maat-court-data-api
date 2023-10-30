package gov.uk.courtdata.laastatus.processor;

import gov.uk.courtdata.link.processor.DefendantInfoProcessor;
import gov.uk.courtdata.repository.DefendantRepository;
import org.springframework.stereotype.Component;

import static gov.uk.courtdata.constants.CourtDataConstants.SAVE_MLR;

@Component
public class UpdateDefendantInfoProcessor extends DefendantInfoProcessor {

    public UpdateDefendantInfoProcessor(DefendantRepository defendantRepository) {
        super(defendantRepository);
    }

    @Override
    protected String getDataSource() {
        return SAVE_MLR;
    }
}
