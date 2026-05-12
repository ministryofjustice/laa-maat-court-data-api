package gov.uk.courtdata.assessment.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.dto.AssessorDetails;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.UserEntity;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class FinancialAssessmentMapperTest {

    private final FinancialAssessmentMapper financialAssessmentMapper =
            Mappers.getMapper(FinancialAssessmentMapper.class);

    @Test
    void shouldSuccessfullyCreateMeansAssessorDetails_fromFinancialAssessmentEntity() {
        FinancialAssessmentEntity financialAssessmentEntity = TestEntityDataBuilder.getFinancialAssessmentEntity();
        final String userName = financialAssessmentEntity.getUserCreated();
        UserEntity userEntity = TestEntityDataBuilder.getUserEntity();
        financialAssessmentEntity.setUserCreatedEntity(userEntity);

        AssessorDetails meansAssessorDetails =
                financialAssessmentMapper.createMeansAssessorDetails(financialAssessmentEntity);

        assertThat(meansAssessorDetails.getFullName()).isEqualTo("Karen Greaves");
        assertThat(meansAssessorDetails.getUserName()).isEqualTo(userName);
    }
}
