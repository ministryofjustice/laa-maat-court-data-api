package gov.uk.courtdata.link.validator;

import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CPDataValidatorTest {

    @Mock
    private RepOrderCPDataRepository repOrderCPDataRepository;

    @InjectMocks
    private CPDataValidator CPDataValidator;

    @Test
    public void testWhenCaseURNisNullonRequest_throwsException() {

        Assertions.assertThrows(ValidationException.class, () ->
                CPDataValidator.validate(CaseDetails.builder().maatId(100)
                .caseUrn(null).build()), "CaseURN can't be null or empty on request.");

    }

    @Test
    public void testWhenCPDataNotExists_throwsException() {

        final int maatId = 1000;
        Mockito.when(repOrderCPDataRepository.findByrepOrderId(maatId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ValidationException.class, ()->
                CPDataValidator.validate(CaseDetails.builder().maatId(maatId)
                .caseUrn("caseURN").build()), "1000 has no common platform data created against Maat application.");
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
