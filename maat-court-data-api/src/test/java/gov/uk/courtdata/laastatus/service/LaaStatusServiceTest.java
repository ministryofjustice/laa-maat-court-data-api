package gov.uk.courtdata.laastatus.service;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.laastatus.impl.LaaStatusUpdateImpl;
import gov.uk.courtdata.repository.IdentifierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LaaStatusServiceTest {

    @InjectMocks
    private LaaStatusService laaStatusService;

    @Mock
    private LaaStatusUpdateImpl laaStatusUpdateImpl;

    @Mock
    private IdentifierRepository identifierRepository;

    @Test
    public void givenCourtDataDataDTO_whenLaaStatusCalled_thenVerifyServicesCalled() {

        CourtDataDTO courtDataDTO = CourtDataDTO.builder()
                .txId(11111)
                .build();

        //when
        laaStatusService.execute(courtDataDTO);

        //then
        verify(laaStatusUpdateImpl).execute(courtDataDTO);
        verify(identifierRepository).getTxnID();
    }
}