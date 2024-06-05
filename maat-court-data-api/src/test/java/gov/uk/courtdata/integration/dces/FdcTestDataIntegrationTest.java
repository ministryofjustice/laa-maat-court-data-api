package gov.uk.courtdata.integration.dces;

import com.fasterxml.jackson.core.JsonProcessingException;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.dces.request.CreateFdcTestDataRequest;
import gov.uk.courtdata.dces.request.FdcNegativeTestType;
import gov.uk.courtdata.entity.ConcorContributionsEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.entity.FdcContributionsEntity;
import gov.uk.courtdata.entity.FdcItemsEntity;
import gov.uk.courtdata.entity.RepOrderCCOutComeEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.enums.ConcorContributionStatus;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
class FdcTestDataIntegrationTest extends MockMvcIntegrationTest {

//    @Value("${debt-collection-enforcement-domain-test-data}")
    private String ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement/test-data/generate_prepare_fdc_data_1";
    final ObjectMapper objectMapper = new ObjectMapper();
    private RepOrderEntity repOrderValid;
    private RepOrderEntity repOrderValid2;

    private RepOrderEntity repOrderFuture;

    private RepOrderEntity repOrderFutureReceived;

    private RepOrderEntity repOrderStatusInvalid;

    private RepOrderEntity repOrderOutcomeNull;

    private RepOrderEntity repOrderSentenceOrderNull;

    @BeforeEach
    public void setUp() {
        // SELECT DISTINCT RO.ID
        //                  FROM TOGDATA.CONCOR_CONTRIBUTIONS CC,
        //                      TOGDATA.REP_ORDERS RO,
        //                      TOGDATA.REP_ORDER_CROWN_COURT_OUTCOMES ROCCO
        //                  WHERE CC.REP_ID = RO.ID
        //                  AND RO.ID = ROCCO.REP_ID
        //                  AND CC.STATUS = 'SENT'
        //                  AND ROCCO.CCOO_OUTCOME IS NOT NULL
        //                  AND RO.SENTENCE_ORDER_DATE IS NOT NULL
        //                  AND TRUNC( ADD_MONTHS( NVL(RO.SENTENCE_ORDER_DATE, SYSDATE ), 5) ) <= TRUNC(SYSDATE)
        //                  AND RO.DATE_RECEIVED<'01-JAN-2015'

        LocalDate dateJan2010 = LocalDate.of(2010,1,1);
        LocalDate dateFuture = LocalDate.now().plus(1, ChronoUnit.MONTHS);

        // basic entity to satisfy all criteria
        repOrderValid = repos.repOrder.save(buildRepOrderEntity(dateJan2010, dateJan2010));
        repos.concorContributions.save(buildConcorContributionEntity(repOrderValid, ConcorContributionStatus.SENT));
        repos.crownCourtProcessing.save(buildCCOutcomeEntity(repOrderValid, "ACQUITTAL"));


        // 2nd entity to allow for testing quantity
        repOrderValid2 = repos.repOrder.save(buildRepOrderEntity(dateJan2010, dateJan2010));
        repos.concorContributions.save(buildConcorContributionEntity(repOrderValid2, ConcorContributionStatus.SENT));
        repos.crownCourtProcessing.save(buildCCOutcomeEntity(repOrderValid2, "ACQUITTAL"));

        // NEGATIVE SCENARIOS:

        // AND CC.STATUS = 'SENT'
        repOrderStatusInvalid = repos.repOrder.save(buildRepOrderEntity(dateJan2010, dateJan2010));
        repos.concorContributions.save(buildConcorContributionEntity(repOrderStatusInvalid, ConcorContributionStatus.REPLACED));
        repos.crownCourtProcessing.save(buildCCOutcomeEntity(repOrderStatusInvalid, "ACQUITTAL"));

        // AND ROCCO.CCOO_OUTCOME IS NOT NULL
        repOrderOutcomeNull = repos.repOrder.save(buildRepOrderEntity(dateJan2010, dateJan2010));
        repos.concorContributions.save(buildConcorContributionEntity(repOrderOutcomeNull, ConcorContributionStatus.SENT));
        repos.crownCourtProcessing.save(buildCCOutcomeEntity(repOrderOutcomeNull, null));

        // AND RO.SENTENCE_ORDER_DATE IS NOT NULL
        repOrderSentenceOrderNull = repos.repOrder.save(buildRepOrderEntity(null, dateJan2010));
        repos.concorContributions.save(buildConcorContributionEntity(repOrderSentenceOrderNull, ConcorContributionStatus.SENT));
        repos.crownCourtProcessing.save(buildCCOutcomeEntity(repOrderSentenceOrderNull, "ACQUITTAL"));

        // AND TRUNC( ADD_MONTHS( NVL(RO.SENTENCE_ORDER_DATE, SYSDATE ), 5) ) <= TRUNC(SYSDATE)
        repOrderFuture = repos.repOrder.save(buildRepOrderEntity(dateFuture, dateJan2010));
        repos.concorContributions.save(buildConcorContributionEntity(repOrderFuture, ConcorContributionStatus.SENT));
        repos.crownCourtProcessing.save(buildCCOutcomeEntity(repOrderFuture, "ACQUITTAL"));

        // AND RO.DATE_RECEIVED<'01-JAN-2015'
        repOrderFutureReceived = repos.repOrder.save(buildRepOrderEntity(dateJan2010, dateFuture));
        repos.concorContributions.save(buildConcorContributionEntity(repOrderFutureReceived, ConcorContributionStatus.SENT));
        repos.crownCourtProcessing.save(buildCCOutcomeEntity(repOrderFutureReceived, "ACQUITTAL"));


    }

