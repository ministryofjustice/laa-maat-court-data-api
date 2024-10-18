package gov.uk.courtdata.assessment.mapper;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.dto.AssessorDetails;
import gov.uk.courtdata.entity.FinAssIncomeEvidenceEntity;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FinancialAssessmentMapperTest {

    private final FinancialAssessmentMapper financialAssessmentMapper = Mappers.getMapper(FinancialAssessmentMapper.class);

    @Test
    void shouldSuccessfullyCreateMeansAssessorDetails_fromFinancialAssessmentEntity() {
        FinancialAssessmentEntity financialAssessmentEntity = TestEntityDataBuilder.getFinancialAssessmentEntity();
        final String userName = financialAssessmentEntity.getUserCreated();
        UserEntity userEntity = TestEntityDataBuilder.getUserEntity();
        financialAssessmentEntity.setUserCreatedEntity(userEntity);

        AssessorDetails meansAssessorDetails = financialAssessmentMapper.createMeansAssessorDetails(financialAssessmentEntity);

        assertEquals("Karen Greaves", meansAssessorDetails.getFullName());
        assertEquals(userName, meansAssessorDetails.getUserName());
    }

    @Test
    void shouldRetainMandatoryField() {
        FinAssIncomeEvidenceEntity finAssIncomeEvidenceEntity =
                financialAssessmentMapper.finAssIncomeEvidenceDTOToFinAssIncomeEvidenceEntity(TestEntityDataBuilder.getFinAssIncomeEvidenceDTO());
        assertThat(finAssIncomeEvidenceEntity.getMandatory()).isNull();
    }
}