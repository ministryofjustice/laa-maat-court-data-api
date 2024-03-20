package gov.uk.courtdata.eform.controller;

import gov.uk.courtdata.eform.exception.USNExceptionUtil;
import gov.uk.courtdata.eform.exception.UsnException;
import gov.uk.courtdata.eform.repository.entity.EformsAudit;
import gov.uk.courtdata.eform.service.EformAuditService;
import gov.uk.courtdata.testutils.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EformAuditController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EformAuditControllerTest {

    private static final String BASE_ENDPOINT_FORMAT = "/api/eform/audit";
    private static final int USN = 123;
    private static final int NON_EXISTENT_USN = 789;
    private static final int MAAT_REF = 456;
    private static final EformsAudit EFORMS_AUDIT = EformsAudit.builder().usn(USN).build();
    private static final EformsAudit EMPTY_EFORMS_AUDIT = new EformsAudit();

    private static final UsnException USN_VALIDATION_EXCEPTION = USNExceptionUtil.nonexistent(NON_EXISTENT_USN);

    @MockBean
    private EformAuditService mockEformAuditService;

    @Autowired
    private MockMvc mvc;

    @Test
    void givenUSN_whenGetEformsAuditCalled_thenReturnEformsAudit() throws Exception {
        when(mockEformAuditService.retrieve(USN))
                .thenReturn(EFORMS_AUDIT);

        mvc.perform(MockMvcRequestBuilders.get(BASE_ENDPOINT_FORMAT+"/"+USN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"usn\":"+USN+"}"));
    }

    @Test
    void givenNonExistentUSN_whenGetEformsAuditCalled_thenReturnAnError() throws Exception {
        when(mockEformAuditService.retrieve(NON_EXISTENT_USN))
                .thenReturn(EMPTY_EFORMS_AUDIT);

        mvc.perform(MockMvcRequestBuilders.get(BASE_ENDPOINT_FORMAT+"/"+NON_EXISTENT_USN)
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenValidData_whenCreateEformsAuditCalled_thenSuccessfullyCreateEformsAudit() throws Exception {

        String eformsAuditData = FileUtils.readResourceToString("eform/audit/create_payload.json");

        EformsAudit eformsAudit = EformsAudit.builder()
                .usn(USN)
                .maatRef(MAAT_REF)
                .userCreated("test-s")
                .statusCode("Processing")
                .build();

        mvc.perform(MockMvcRequestBuilders.post(BASE_ENDPOINT_FORMAT)
                .content(eformsAuditData)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(mockEformAuditService, times(1)).create(eformsAudit);
    }

    @Test
    void givenUSN_whenDeleteEformsAuditCalled_thenSuccessfullyDeleteEformsAudit() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(BASE_ENDPOINT_FORMAT+"/"+USN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(mockEformAuditService, times(1)).delete(USN);
    }
}
