package gov.uk.courtdata.billing.service;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.billing.repository.MaatReferenceRepository;
import gov.uk.courtdata.exception.RecordsAlreadyExistException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MaatReferenceServiceTest {

    @Mock
    private MaatReferenceRepository maatReferenceRepository;
    
    private MaatReferenceService maatReferenceService;
    
    @BeforeEach
    void setUp() {
        maatReferenceService = new MaatReferenceService(maatReferenceRepository);
    }

    @Test
    void givenNoInput_whenPopulateTableInvoked_thenInsertIsSuccessful() {
        maatReferenceService.populateTable();
        verify(maatReferenceRepository, atLeastOnce()).count();
        verify(maatReferenceRepository, atLeastOnce()).populateMaatReferences();
    }
    
    @Test
    void givenEntriesAlreadyExist_whenPopulateTableInvoked_thenThrowAnException() {
        when(maatReferenceRepository.count()).thenReturn(10L);

        Assertions.assertThrows(RecordsAlreadyExistException.class,
            () -> maatReferenceService.populateTable());
    }
}
