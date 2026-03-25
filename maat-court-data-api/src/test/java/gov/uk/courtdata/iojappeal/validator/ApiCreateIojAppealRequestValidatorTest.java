package gov.uk.courtdata.iojappeal.validator;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import uk.gov.justice.laa.crime.common.model.ioj.ApiCreateIojAppealRequest;
import uk.gov.justice.laa.crime.enums.IojAppealAssessor;
import uk.gov.justice.laa.crime.enums.NewWorkReason;
import uk.gov.justice.laa.crime.error.ErrorMessage;

import java.util.List;
import java.util.stream.Stream;

import static gov.uk.courtdata.iojappeal.enums.ApiCreateIojAppealRequestValidatorFields.ERROR_APPEAL_REASON_IS_INVALID;
import static gov.uk.courtdata.iojappeal.enums.ApiCreateIojAppealRequestValidatorFields.ERROR_INCORRECT_COMBINATION;
import static org.assertj.core.api.Assertions.assertThat;

class ApiCreateIojAppealRequestValidatorTest {

    private static Stream<Arguments> reasonAssessorCombinations() {
          return Stream.of(
              Arguments.of(IojAppealAssessor.CASEWORKER, NewWorkReason.JR, false),
              Arguments.of(IojAppealAssessor.CASEWORKER, NewWorkReason.NEW, true),
              Arguments.of(IojAppealAssessor.CASEWORKER, NewWorkReason.PRI, true),
              Arguments.of(IojAppealAssessor.JUDGE, NewWorkReason.JR, true),
              Arguments.of(IojAppealAssessor.JUDGE, NewWorkReason.NEW, false),
              Arguments.of(IojAppealAssessor.JUDGE, NewWorkReason.PRI, true));
    }

    @ParameterizedTest
    @MethodSource("reasonAssessorCombinations")
    void whenIojAssessorAndReasonCombinationIsTested_thenErrorShouldSurfaceIfInvalidCombination(
            IojAppealAssessor assessor, NewWorkReason reason, boolean isValidCombination) {

        ApiCreateIojAppealRequest request = TestModelDataBuilder.getApiCreateIojAppealRequest();

        request.getIojAppeal().setAppealReason(reason);
        request.getIojAppeal().setAppealAssessor(assessor);

        List<ErrorMessage> returnedErrorList = ApiCreateIojAppealRequestValidator.validateRequest(request);

        if (isValidCombination) {
            assertThat(returnedErrorList).isEmpty();
        } else {
            assertThat(returnedErrorList).hasSize(1);
            assertThat(returnedErrorList.getFirst().message()).isEqualTo(ERROR_INCORRECT_COMBINATION.getName());
        }
    }

    @ParameterizedTest
    @EnumSource(
        value = NewWorkReason.class,
        names = {"PRI", "NEW", "JR"},
        mode = EnumSource.Mode.EXCLUDE)
    void whenInvalidAppealReasonSelected_thenReturnsCorrectError(NewWorkReason reason) {
        var request = TestModelDataBuilder.getApiCreateIojAppealRequest();
        request.getIojAppeal().setAppealReason(reason);

        List<ErrorMessage> returnedErrorList = ApiCreateIojAppealRequestValidator.validateRequest(request);
        assertThat(returnedErrorList).hasSize(1);
        assertThat(returnedErrorList.getFirst().message()).isEqualTo(ERROR_APPEAL_REASON_IS_INVALID.getName());
    }
}
