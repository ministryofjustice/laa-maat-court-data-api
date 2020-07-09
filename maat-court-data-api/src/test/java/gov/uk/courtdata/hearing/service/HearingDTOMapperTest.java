package gov.uk.courtdata.hearing.service;

import gov.uk.courtdata.hearing.dto.*;
import gov.uk.courtdata.hearing.mapper.HearingDTOMapper;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.model.Session;
import gov.uk.courtdata.model.hearing.HearingResulted;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class HearingDTOMapperTest {

    private final HearingDTOMapper hearingDTOMapper = Mappers.getMapper(HearingDTOMapper.class);

    @Test
    public void givenDefendant_whenMapperIsInvoke_thenCompareData() {

        //given
        Defendant defendant = Defendant.builder().defendantId("1").surname("Smith").build();

        //when
        DefendantDTO defendantDTO = hearingDTOMapper.toDefendantDTO(defendant);

        //then
        assertThat(defendantDTO.getSurname()).isEqualTo("Smith");

    }

    @Test
    public void givenResult_whenMapperIsInvoke_thenCompareData() {

        //given
        Result result = Result.builder().resultCode("1").build();

        //when
        ResultDTO resultDTO = hearingDTOMapper.toResultDTO(result);

        //then
        assertThat(resultDTO.getResultCode()).isEqualTo(1);
    }

    @Test
    public void giventoSession_whenMapperIsInvoke_thenCompareData() {

        //given
        Session session = Session.builder().courtLocation("London").build();

        //when
        SessionDTO sessionDTO = hearingDTOMapper.toSessionDTO(session);

        //then
        assertThat(sessionDTO.getCourtLocation()).isEqualTo("London");
    }

    @Test
    public void giventoDefendant_whenMapperIsInvoke_thenCompareData() {

        //given
        Offence offence = Offence.builder().asnSeq("as12").build();

        //when
        OffenceDTO offenceDTO = hearingDTOMapper.toOffenceDTO(offence);

        //then
        assertThat(offenceDTO.getAsnSeq()).isEqualTo("as12");
    }

    @Test
    public void giventoHearing_whenMapperIsInvoke_thenCompareData() {

        //given
        HearingResulted hearingResulted = HearingResulted.builder().caseUrn("caseurl").build();
        Result result = Result.builder().resultCode("1").build();
        Offence offence = Offence.builder().asnSeq("as12").build();

        //when
        HearingDTO hearingDTO = hearingDTOMapper.toHearingDTO(hearingResulted, 12, 34, 56, offence, result);

        //then
        assertThat(hearingDTO.getOffence().getAsnSeq()).isEqualTo("as12");
        assertThat(hearingDTO.getResult().getResultCode()).isEqualTo(1);
    }
}
