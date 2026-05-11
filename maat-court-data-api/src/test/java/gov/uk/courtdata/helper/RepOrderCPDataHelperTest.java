package gov.uk.courtdata.helper;

import static org.assertj.core.api.Assertions.assertThat;

import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RepOrderCPDataHelperTest {

    @InjectMocks
    private RepOrderCPDataHelper repOrderCPDataHelper;

    @Mock
    private RepOrderCPDataRepository repOrderCPDataRepository;

    @Test
    void givenValidDefendantIdWhenFindCalledThenReturnMaatId() {

        RepOrderCPDataEntity repOrderCPDataEntity = RepOrderCPDataEntity.builder()
                .defendantId("556677")
                .caseUrn("caseun334")
                .repOrderId(12345)
                .inCommonPlatform("Y")
                .build();

        Mockito.when(repOrderCPDataRepository.findByDefendantId("556677"))
                .thenReturn(Optional.ofNullable(repOrderCPDataEntity));

        Integer maatId = repOrderCPDataHelper.getMaatIdByDefendantId(repOrderCPDataEntity.getDefendantId());

        assertThat(maatId).isEqualTo(12345);
    }

    @Test
    void givenInValidDefendantIdWhenFindCalledThenReturnMaatId() {

        RepOrderCPDataEntity repOrderCPDataEntity = RepOrderCPDataEntity.builder()
                .defendantId("556677")
                .caseUrn("caseun334")
                .repOrderId(12345)
                .inCommonPlatform("Y")
                .build();
        Assertions.assertThrows(
                ValidationException.class,
                () -> repOrderCPDataHelper.getMaatIdByDefendantId(repOrderCPDataEntity.getDefendantId()));
    }
}
