package gov.uk.courtdata.wqlinkregister.service;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.wqlinkregister.impl.WQLinkRegisterImpl;
import gov.uk.courtdata.wqlinkregister.mapper.WQLinkRegisterMapper;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WQLinkRegisterServiceTest {
    @Mock
    private WQLinkRegisterImpl wqLinkRegisterImpl;

    @Mock
    private WQLinkRegisterMapper mapper;

    @InjectMocks
    private WQLinkRegisterService wqLinkRegisterService;

    @Test
    public void givenAValidCaseId_whenFindByCaseIdIsInvoked_shouldReturnOffenceDTO() {
        List wqLinkRegisterEntityList = List.of(TestEntityDataBuilder.getWQLinkRegisterEntity(8064716));
        when(wqLinkRegisterImpl.findByMaatId(TestEntityDataBuilder.TEST_CASE_ID))
                .thenReturn(wqLinkRegisterEntityList);
        List wqLinkRegisterDTOList = List.of(TestModelDataBuilder.getWQLinkRegisterDTO(8064716));
        when(mapper.wQLinkRegisterToWQLinkRegisterDTO(anyList())).thenReturn(wqLinkRegisterDTOList);
        wqLinkRegisterService.findByMaatId(TestEntityDataBuilder.TEST_CASE_ID);
        verify(wqLinkRegisterImpl, atLeastOnce()).findByMaatId(anyInt());
        verify(mapper, atLeastOnce()).wQLinkRegisterToWQLinkRegisterDTO(anyList());
    }
}
