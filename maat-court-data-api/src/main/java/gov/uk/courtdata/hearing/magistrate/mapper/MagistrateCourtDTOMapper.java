package gov.uk.courtdata.hearing.magistrate.mapper;

import gov.uk.courtdata.hearing.magistrate.dto.*;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.model.hearing.Defendant;
import gov.uk.courtdata.model.hearing.Offence;
import gov.uk.courtdata.model.hearing.Result;
import gov.uk.courtdata.model.hearing.Session;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <code>HearingResultedMagsCourtDTOMapper/code> to map from hearing resulted to mags court dto and
 * vice versa.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface MagistrateCourtDTOMapper {

    /***
     *  Map from hearing resulted to mags court dto
     * @param hearingResulted
     * @return
     */
    MagistrateCourtDTO toMagsCourtDTO(final HearingResulted hearingResulted,
                                      final Integer caseId,
                                      final Integer proceedingId,
                                      final Integer txId,
                                      final Offence offence,
                                      final Result result);

    DefendantDTO toDefendantDTO(final Defendant defendant);

    SessionDTO toSessionDTO(final Session session);

    OffenceDTO toOffenceDTO(final Offence offence);

    ResultDTO toResultDTO(final Result result);


}
