package gov.uk.courtdata.link.validator;

import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class CPDataValidatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private RepOrderCPDataRepository repOrderCPDataRepository;

    @InjectMocks
    private CPDataValidator CPDataValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testWhenCPDataNotExists_throwsException() {

        final int maatId = 1000;
        Mockito.when(repOrderCPDataRepository.findByrepOrderId(maatId)).thenReturn(Optional.empty());
        thrown.expect(ValidationException.class);
        thrown.expectMessage("MaatId 1000 has no common platform data created in MAAT.");

        CPDataValidator.validate(CaseDetails.builder().maatId(maatId)
                .caseUrn("caseURN").build());
    }


    @Test
    public void testWhenCaseURNValidAndExists_validationPasses() {

        final int maatId = 1000;
        final String urn = "caseURN111";
        Mockito.when(repOrderCPDataRepository.findByrepOrderId(maatId))
                .thenReturn(Optional.of(RepOrderCPDataEntity.builder().repOrderId(maatId).caseUrn(urn).build()));
        CPDataValidator.validate(CaseDetails.builder().maatId(maatId)
                .caseUrn(urn).build());

    }

}
