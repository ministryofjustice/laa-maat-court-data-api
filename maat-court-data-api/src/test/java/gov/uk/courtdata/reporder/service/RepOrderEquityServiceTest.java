package gov.uk.courtdata.reporder.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.entity.RepOrderEquityEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.reporder.repository.RepOrderEquityRepository;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(RepOrderEquityService.class)
class RepOrderEquityServiceTest {

    private static final int ID = 123;
    private static final int NON_EXISTENT_ID = 888;
    private static final int REP_ID = 777;

    private static final RepOrderEquityEntity REP_ORDER_EQUITY_ENTITY =
            RepOrderEquityEntity.builder().id(ID).build();

    @MockitoBean
    private RepOrderEquityRepository mockRepOrderEquityRepository;

    private RepOrderEquityService repOrderEquityService;

    @BeforeEach
    void setUp() {
        repOrderEquityService = new RepOrderEquityService(mockRepOrderEquityRepository);
    }

    @Test
    void givenID_whenRetrieveCalled_thenReturnRepOrderEquity() {
        Mockito.when(mockRepOrderEquityRepository.findById(ID)).thenReturn(Optional.of(REP_ORDER_EQUITY_ENTITY));

        RepOrderEquityEntity repOrderEquity = repOrderEquityService.retrieve(REP_ORDER_EQUITY_ENTITY.getId());

        assertThat(repOrderEquity).isEqualTo(REP_ORDER_EQUITY_ENTITY);
    }

    @Test
    void givenNonExistentID_whenRetrieveCalled_thenThrowException() {
        assertThatThrownBy(() -> repOrderEquityService.retrieve(NON_EXISTENT_ID))
                .isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessage("No Rep Order Equity found with ID: " + NON_EXISTENT_ID);
    }

    @Test
    void givenValidRepOrderEquity_whenCreateCalled_thenSuccessfullyCreateRepOrderEquity() {
        repOrderEquityService.create(REP_ORDER_EQUITY_ENTITY);

        Mockito.verify(mockRepOrderEquityRepository).save(REP_ORDER_EQUITY_ENTITY);
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

        Mockito.verify(mockRepOrderEquityRepository).findById(ID);
        Mockito.verify(mockRepOrderEquityRepository).save(updatedRepOrderEquityEntity);
    }

    @Test
    void givenID_whenDeleteCalled_thenDeleteRepOrderEquity() {
        repOrderEquityService.delete(ID);

        Mockito.verify(mockRepOrderEquityRepository).deleteById(ID);
    }
}
