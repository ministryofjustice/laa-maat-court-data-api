package gov.uk.courtdata.eform.service;

import gov.uk.courtdata.eform.exception.UsnException;
import gov.uk.courtdata.eform.repository.EformAuditRepository;
import gov.uk.courtdata.eform.repository.entity.EformsAudit;
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

@ExtendWith(MockitoExtension.class)
@WebMvcTest(EformAuditService.class)
public class EformAuditServiceTest {

    private static final int USN = 123;
    private static final int NON_EXISTENT_USN = 789;
    private static final int MAAT_REF = 456;
    private static final EformsAudit EFORMS_AUDIT = EformsAudit
            .builder()
            .usn(USN)
            .maatRef(MAAT_REF)
            .build();
    private static final EformsAudit BLANK_EFORMS_AUDIT = EformsAudit
            .builder()
            .usn(null)
            .maatRef(null)
            .build();

    @MockBean
    private EformAuditRepository mockEformAuditRepository;

    private EformAuditService eformAuditService;

    @BeforeEach
    void setUp() {
        eformAuditService = new EformAuditService(mockEformAuditRepository);
    }

    @Test
    void givenUSN_whenRetrieveCalled_thenReturnAnEformsAudit() {
        Mockito.when(mockEformAuditRepository.findByUsn(USN))
                .thenReturn(Optional.of(EFORMS_AUDIT));

        EformsAudit eformsAudit = eformAuditService.retrieve(EFORMS_AUDIT.getUsn());

        assertEquals(EFORMS_AUDIT, eformsAudit);
    }

    @Test
    void givenNonExistentUSN_whenRetrieveCalled_thenThrowException() {
        assertEquals(BLANK_EFORMS_AUDIT, eformAuditService.retrieve(NON_EXISTENT_USN));
    }

    @Test
    void givenValidEformsAudit_whenCreateCalled_thenSuccessfullyCreateEformsAudit() {
        eformAuditService.create(EFORMS_AUDIT);

        Mockito.verify(mockEformAuditRepository, Mockito.times(1)).save(EFORMS_AUDIT);
    }

    @Test
    void givenUSN_whenDeleteCalled_thenDeleteEformsAudit() {
        eformAuditService.delete(USN);

        Mockito.verify(mockEformAuditRepository, Mockito.times(1)).deleteAllByUsn(USN);
    }
}
