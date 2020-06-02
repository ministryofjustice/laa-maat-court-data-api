package gov.uk.courtdata.unlink.validator;

import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.validator.MaatIdValidator;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UnLinkValidationProcessorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private MaatIdValidator maatIdValidator;

    @InjectMocks
    private UnLinkValidationProcessor unLinkValidationProcessor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


    @Test @Ignore
    public void testWhenMaatId_Valid() {
        final int maatId = 123456;
        Unlink unlink = Unlink.builder()
                .maatId(123456)
                .reasonId(999888)
                .build();

        Mockito.when(maatIdValidator.validate(123456)).thenReturn(Optional.empty());

        unLinkValidationProcessor.validate(unlink);

    }

    @After
    public void tearDown() throws Exception {

    }


}
