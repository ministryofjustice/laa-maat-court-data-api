package gov.uk.courtdata.eform.controller;

import gov.uk.courtdata.eform.exception.USNExceptionUtil;
import gov.uk.courtdata.eform.exception.UsnException;
import gov.uk.courtdata.eform.repository.entity.EformsAudit;
import gov.uk.courtdata.eform.service.EformAuditService;
import gov.uk.courtdata.testutils.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EformAuditController.class)
public class EformAuditControllerTest {

    private static final String ENDPOINT_FORMAT = "/api/eform/audit/";
    private static final int USN = 123;
    private static final int MAAT_REF = 456;
    private static final EformsAudit EFORM_AUDIT = EformsAudit.builder().usn(USN).build();

    private static final UsnException USN_VALIDATION_EXCEPTION = USNExceptionUtil.nonexistent(987);

    @MockBean
    private EformAuditService mockEformAuditService;

    @Autowired
    private MockMvc mvc;

    @Test
    void givenUSN_whenGetEformsAuditCalled_thenReturnEformsAudit() throws Exception {
        when(mockEformAuditService.retrieve(USN))
                .thenReturn(EFORM_AUDIT);

        mvc.perform(MockMvcRequestBuilders.get(url())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"usn\":"+USN+"}"));
    }

    @Test
    void givenNonExistentUSN_whenGetEformsAuditCalled_thenReturnAnError() throws Exception {
        when(mockEformAuditService.retrieve(USN))
                .thenThrow(USN_VALIDATION_EXCEPTION);

        mvc.perform(MockMvcRequestBuilders.get(url())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"code\":\"NOT_FOUND\",\"message\":\"The USN [987] does not exist in the data store.\"}"));
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

        mvc.perform(MockMvcRequestBuilders.post(url())
                .content(eformsAuditData)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(mockEformAuditService, times(1)).create(eformsAudit);
    }

    @NotNull
    private String url() {
        return ENDPOINT_FORMAT + USN;
    }
}
