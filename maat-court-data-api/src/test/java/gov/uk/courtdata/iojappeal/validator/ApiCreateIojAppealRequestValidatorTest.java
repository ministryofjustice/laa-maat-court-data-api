package gov.uk.courtdata.iojappeal.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static gov.uk.courtdata.iojappeal.validator.ApiCreateIojAppealRequestValidator.APPEAL_ASSESSOR;
import static gov.uk.courtdata.iojappeal.validator.ApiCreateIojAppealRequestValidator.ERROR_APPEAL_REASON_IS_INVALID;
import static gov.uk.courtdata.iojappeal.validator.ApiCreateIojAppealRequestValidator.ERROR_FIELD_IS_MISSING;
import static gov.uk.courtdata.iojappeal.validator.ApiCreateIojAppealRequestValidator.ERROR_INCORRECT_COMBINATION;
import static gov.uk.courtdata.iojappeal.validator.ApiCreateIojAppealRequestValidator.LEGACY_APPLICATION_ID;
import static gov.uk.courtdata.iojappeal.validator.ApiCreateIojAppealRequestValidator.SESSION_ID;
import static gov.uk.courtdata.iojappeal.validator.ApiCreateIojAppealRequestValidator.USERNAME;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import uk.gov.justice.laa.crime.common.model.ioj.ApiCreateIojAppealRequest;
import uk.gov.justice.laa.crime.common.model.ioj.IojAppeal;
import uk.gov.justice.laa.crime.common.model.ioj.IojAppealMetadata;
import uk.gov.justice.laa.crime.enums.IojAppealAssessor;
import uk.gov.justice.laa.crime.enums.NewWorkReason;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ApiCreateIojAppealRequestValidatorTest {
  // Null check validation.

  @Test
  void whenRequestEmpty_thenTwoErrors() {
    List<String> returnedErrorList =
        ApiCreateIojAppealRequestValidator.validateRequest(new ApiCreateIojAppealRequest());
    assertThat(returnedErrorList).hasSize(2);
    assertThat(returnedErrorList.stream()
        .filter(x -> x.contains(" is missing."))
        .count())
        .isEqualTo(2);
  }

  @Test
  void whenAppealAndMetaPresentButNotPopulated_thenOnlyMissingErrors() {
    ApiCreateIojAppealRequest request = new ApiCreateIojAppealRequest();
    request.setIojAppeal(new IojAppeal());
    request.setIojAppealMetadata(new IojAppealMetadata());
    int expectedErrorCount = 10;
    List<String> returnedErrorList = ApiCreateIojAppealRequestValidator.validateRequest(request);
    assertThat(returnedErrorList).hasSize(expectedErrorCount); // 6 field validations on appeal, 4 on metadata.
    assertThat(returnedErrorList.stream()
        .filter(x -> x.contains(" is missing."))
        .count())
        .isEqualTo(expectedErrorCount);
    assertThat(returnedErrorList.stream()
        .filter(x -> x.equals(getExpectedMissingError(LEGACY_APPLICATION_ID)))
        .count())
        .isEqualTo(1);
  }

  // Appeal Reason/Assessor Combination Tests
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
    var request = TestModelDataBuilder.getApiCreateIojAppealRequest();
    request.getIojAppeal().setAppealAssessor(assessor);
    request.getIojAppeal().setAppealReason(reason);

    List<String> returnedErrorList = ApiCreateIojAppealRequestValidator.validateRequest(request);
    if (isValidCombination) {
      assertThat(returnedErrorList).isEmpty();
    } else {
      assertThat(returnedErrorList).hasSize(1);
      assertThat(returnedErrorList.getFirst()).isEqualTo(ERROR_INCORRECT_COMBINATION);
    }
  }

  @ParameterizedTest
  @EnumSource(
      value = NewWorkReason.class,
      names = {"PRI", "NEW", "JR"},
      mode = EnumSource.Mode.EXCLUDE)
  void whenInvalidAppealReasonSelected_thenOneError(NewWorkReason reason) {
    // Judicial Review + Judge
    var request = TestModelDataBuilder.getApiCreateIojAppealRequest();
    request.getIojAppeal().setAppealReason(reason);

    List<String> returnedErrorList = ApiCreateIojAppealRequestValidator.validateRequest(request);
    assertThat(returnedErrorList).hasSize(1);
    assertThat(returnedErrorList.getFirst()).isEqualTo(ERROR_APPEAL_REASON_IS_INVALID);
  }

  @Test
  void whenValidButNoAssessor_thenOnlyMissingErrors() {
    var request = TestModelDataBuilder.getApiCreateIojAppealRequest();
    request.getIojAppeal().setAppealAssessor(null);
    List<String> returnedErrorList = ApiCreateIojAppealRequestValidator.validateRequest(request);
    assertThat(returnedErrorList).hasSize(1);
    assertThat(returnedErrorList.getFirst()).isEqualTo(getExpectedMissingError(APPEAL_ASSESSOR));
  }

  @ParameterizedTest
  @NullAndEmptySource // additional check to ensure validation works on empty strings and nulls
  void whenValidButNoUserSessionDetails_thenOnlyMissingErrors(String emptyOrNull) {
    var request = TestModelDataBuilder.getApiCreateIojAppealRequest();
    request.getIojAppealMetadata().getUserSession().setUserName(emptyOrNull);
    request.getIojAppealMetadata().getUserSession().setSessionId(emptyOrNull);

    List<String> returnedErrorList = ApiCreateIojAppealRequestValidator.validateRequest(request);
    assertThat(returnedErrorList)
        .hasSize(2)
        .containsExactlyInAnyOrder(getExpectedMissingError(SESSION_ID), getExpectedMissingError(USERNAME));
  }

  // helpers

  private String getExpectedMissingError(String fieldName) {
    return String.format(ERROR_FIELD_IS_MISSING, fieldName);
  }
}
