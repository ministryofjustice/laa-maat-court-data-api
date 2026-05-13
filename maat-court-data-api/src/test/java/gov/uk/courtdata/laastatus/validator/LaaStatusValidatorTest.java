package gov.uk.courtdata.laastatus.validator;

import static gov.uk.courtdata.enums.IOJDecision.FAIL;
import static gov.uk.courtdata.enums.IOJDecision.NOT_APPLICABLE;
import static gov.uk.courtdata.enums.IOJDecision.PASS;
import static gov.uk.courtdata.enums.LAAStatus.FAILED_BOTH_IOJ_AND_MEANS;
import static gov.uk.courtdata.enums.LAAStatus.FAILED_ON_IOJ;
import static gov.uk.courtdata.enums.LAAStatus.GRANTED;
import static gov.uk.courtdata.enums.LAAStatus.GRANTED_FOR_TWO_ADVOCATES;
import static gov.uk.courtdata.enums.LAAStatus.GRANTED_WITH_QC;
import static org.assertj.core.api.Assertions.assertThat;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.enums.IOJDecision;
import gov.uk.courtdata.enums.LAAStatus;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.MessageCollection;
import gov.uk.courtdata.model.Offence;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LaaStatusValidatorTest {

    private LaaStatusValidator laaStatusValidator;
    private CaseDetails caseDetails;

    @BeforeEach
    void build() {
        laaStatusValidator = new LaaStatusValidator();
        caseDetails = TestModelDataBuilder.getCaseDetails(TestModelDataBuilder.REP_ID);
    }

    private static Stream<Arguments> validationFailureData() {
        return Stream.of(
                Arguments.of(FAIL, GRANTED, "Cannot Grant Legal Aid on a Failed or Pending IOJ - See offence 001"),
                Arguments.of(
                        PASS, FAILED_BOTH_IOJ_AND_MEANS, "Cannot Pass IOJ and Fail Legal Aid on IOJ - See offence 001"),
                Arguments.of(NOT_APPLICABLE, GRANTED_WITH_QC, "Cannot Grant Legal Aid on a n/a IOJ - See offence 001"));
    }

    @MethodSource(value = "validationFailureData")
    @ParameterizedTest
    void givenInvalidStates_whenLaaStatusValidatorIsInvoked_thenValidationFailed(
            IOJDecision iojDecision, LAAStatus status, String expectedErrorMessage) {
        // given
        Offence offence = caseDetails.getDefendant().getOffences().getFirst();
        offence.setIojDecision(iojDecision.value());
        offence.setLegalAidStatus(status.getValue());

        // when
        MessageCollection messageCollection = laaStatusValidator.validate(caseDetails);

        // then
        assertThat(messageCollection.getMessages().getFirst()).isEqualTo(expectedErrorMessage);
    }

    private static Stream<Arguments> validationPassData() {
        return Stream.of(
                Arguments.of(PASS, GRANTED_FOR_TWO_ADVOCATES),
                Arguments.of(FAIL, FAILED_ON_IOJ),
                Arguments.of(NOT_APPLICABLE, FAILED_BOTH_IOJ_AND_MEANS));
    }

    @MethodSource(value = "validationPassData")
    @ParameterizedTest
    void givenValidStates_whenLaaStatusValidatorIsInvoked_thenValidationPassed(IOJDecision decision, LAAStatus status) {
        // given
        Offence offence = caseDetails.getDefendant().getOffences().getFirst();
        offence.setIojDecision(decision.value());
        offence.setLegalAidStatus(status.name());

        // when
        MessageCollection messageCollection = laaStatusValidator.validate(caseDetails);

        // then
        assertThat(messageCollection.getMessages()).isEmpty();
    }
}