    private RepOrderEntity buildRepOrderEntity(LocalDate sentenceOrderDate, LocalDate dateReceived){
        return RepOrderEntity.builder()
                .sentenceOrderDate(sentenceOrderDate)
                .dateReceived(dateReceived)
                .build();
    }

    private RepOrderCCOutComeEntity buildCCOutcomeEntity(RepOrderEntity repOrder, String outcome){
        return RepOrderCCOutComeEntity.builder()
                .repOrder(repOrder)
                .outcome(outcome)
                .build();
    }

    private ConcorContributionsEntity buildConcorContributionEntity(RepOrderEntity repOrder, ConcorContributionStatus status){
        return ConcorContributionsEntity.builder()
                .repId(repOrder.getId())
                .status(status)
                .build();
    }

    private FdcContributionsEntity buildFdcContributionEntity(){
        return FdcContributionsEntity.builder().userCreated("TEST-INVALID").build();
    }

    private FdcItemsEntity buildFdcItemsEntity(Integer fdcId){
        return FdcItemsEntity.builder().fdcId(fdcId).build();
    }

    private CreateFdcTestDataRequest buildRequest(Integer numTestEntries, boolean isNegative, FdcNegativeTestType negativeType){
        return CreateFdcTestDataRequest.builder()
                .numOfTestEntries(numTestEntries)
                .negativeTest(isNegative)
                .negativeTestType(negativeType)
                .build();
    }

    private String getRequestString(CreateFdcTestDataRequest request) throws JsonProcessingException {
        return objectMapper.writeValueAsString(request);
    }

