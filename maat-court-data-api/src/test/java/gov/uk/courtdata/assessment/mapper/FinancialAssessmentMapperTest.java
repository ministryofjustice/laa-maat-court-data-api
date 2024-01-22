package gov.uk.courtdata.assessment.mapper;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.dto.IOJAssessorDetails;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FinancialAssessmentMapperTest {

    private final FinancialAssessmentMapper financialAssessmentMapper = Mappers.getMapper(FinancialAssessmentMapper.class);

    @Test
    void shouldSuccessfullyCreateIOJAssessorDetails_fromFinancialAssessmentEntity() {
        FinancialAssessmentEntity financialAssessmentEntity = TestEntityDataBuilder.getFinancialAssessmentEntity();
        final String userName = financialAssessmentEntity.getUserCreated();
        UserEntity userEntity = TestEntityDataBuilder.getUserEntity(userName);
        financialAssessmentEntity.setUserCreatedEntity(userEntity);

        IOJAssessorDetails iojAssessorDetails = financialAssessmentMapper.createIOJAssessorDetails(financialAssessmentEntity);

        assertEquals("Karen Greaves", iojAssessorDetails.getFullName());
        assertEquals(userName, iojAssessorDetails.getUserName());
    }
}