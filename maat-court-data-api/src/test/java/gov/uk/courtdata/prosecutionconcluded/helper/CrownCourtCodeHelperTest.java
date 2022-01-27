package gov.uk.courtdata.prosecutionconcluded.helper;

import gov.uk.courtdata.entity.CrownCourtCode;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.repository.CrownCourtCodeRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CrownCourtCodeHelperTest {

    @InjectMocks
    private CrownCourtCodeHelper crownCourtCodeHelper;

    @Mock
    private CrownCourtCodeRepository crownCourtCodeRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testWhenOuCodeFound_thenReturnCode() {

        Optional<CrownCourtCode> optCrownCourtCode = Optional.of(
                CrownCourtCode.builder()
                        .code("1234")
                        .code("8899")
                        .build());
        when(crownCourtCodeRepository.findByOuCode(anyString())).thenReturn(optCrownCourtCode);

        String code = crownCourtCodeHelper.getCode(anyString());

        verify(crownCourtCodeRepository).findByOuCode(anyString());
        assertEquals("8899", code);
    }

    @Test(expected = MAATCourtDataException.class)
    public void testWhenOuCodeNotFound_thenThrowMAATCourtDataException() {

        Optional<CrownCourtCode> optCrownCourtCode = Optional.empty();
        when(crownCourtCodeRepository.findByOuCode(anyString())).thenReturn(optCrownCourtCode);

        crownCourtCodeHelper.getCode(anyString());
        verify(crownCourtCodeRepository).findByOuCode(anyString());
    }
}