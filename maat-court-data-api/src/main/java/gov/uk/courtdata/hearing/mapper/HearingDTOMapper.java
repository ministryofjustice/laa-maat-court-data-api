package gov.uk.courtdata.hearing.mapper;

import gov.uk.courtdata.hearing.dto.*;
import gov.uk.courtdata.model.*;
import gov.uk.courtdata.model.hearing.HearingResulted;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <code>HearingDTOMapper/code> to map from hearing resulted to mags court dto and
 * vice versa.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface HearingDTOMapper {
    /***
     *  Map from hearing resulted to hearing DTO.
     * @param hearingResulted
     * @return
     */
    HearingDTO toHearingDTO(final HearingResulted hearingResulted,
                            final Integer caseId,
                            final Integer proceedingId,
                            final Integer txId,
                            final Offence offence,
                            final Result result,
                            final Plea plea,
                            final Verdict verdict
    );

    DefendantDTO toDefendantDTO(final Defendant defendant);

    SessionDTO toSessionDTO(final Session session);

    OffenceDTO toOffenceDTO(final Offence offence);

    ResultDTO toResultDTO(final Result result);

    PleaDTO toPleaDTO (final Plea plea);
    VerdictDTO toVerdictDTO (final Verdict verdict);

}