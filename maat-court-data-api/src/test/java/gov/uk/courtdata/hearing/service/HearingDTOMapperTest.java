package gov.uk.courtdata.hearing.service;

import gov.uk.courtdata.hearing.dto.*;
import gov.uk.courtdata.hearing.mapper.HearingDTOMapper;
import gov.uk.courtdata.model.*;
import gov.uk.courtdata.model.hearing.HearingResulted;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
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
        Plea plea = Plea.builder().offenceId("off1").build();
        Verdict verdict = Verdict.builder().category("Cat").build();


        //when
        HearingDTO hearingDTO = hearingDTOMapper.toHearingDTO(hearingResulted, 12, 34, 56, offence, result);

        //then
//        assertThat(hearingDTO.getOffence().getAsnSeq()).isEqualTo("as12");
        assertThat(hearingDTO.getResult().getResultCode()).isEqualTo(1);
    }

    @Test
    public void givenToPlea_whenMapperIsInvoke_thenCompareData() {

        //given
        HearingResulted hearingResulted = HearingResulted.builder().caseUrn("caseurl").build();
        Result result = Result.builder().resultCode("1").build();
        Plea plea = Plea
                .builder()
                .pleaValue("NOT_GUILTY")
                .offenceId("8072")
                .pleaDate("2018-10-25")
                .build();

        Offence offence = Offence
                .builder()
                .asnSeq("as12")
                .plea(plea)
                .build();

        //when
        HearingDTO hearingDTO = hearingDTOMapper.toHearingDTO(hearingResulted, 12, 34, 56, offence, result);

        //then
        assertThat(hearingDTO.getOffence().getPlea()).isNotNull();
        assertThat(hearingDTO.getOffence().getPlea().getPleaValue()).isEqualTo("NOT_GUILTY");
        assertThat(hearingDTO.getOffence().getPlea().getOffenceId()).isEqualTo("8072");
        assertThat(hearingDTO.getOffence().getPlea().getPleaDate()).isEqualTo("2018-10-25");

    }

    @Test
    public void givenToNullPlea_whenMapperIsInvoke_thenCompareData() {

        //given
        HearingResulted hearingResulted = HearingResulted.builder().caseUrn("caseurl").build();
        Result result = Result.builder().resultCode("1").build();

        Offence offence = Offence
                .builder()
                .asnSeq("as12")
                .plea(null)
                .build();

        //when
        HearingDTO hearingDTO = hearingDTOMapper.toHearingDTO(hearingResulted, 12, 34, 56, offence, result);

        //then
        assertThat(hearingDTO.getOffence().getPlea()).isNull();
    }

    @Test
    public void givenToVerdict_whenMapperIsInvoke_thenCompareData() {

        //given
        HearingResulted hearingResulted = HearingResulted.builder().caseUrn("caseurl").build();
        Result result = Result.builder().resultCode("1").build();
        Verdict verdict = Verdict
                .builder()
                .offenceId("12345")
                .verdictDate("2018-12-25")
                .category("Verdict Category")
                .categoryType("GUILTY")
                .cjsVerdictCode("CD2343")
                .verdictCode("MK1212")
                .build();

        Offence offence = Offence
                .builder()
                .asnSeq("as12")
                .verdict(verdict)
                .build();

        //when
        HearingDTO hearingDTO = hearingDTOMapper.toHearingDTO(hearingResulted, 12, 34, 56, offence, result);

        //then
        assertThat(hearingDTO.getOffence().getVerdict()).isNotNull();
        assertThat(hearingDTO.getOffence().getVerdict().getOffenceId()).isEqualTo("12345");
        assertThat(hearingDTO.getOffence().getVerdict().getVerdictDate()).isEqualTo("2018-12-25");
        assertThat(hearingDTO.getOffence().getVerdict().getCategory()).isEqualTo("Verdict Category");
        assertThat(hearingDTO.getOffence().getVerdict().getCategoryType()).isEqualTo("GUILTY");
        assertThat(hearingDTO.getOffence().getVerdict().getCjsVerdictCode()).isEqualTo("CD2343");
        assertThat(hearingDTO.getOffence().getVerdict().getVerdictCode()).isEqualTo("MK1212");
    }

    @Test
    public void givenToNullVerdict_whenMapperIsInvoke_thenCompareData() {

        //given
        HearingResulted hearingResulted = HearingResulted.builder().caseUrn("caseurl").build();
        Result result = Result.builder().resultCode("1").build();
        Offence offence = Offence
                .builder()
                .asnSeq("as12")
                .verdict(null)
                .build();

        //when
        HearingDTO hearingDTO = hearingDTOMapper.toHearingDTO(hearingResulted, 12, 34, 56, offence, result);

        //then
        assertThat(hearingDTO.getOffence().getVerdict()).isNull();
    }
}
