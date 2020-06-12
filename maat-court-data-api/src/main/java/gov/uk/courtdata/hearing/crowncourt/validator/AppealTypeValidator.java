package gov.uk.courtdata.hearing.crowncourt.validator;

import gov.uk.courtdata.entity.AppealTypeEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.AppealTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AppealTypeValidator {

    private final AppealTypeRepository appealTypeRepository;

    public void validate(final HearingResulted hearingResulted) {

        final String appealType = hearingResulted.getCcOutComeData().getAppealType();
        if (appealType != null) {
            boolean isValidAppealType = appealTypeRepository
                    .findAll()
                    .stream()
                    .map(AppealTypeEntity::getCode)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()).contains(appealType);

            if (!isValidAppealType) {
                throw new MAATCourtDataException("Invalid Appeal Type : "
                        .concat(appealType.concat(" is passed in for MAAT ID: " + hearingResulted.getMaatId())));
            }
        }
    }
}
