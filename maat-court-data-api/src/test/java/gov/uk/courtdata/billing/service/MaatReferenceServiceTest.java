package gov.uk.courtdata.billing.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.billing.repository.MaatReferenceRepository;
import gov.uk.courtdata.exception.RecordsAlreadyExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MaatReferenceServiceTest {

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

        assertThrows(RecordsAlreadyExistException.class,
            () -> maatReferenceService.populateTable());
    }
    
    @Test
    void givenNoInput_whenDeleteMaatReferencesInvoked_thenDeleteIsSuccessful() {
        maatReferenceService.deleteMaatReferences();
        verify(maatReferenceRepository, atLeastOnce()).deleteMaatReferences();
    }

    @Test
    void givenARepositoryException_whenDeleteMaatReferencesInvoked_thenThrowAnException() {
        doThrow(new RuntimeException()).when(maatReferenceRepository).deleteMaatReferences();
        
        assertThrows(RuntimeException.class, () -> {
            maatReferenceService.deleteMaatReferences();
        });
    }
}
