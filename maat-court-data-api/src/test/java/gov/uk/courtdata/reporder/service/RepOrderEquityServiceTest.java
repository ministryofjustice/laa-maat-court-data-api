package gov.uk.courtdata.reporder.service;

import gov.uk.courtdata.entity.RepOrderEquityEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.reporder.repository.RepOrderEquityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(RepOrderEquityService.class)
public class RepOrderEquityServiceTest {

    private static final int ID = 123;
    private static final int NON_EXISTENT_ID = 888;
    private static final int REP_ID = 777;

    private static final RepOrderEquityEntity REP_ORDER_EQUITY_ENTITY = RepOrderEquityEntity
            .builder()
            .id(ID)
            .build();

    @MockBean
    private RepOrderEquityRepository mockRepOrderEquityRepository;

    private RepOrderEquityService repOrderEquityService;

    @BeforeEach
    void setUp() {
        repOrderEquityService = new RepOrderEquityService(mockRepOrderEquityRepository);
    }

    @Test
    void givenID_whenRetrieveCalled_thenReturnRepOrderEquity() {
        Mockito.when(mockRepOrderEquityRepository.findById(ID))
                .thenReturn(Optional.of(REP_ORDER_EQUITY_ENTITY));

        RepOrderEquityEntity repOrderEquity = repOrderEquityService.retrieve(REP_ORDER_EQUITY_ENTITY.getId());

        assertEquals(REP_ORDER_EQUITY_ENTITY, repOrderEquity);
    }

    @Test
    void givenNonExistentID_whenRetrieveCalled_thenThrowException() {
        assertThatThrownBy(
                () -> repOrderEquityService.retrieve(NON_EXISTENT_ID)
        ).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessage("No Rep Order Equity found with ID: " + NON_EXISTENT_ID);
    }

    @Test
    void givenValidRepOrderEquity_whenCreateCalled_thenSuccessfullyCreateRepOrderEquity() {
        repOrderEquityService.create(REP_ORDER_EQUITY_ENTITY);

        Mockito.verify(mockRepOrderEquityRepository, Mockito.times(1)).save(REP_ORDER_EQUITY_ENTITY);
    }

    @Test
    void givenValidRepOrderEquity_whenUpdateCalled_thenSuccessfullyUpdateRepOrderEquity() {
        RepOrderEquityEntity updatedRepOrderEquityEntity = RepOrderEquityEntity.builder()
                .repId(REP_ID)
                .undeclared("Y")
                .active("N")
                .build();

        when(mockRepOrderEquityRepository.findById(ID)).thenReturn(Optional.ofNullable(updatedRepOrderEquityEntity));
        repOrderEquityService.update(ID, updatedRepOrderEquityEntity);

        Mockito.verify(mockRepOrderEquityRepository, Mockito.times(1)).findById(ID);
        Mockito.verify(mockRepOrderEquityRepository, Mockito.times(1)).save(updatedRepOrderEquityEntity);
    }

    @Test
    void givenID_whenDeleteCalled_thenDeleteRepOrderEquity() {
        repOrderEquityService.delete(ID);

        Mockito.verify(mockRepOrderEquityRepository, Mockito.times(1)).deleteById(ID);
    }
}
