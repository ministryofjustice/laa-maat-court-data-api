package gov.uk.courtdata.hearing.service;

import gov.uk.courtdata.hearing.dto.DefendantDTO;
import gov.uk.courtdata.hearing.dto.ResultDTO;
import gov.uk.courtdata.hearing.dto.SessionDTO;
import gov.uk.courtdata.hearing.mapper.HearingDTOMapper;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.model.Session;
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
        Defendant defendant = Defendant.builder().postcode("LU1 111").build();

        //when
        DefendantDTO defendantDTO = hearingDTOMapper.toDefendantDTO(defendant);

        //then
        assertThat(defendantDTO.getPostcode()).isEqualTo("LU1 111");
    }

}
