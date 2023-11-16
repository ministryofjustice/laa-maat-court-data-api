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
    private static final EformsAudit EFORM_AUDIT = EformsAudit
            .builder()
            .usn(USN)
            .maatRef(MAAT_REF)
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
                .thenReturn(Optional.of(EFORM_AUDIT));

        EformsAudit eformsAudit = eformAuditService.retrieve(EFORM_AUDIT.getUsn());

        assertEquals(EFORM_AUDIT, eformsAudit);
    }

    @Test
    void givenNonExistentUSN_whenRetrieveCalled_thenThrowException() {
        assertThatThrownBy(
                () -> eformAuditService.retrieve(NON_EXISTENT_USN)
        ).isInstanceOf(UsnException.class)
                .hasMessage("The USN ["+NON_EXISTENT_USN+"] does not exist in the data store.");
    }

    @Test
    void givenValidEformsAudit_whenCreateCalled_thenSuccessfullyCreateEformsAudit() {
        eformAuditService.create(EFORM_AUDIT);

        Mockito.verify(mockEformAuditRepository, Mockito.times(1)).save(EFORM_AUDIT);
    }
}
