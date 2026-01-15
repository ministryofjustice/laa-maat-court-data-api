package gov.uk.courtdata.iojappeal.validator;

import static gov.uk.courtdata.iojappeal.enums.ApiCreateIojAppealRequestValidatorFields.ERROR_APPEAL_REASON_IS_INVALID;
import static gov.uk.courtdata.iojappeal.enums.ApiCreateIojAppealRequestValidatorFields.ERROR_INCORRECT_COMBINATION;
import static org.assertj.core.api.Assertions.assertThat;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import uk.gov.justice.laa.crime.common.model.ioj.ApiCreateIojAppealRequest;
import uk.gov.justice.laa.crime.enums.IojAppealAssessor;
import uk.gov.justice.laa.crime.enums.NewWorkReason;

class ApiCreateIojAppealRequestValidatorTest {

    @Test
    void whenAppealAndMetaPresentButNotPopulated_thenAllMissingFieldValidationErrorsReturned() {
          ApiCreateIojAppealRequest request = TestModelDataBuilder.getApiCreateIojAppealRequest();
          request.getIojAppeal().setAppealSuccessful(null);
          request.getIojAppealMetadata().setLegacyApplicationId(null);
          request.getIojAppealMetadata().setApplicationReceivedDate(null);
          int expectedErrorCount = 2; // 1 field validation on appeal, 1 on metadata.
          List<String> returnedErrorList = ApiCreateIojAppealRequestValidator.validateRequest(request);
          assertThat(returnedErrorList).hasSize(expectedErrorCount);
          assertThat(returnedErrorList.stream()
              .filter(x -> x.contains(" is missing."))
              .count())
              .isEqualTo(expectedErrorCount);
    }

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
            IojAppealAssessor assessor, NewWorkReason reason, boolean isValidCombination) {var request = TestModelDataBuilder.getApiCreateIojAppealRequest();
        request.getIojAppeal().setAppealAssessor(assessor);
        request.getIojAppeal().setAppealReason(reason);

        List<String> returnedErrorList = ApiCreateIojAppealRequestValidator.validateRequest(request);
        if (isValidCombination) {
            assertThat(returnedErrorList).isEmpty();
        } else {
            assertThat(returnedErrorList).hasSize(1);
            assertThat(returnedErrorList.getFirst()).isEqualTo(ERROR_INCORRECT_COMBINATION.getName());
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

        List<String> returnedErrorList = ApiCreateIojAppealRequestValidator.validateRequest(request);
        assertThat(returnedErrorList).hasSize(1);
        assertThat(returnedErrorList.getFirst()).isEqualTo(ERROR_APPEAL_REASON_IS_INVALID.getName());
    }
}
