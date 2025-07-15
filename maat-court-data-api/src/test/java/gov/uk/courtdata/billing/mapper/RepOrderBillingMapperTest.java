package gov.uk.courtdata.billing.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.uk.courtdata.billing.dto.RepOrderBillingDTO;
import gov.uk.courtdata.billing.entity.RepOrderBillingEntity;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.enums.CrownCourtCaseType;
import gov.uk.courtdata.enums.CrownCourtTrialOutcome;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.enums.AppealType;
import uk.gov.justice.laa.crime.enums.EvidenceFeeLevel;
import uk.gov.justice.laa.crime.enums.MagCourtOutcome;

@ExtendWith(MockitoExtension.class)
class RepOrderBillingMapperTest {

    @Test
    void givenARepOrderEntity_whenMapEntityToDtoIsInvoked_thenDtoIsReturned() {
        RepOrderBillingDTO expectedRepOrder = RepOrderBillingDTO.builder()
            .id(123)
            .applicantId(123)
            .arrestSummonsNo("ARREST-5678")
            .evidenceFeeLevel(EvidenceFeeLevel.LEVEL1.getFeeLevel())
            .supplierAccountCode("AB123C")
            .magsCourtId(34)
            .magsCourtOutcome(MagCourtOutcome.COMMITTED.getOutcome())
            .dateReceived(LocalDate.of(2025, 6, 10))
            .crownCourtRepOrderDate(LocalDate.of(2025, 6, 12))
            .offenceType("BURGLARY")
            .crownCourtWithdrawalDate(LocalDate.of(2025, 6, 30))
            .applicantHistoryId(96)
            .caseId("CASE-123-C")
            .committalDate(LocalDate.of(2025, 6, 11))
            .repOrderStatus("CURR")
            .appealTypeCode(AppealType.ACN.getCode())
            .crownCourtOutcome(CrownCourtTrialOutcome.CONVICTED.getValue())
            .dateCreated(LocalDate.of(2025, 6, 20))
            .userCreated("joe-bloggs")
            .dateModified(LocalDate.of(2025, 6, 21).atStartOfDay())
            .userModified("alice-smith")
            .caseType(CrownCourtCaseType.EITHER_WAY.getValue())
            .build();

        RepOrderBillingEntity entity = TestEntityDataBuilder.getPopulatedRepOrderForBilling(123);

        RepOrderBillingDTO actualRepOrder = RepOrderBillingMapper.mapEntityToDTO(entity);

        assertEquals(expectedRepOrder, actualRepOrder);
    }

}
