package gov.uk.courtdata.helper;


import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class RepOrderCPDataHelperTest {

    @InjectMocks
    private RepOrderCPDataHelper repOrderCPDataHelper;

    @Mock
    private RepOrderCPDataRepository repOrderCPDataRepository;



    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenValidDefendantIdWhenFindCalledThenReturnMaatId() {

        RepOrderCPDataEntity repOrderCPDataEntity = RepOrderCPDataEntity.builder()
                .defendantId("556677")
                .caseUrn("caseun334")
                .repOrderId(12345)
                .inCommonPlatform("Y")
                .build();

        Mockito.when(repOrderCPDataRepository.findByDefendantId("556677")).thenReturn(Optional.ofNullable(repOrderCPDataEntity));

        Integer maatId = repOrderCPDataHelper.getMaatIdByDefendantId(repOrderCPDataEntity.getDefendantId());

        assertThat(maatId).isEqualTo(12345);

    }

    @Test(expected = ValidationException.class)
    public void givenInValidDefendantIdWhenFindCalledThenReturnMaatId() {

        RepOrderCPDataEntity repOrderCPDataEntity = RepOrderCPDataEntity.builder()
                .defendantId("556677")
                .caseUrn("caseun334")
                .repOrderId(12345)
                .inCommonPlatform("Y")
                .build();

        repOrderCPDataHelper.getMaatIdByDefendantId(repOrderCPDataEntity.getDefendantId());
    }



}