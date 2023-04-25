package gov.uk.courtdata.eform.validator;

import gov.uk.courtdata.eform.repository.EformStagingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsnValidatorTest {

    @Mock
    private EformStagingRepository mockEformStagingRepository;

    @InjectMocks
    private UsnValidator usnValidator;

    @Test
    void validate() {
        // TODO
    }
}