package gov.uk.courtdata.wqlinkregister.impl;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WQLinkRegisterImplTest {

    @Mock
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @InjectMocks
    private WQLinkRegisterImpl wqLinkRegisterImpl;

    @Test
    public void givenAValidMaatId_whenFindByMaatIdIsInvoked_shouldReturnWQLinkRegisterRecords() {
        wqLinkRegisterImpl.findByMaatId(TestModelDataBuilder.REP_ID);
        verify(wqLinkRegisterRepository, atLeastOnce()).findBymaatId(anyInt());
    }

}