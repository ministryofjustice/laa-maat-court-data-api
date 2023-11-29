package gov.uk.courtdata.service;

import gov.uk.courtdata.model.StoredProcedureRequest;
import gov.uk.courtdata.repository.StoredProcedureRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StoredProcedureServiceTest {

    @InjectMocks
    private StoredProcedureService service;

    @Mock
    private StoredProcedureRepository repository;

    @Test
    void givenAValidInput_whenExecuteStoredProcedureIsInvoked_thenApplicationDTOIsReturned() throws Exception{
        service.executeStoredProcedure(new StoredProcedureRequest());
        verify(repository).executeStoredProcedure(any());
    }

}