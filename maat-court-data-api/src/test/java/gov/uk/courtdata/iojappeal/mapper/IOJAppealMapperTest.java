package gov.uk.courtdata.iojappeal.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.enums.IojAppealAssessor;

class IOJAppealMapperTest {
    IOJAppealMapper iojAppealMapper = new IOJAppealMapperImpl();

    @Test
    void givenApiCreateIojAppealRequest_whenMapToIojAppealEntity_thenAllFieldsMapped() {
        var request = TestModelDataBuilder.getApiCreateIojAppealRequest();
        var entity = iojAppealMapper.toIojAppealEntity(request);

        assertThat(entity)
            .isNotNull()
            .hasNoNullFieldsOrPropertiesExcept(
                "id", "dateCreated", "incomplete", "dateModified", "userModified");
        assertThat(entity.getAppealSetupResult())
            .isEqualTo("GRANT");
        assertThat(entity.getNotes()).isEqualTo(request.getIojAppeal().getNotes());
        assertThat(entity.getIapsStatus()).isEqualTo("COMPLETE");
        assertThat(entity.getDecisionDate().toLocalDate()).isEqualTo(request.getIojAppeal().getDecisionDate());
        assertThat(entity.getIderCode()).isEqualTo(request.getIojAppeal().getDecisionReason().getCode());
        assertThat(entity.getUserCreated()).isEqualTo(request.getIojAppealMetadata().getUserSession().getUserName());
        assertThat(entity.getCmuId()).isEqualTo(request.getIojAppealMetadata().getCaseManagementUnitId());
        assertThat(entity.getAppealSetupDate().toLocalDate()).isEqualTo(request.getIojAppeal().getReceivedDate());
        assertThat(entity.getDecisionResult()).isEqualTo("PASS");
        assertThat(entity.getNworCode()).isEqualTo(request.getIojAppeal().getAppealReason().getCode());
        assertThat(entity.getRepOrder().getId()).isEqualTo(request.getIojAppealMetadata().getLegacyApplicationId());
    }

    @Test
    void givenIojAppealEntity_whenMapToApiCreateIojAppealResponse_thenAllFieldsMapped() {
        var entity = TestEntityDataBuilder.getIOJAppealEntity();
        var response = iojAppealMapper.toApiCreateIojAppealResponse(entity);

        assertThat(response).isNotNull().hasNoNullFieldsOrPropertiesExcept("appealId");
        assertThat(response.getLegacyAppealId()).isEqualTo(entity.getId());
    }

    @Test
    void givenIojAppealEntity_whenMapToApiGetIojAppealResponse_thenAllFieldsMapped() {
        var entity = TestEntityDataBuilder.getIOJAppealEntity();
        var response = iojAppealMapper.toApiGetIojAppealResponse(entity);

        assertThat(response).isNotNull().hasNoNullFieldsOrPropertiesExcept("appealId");

        assertThat(response.getLegacyAppealId()).isEqualTo(entity.getId());
        assertThat(response.getAppealReason().getCode()).isEqualTo(entity.getNworCode());
        assertThat(response.getDecisionDate()).isEqualTo(entity.getDecisionDate().toLocalDate());
        assertThat(response.getReceivedDate()).isEqualTo(entity.getAppealSetupDate().toLocalDate());
        assertThat(response.getCaseManagementUnitId()).isEqualTo(entity.getCmuId());
        assertThat(response.getAppealAssessor()).isEqualTo(IojAppealAssessor.CASEWORKER);
        assertThat(response.getAppealSuccessful()).isTrue();
        assertThat(response.getDecisionReason().getCode()).isEqualTo(entity.getIderCode());
        assertThat(response.getNotes()).isEqualTo(entity.getNotes());
    }
}