    @Test
    void testNegativeResultsNotAccepted() throws Exception {
        CreateFdcTestDataRequest request = buildRequest(50, false, null);
        String requestString = getRequestString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("repOrderIds.length()").value(2))
                .andExpect(jsonPath("repOrderIds", Matchers.containsInAnyOrder(repOrderValid.getId(), repOrderValid2.getId())));
    }

    @Test
    void testFetchValidData() throws Exception {
        CreateFdcTestDataRequest request = buildRequest(1, false, null);
        String requestString = getRequestString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("repOrderIds").value(repOrderValid.getId()));

        // verify creation of fdcContribution
        List<FdcContributionsEntity> fdcContributionsEntityList = repos.fdcContributions.findAll();
        assertEquals(1,fdcContributionsEntityList.size(), "Should have only 1 saved fdcContribution");
        // check it's in the right status
        FdcContributionsEntity fdcContribution = fdcContributionsEntityList.get(0);
        assertEquals(FdcContributionsStatus.WAITING_ITEMS, fdcContribution.getStatus());
        assertEquals(repOrderValid.getId(), fdcContribution.getRepOrderEntity().getId());

        // verify the creation of a new fdc item.
        List<FdcItemsEntity> fdcItemsEntityList = repos.fdcItemsRepository.findAll();
        assertEquals(1,fdcItemsEntityList.size(), "Should have only 1 saved fdcItem");
        FdcItemsEntity fdcItem = fdcItemsEntityList.get(0);
        assertEquals(fdcContribution.getId(), fdcItem.getFdcId());
    }


    @Test
    void testNegativeTypeFdcItem() throws Exception {
        // setup previously existing data for deletion verification
        FdcContributionsEntity priorFdcContributions = buildFdcContributionEntity();
        priorFdcContributions.setRepOrderEntity(repOrderValid2);
        repos.fdcContributions.save(priorFdcContributions);
        FdcItemsEntity priorFdcItem = buildFdcItemsEntity(priorFdcContributions.getId());
        repos.fdcItemsRepository.save(priorFdcItem);

        CreateFdcTestDataRequest request = buildRequest(1, true, FdcNegativeTestType.FDC_ITEM);
        String requestString = getRequestString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("repOrderIds").value(repOrderValid.getId()));

        // verify there is no new fdc item.
        List<FdcItemsEntity> fdcItemsEntityList = repos.fdcItemsRepository.findAll();
        assertEquals(1,fdcItemsEntityList.size(), "Should have only 1 saved fdcItem");
        FdcItemsEntity fdcItem = fdcItemsEntityList.get(0);

        // verify creation of fdcContribution
        List<FdcContributionsEntity> fdcContributionsEntityList = repos.fdcContributions.findAll();
        assertEquals(2,fdcContributionsEntityList.size(), "Should have only 2 saved fdcContribution");
        for(FdcContributionsEntity fdcContribution : fdcContributionsEntityList){
            // check if we've got the pre-created one.
            if(priorFdcContributions.getId().equals(fdcContribution.getId())){
                assertNull(fdcContribution.getStatus()); // unchanged
                assertEquals(repOrderValid2.getId(), fdcContribution.getRepOrderEntity().getId());
                assertEquals(fdcContribution.getId(), fdcItem.getFdcId()); // fdcItem should have been deleted
            }
            else{
                // else we've found the new one that the process created.
                // check it's in the right status
                assertEquals(FdcContributionsStatus.WAITING_ITEMS, fdcContribution.getStatus());
                assertEquals(repOrderValid.getId(), fdcContribution.getRepOrderEntity().getId());
            }
        }
    }

    @Test
    void testNegativeTypeFdcStatus() throws Exception {
        CreateFdcTestDataRequest request = buildRequest(1, true, FdcNegativeTestType.FDC_STATUS);
        String requestString = getRequestString(request);
        // create unrelated fdcContribution for verifying behaviour
        FdcContributionsEntity unrelatedFdcContribution = repos.fdcContributions.save(buildFdcContributionEntity());

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("repOrderIds").value(repOrderValid.getId()));

        // verify the creation of a new fdc item.
        List<FdcItemsEntity> fdcItemsEntityList = repos.fdcItemsRepository.findAll();
        assertEquals(1,fdcItemsEntityList.size(), "Should have only 1 saved entry");
        FdcItemsEntity fdcItem = fdcItemsEntityList.get(0);

        // verify creation of fdcContribution
        List<FdcContributionsEntity> fdcContributionsEntityList = repos.fdcContributions.findAll();
        assertEquals(2,fdcContributionsEntityList.size(), "Should have only 2 saved entries. One generated mid-test");
        // check it's in the right status
        for(FdcContributionsEntity fdcContribution: fdcContributionsEntityList){
            if(Objects.equals(fdcContribution.getId(), unrelatedFdcContribution.getId())){
                // check we've not modified an unrelated entry.
                assertNull(fdcContribution.getStatus());
            }
            else {
                assertEquals(FdcContributionsStatus.SENT, fdcContribution.getStatus());
                assertEquals(repOrderValid.getId(), fdcContribution.getRepOrderEntity().getId());
                assertEquals(fdcContribution.getId(), fdcItem.getFdcId());
            }
        }
    }


    @Test
    void testNegativeTypeSOD() throws Exception {
        CreateFdcTestDataRequest request = buildRequest(1, true, FdcNegativeTestType.SOD);
        String requestString = getRequestString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("repOrderIds").value(repOrderValid.getId()));

        // verify creation of fdcContribution
        List<FdcContributionsEntity> fdcContributionsEntityList = repos.fdcContributions.findAll();
        assertEquals(1,fdcContributionsEntityList.size(), "Should have only 1 saved fdcContribution");
        // check it's in the right status
        FdcContributionsEntity fdcContribution = fdcContributionsEntityList.get(0);
        assertEquals(FdcContributionsStatus.WAITING_ITEMS, fdcContribution.getStatus());
        assertEquals(repOrderValid.getId(), fdcContribution.getRepOrderEntity().getId());

        // verify the creation of a new fdc item.
        List<FdcItemsEntity> fdcItemsEntityList = repos.fdcItemsRepository.findAll();
        assertEquals(1,fdcItemsEntityList.size(), "Should have only 1 saved fdcItem");
        FdcItemsEntity fdcItem = fdcItemsEntityList.get(0);
        assertEquals(fdcContribution.getId(), fdcItem.getFdcId());

        // verify SOD behaviour
        Optional<RepOrderEntity> sodRepOrderOpt = repos.repOrder.findById(repOrderValid.getId());
        assertEquals(LocalDate.now().plus(3, ChronoUnit.MONTHS), sodRepOrderOpt.get().getSentenceOrderDate());

        List<RepOrderCCOutComeEntity> ccoList = repos.crownCourtProcessing.findByRepOrder_Id(repOrderValid.getId());
        assertFalse(ccoList.isEmpty());
    }

    @Test
    void testNegativeTypeCCO() throws Exception {
        CreateFdcTestDataRequest request = buildRequest(1, true, FdcNegativeTestType.CCO);
        String requestString = getRequestString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("repOrderIds").value(repOrderValid.getId()));

        // verify creation of fdcContribution
        List<FdcContributionsEntity> fdcContributionsEntityList = repos.fdcContributions.findAll();
        assertEquals(1,fdcContributionsEntityList.size(), "Should have only 1 saved fdcContribution");
        // check it's in the right status
        FdcContributionsEntity fdcContribution = fdcContributionsEntityList.get(0);
        assertEquals(FdcContributionsStatus.WAITING_ITEMS, fdcContribution.getStatus());
        assertEquals(repOrderValid.getId(), fdcContribution.getRepOrderEntity().getId());

        // verify the creation of a new fdc item.
        List<FdcItemsEntity> fdcItemsEntityList = repos.fdcItemsRepository.findAll();
        assertEquals(1,fdcItemsEntityList.size(), "Should have only 1 saved fdcItem");
        FdcItemsEntity fdcItem = fdcItemsEntityList.get(0);
        assertEquals(fdcContribution.getId(), fdcItem.getFdcId());

        // verify CCO has been deleted.
        List<RepOrderCCOutComeEntity> ccoList = repos.crownCourtProcessing.findByRepOrder_Id(repOrderValid.getId());
        assertTrue(ccoList.isEmpty());

    }




}