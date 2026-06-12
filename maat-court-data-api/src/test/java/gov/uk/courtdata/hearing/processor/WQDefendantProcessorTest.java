package gov.uk.courtdata.hearing.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.WQDefendant;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WQDefendantRepository;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WQDefendantProcessorTest {

    @InjectMocks
    private WQDefendantProcessor wqDefendantProcessor;

    @Mock
    private WQDefendantRepository defendantRepository;

    @Captor
    private ArgumentCaptor<WQDefendant> wqDefendantArgumentCaptor;

    @Test
    void givenDefendantProcessor_whenProcessIsInvoke_thenSaveDefendant() {
        // given
        HearingDTO hearingDTO = TestModelDataBuilder.getHearingDTO();

        // when
        wqDefendantProcessor.process(hearingDTO);

        // then
        verify(defendantRepository).save(wqDefendantArgumentCaptor.capture());
        assertThat(wqDefendantArgumentCaptor.getValue().getSurname()).isEqualTo("Smith");
        assertThat(wqDefendantArgumentCaptor.getValue().getPostCode()).isEqualTo("LU3 111");
    }

    @ParameterizedTest
    @MethodSource("telephoneWorkValues")
    void givenTelephoneWorkContainsAdditionalText_whenProcessIsInvoked_thenOnlyTelephoneNumberIsSaved(
            final String inputTelephoneWork, final String expectedTelephoneWork) {
        // given
        HearingDTO hearingDTO = TestModelDataBuilder.getHearingDTO();
        hearingDTO.getDefendant().setTelephoneWork(inputTelephoneWork);

        // when
        wqDefendantProcessor.process(hearingDTO);

        // then
        verify(defendantRepository).save(wqDefendantArgumentCaptor.capture());

        WQDefendant savedDefendant = wqDefendantArgumentCaptor.getValue();

        assertThat(savedDefendant.getTelephoneWork()).isEqualTo(expectedTelephoneWork);
    }

    @ParameterizedTest
    @MethodSource("telephoneHomeValues")
    void givenTelephoneHomeContainsAdditionalText_whenProcessIsInvoked_thenOnlyTelephoneNumberIsSaved(
            final String inputTelephoneHome, final String expectedTelephoneHome) {
        // given
        HearingDTO hearingDTO = TestModelDataBuilder.getHearingDTO();
        hearingDTO.getDefendant().setTelephoneHome(inputTelephoneHome);

        // when
        wqDefendantProcessor.process(hearingDTO);

        // then
        verify(defendantRepository).save(wqDefendantArgumentCaptor.capture());

        WQDefendant savedDefendant = wqDefendantArgumentCaptor.getValue();

        assertThat(savedDefendant.getTelephoneHome()).isEqualTo(expectedTelephoneHome);
    }

    @ParameterizedTest
    @MethodSource("telephoneMobileValues")
    void givenTelephoneMobileContainsAdditionalText_whenProcessIsInvoked_thenOnlyTelephoneNumberIsSaved(
            final String inputTelephoneMobile, final String expectedTelephoneMobile) {
        // given
        HearingDTO hearingDTO = TestModelDataBuilder.getHearingDTO();
        hearingDTO.getDefendant().setTelephoneMobile(inputTelephoneMobile);

        // when
        wqDefendantProcessor.process(hearingDTO);

        // then
        verify(defendantRepository).save(wqDefendantArgumentCaptor.capture());

        WQDefendant savedDefendant = wqDefendantArgumentCaptor.getValue();

        assertThat(savedDefendant.getTelephoneMobile()).isEqualTo(expectedTelephoneMobile);
    }

    @Test
    void givenAllDefendantTelephoneFieldsContainText_whenProcessIsInvoked_thenOnlyTelephoneNumbersAreSaved() {
        // given
        HearingDTO hearingDTO = TestModelDataBuilder.getHearingDTO();

        hearingDTO.getDefendant().setTelephoneHome("020 7946 0018 home");
        hearingDTO.getDefendant().setTelephoneWork("07700 900123 (work)");
        hearingDTO.getDefendant().setTelephoneMobile("+44 7700 900123 (mobile)");

        // when
        wqDefendantProcessor.process(hearingDTO);

        // then
        verify(defendantRepository).save(wqDefendantArgumentCaptor.capture());

        WQDefendant savedDefendant = wqDefendantArgumentCaptor.getValue();

        assertThat(savedDefendant.getTelephoneHome()).isEqualTo("02079460018");
        assertThat(savedDefendant.getTelephoneWork()).isEqualTo("07700900123");
        assertThat(savedDefendant.getTelephoneMobile()).isEqualTo("+447700900123");
    }

    @Test
    void givenInvalidTelephoneValues_whenProcessIsInvoked_thenDefendantIsStillSavedWithNullTelephoneNumbers() {
        // given
        HearingDTO hearingDTO = TestModelDataBuilder.getHearingDTO();

        hearingDTO.getDefendant().setTelephoneHome("home number unknown");
        hearingDTO.getDefendant().setTelephoneWork("after 6pm");
        hearingDTO.getDefendant().setTelephoneMobile("mum's mobile");

        // when
        wqDefendantProcessor.process(hearingDTO);

        // then
        verify(defendantRepository).save(wqDefendantArgumentCaptor.capture());

        WQDefendant savedDefendant = wqDefendantArgumentCaptor.getValue();

        assertThat(savedDefendant.getTelephoneHome()).isNull();
        assertThat(savedDefendant.getTelephoneWork()).isNull();
        assertThat(savedDefendant.getTelephoneMobile()).isNull();

        assertThat(savedDefendant.getSurname()).isEqualTo("Smith");
        assertThat(savedDefendant.getPostCode()).isEqualTo("LU3 111");
    }

    private static Stream<Arguments> telephoneWorkValues() {
        return Stream.of(
                arguments("07700900123", "07700900123"),
                arguments("07700 900123", "07700900123"),
                arguments("07700 900123 (mum's mobile)", "07700900123"),
                arguments("07700 900123 (mum 2)", "07700900123"),
                arguments("07700-900-123", "07700900123"),
                arguments("+44 7700 900123", "+447700900123"),
                arguments("+44 7700 900123 (mum's mobile)", "+447700900123"),
                arguments("+ 44 7700 900123", "+447700900123"),
                arguments("Call mum on 07700 900123 after 6pm", "07700900123"),
                arguments("after 6pm call 07700 900123", "07700900123"),
                arguments("mum's mobile", null),
                arguments("after 6pm", null),
                arguments("123456", null),
                arguments("", null),
                arguments("   ", null),
                arguments(null, null),

                // TELEPHONE_WORK is VARCHAR2(25 BYTE)
                arguments("1234567890123456789012345", "1234567890123456789012345"),
                arguments("12345678901234567890123456", null));
    }

    private static Stream<Arguments> telephoneHomeValues() {
        return Stream.of(
                arguments("020 7946 0018 home", "02079460018"),
                arguments("+44 20 7946 0018 home", "+442079460018"),

                // TELEPHONE_HOME is VARCHAR2(20 CHAR)
                arguments("12345678901234567890", "12345678901234567890"),
                arguments("123456789012345678901", null));
    }

    private static Stream<Arguments> telephoneMobileValues() {
        return Stream.of(
                arguments("07700 900123", "07700900123"),
                arguments("07700 900123 (mum's mobile)", "07700900123"),
                arguments("+44 7700 900123", "+447700900123"),
                arguments("+44 7700 900123 (mobile)", "+447700900123"),

                // TELEPHONE_MOBILE is VARCHAR2(20 CHAR)
                arguments("12345678901234567890", "12345678901234567890"),
                arguments("123456789012345678901", null));
    }
}
