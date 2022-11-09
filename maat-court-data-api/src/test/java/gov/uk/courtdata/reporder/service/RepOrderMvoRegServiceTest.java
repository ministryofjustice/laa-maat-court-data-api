package gov.uk.courtdata.reporder.service;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.reporder.impl.RepOrderMvoRegImpl;
import gov.uk.courtdata.reporder.mapper.RepOrderMvoRegMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RepOrderMvoRegServiceTest {

    @Mock
    private RepOrderMvoRegImpl repOrderMvoRegImpl;
    @Mock
    private RepOrderMvoRegMapper repOrderMvoRegMapper;
    @InjectMocks
    private RepOrderMvoRegService repOrderMvoRegService;

    @Test
    void givenValidMvoId_whenFindByCurrentMvoRegistrationIsInvoked_thenRepOrderMvoRegIsReturned() {
        when(repOrderMvoRegImpl.findByCurrentMvoRegistration(anyInt())).thenReturn(TestEntityDataBuilder.getRepOrderMvoRegEntityInfo());
        when(repOrderMvoRegMapper.repOrderMvoRegEntityInfoToRepOrderMvoRegDTO(anyList())).thenReturn(TestModelDataBuilder.getRepOrderMvoRegDTOList());
        assertThat(repOrderMvoRegService.findByCurrentMvoRegistration(TestModelDataBuilder.MVO_ID)).isEqualTo(TestModelDataBuilder.getRepOrderMvoRegDTOList());
    }
}