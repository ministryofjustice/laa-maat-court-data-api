package gov.uk.courtdata.laaStatus.processor;

import gov.uk.courtdata.link.processor.DefendantInfoProcessor;
import gov.uk.courtdata.repository.DefendantRepository;
import gov.uk.courtdata.util.CourtDataUtil;
import org.springframework.stereotype.Component;

import static gov.uk.courtdata.constants.CourtDataConstants.SAVE_MLR;

@Component
public class UpdateDefendantInfoProcessor extends DefendantInfoProcessor {

    public UpdateDefendantInfoProcessor(DefendantRepository defendantRepository, CourtDataUtil courtDataUtil) {
        super(defendantRepository, courtDataUtil);
    }

    @Override
    protected String getDataSource() {
        return SAVE_MLR;
    }
}
