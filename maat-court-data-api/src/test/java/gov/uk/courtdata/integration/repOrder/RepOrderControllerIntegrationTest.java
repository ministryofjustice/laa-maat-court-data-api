package gov.uk.courtdata.integration.repOrder;

import org.junit.jupiter.api.Disabled;

@Disabled
public class RepOrderControllerIntegrationTest {


//
//    @Test
//    public void givenARepIdIsMissing_whenUpdateAppDateCompletedInvoked_theCorrectErrorResponseIsReturned() throws Exception{
//        runBadRequestErrorScenario(
//                "Rep Id is missing from request and is required",
//                post(BASE_URL+ "/update-date-completed").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new UpdateAppDateCompleted().builder().build())));
//
//    }

//    @Test
//    public void givenAUpdateDateCompletedIsMissing_whenUpdateAppDateCompletedInvoked_theCorrectErrorResponseIsReturned() throws Exception{
//        runBadRequestErrorScenario(
//                "Assessment Date completed is missing from request and is required",
//                post(BASE_URL+ "/update-date-completed").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(
//                        UpdateAppDateCompleted.builder().repId(TestModelDataBuilder.REP_ORDERS_ID).build())));
//
//    }
//    @Test
//    public void givenAInvalidRepId_whenUpdateAppDateCompletedInvoked_theCorrectErrorResponseIsReturned() throws Exception{
//        new UpdateAppDateCompleted();
//        runBadRequestErrorScenario(
//                "MAAT/REP ID: "+TestModelDataBuilder.MAAT_ID+" is invalid.",
//                post(BASE_URL+ "/update-date-completed").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(
//                        UpdateAppDateCompleted.builder().repId(TestModelDataBuilder.MAAT_ID).assessmentDateCompleted(LocalDateTime.now()).build())));
//
//    }
//
//    @Test
//    public void givenAValidRepOrdersAvailable_whenUpdateAppDateCompletedInvoked_theCompletedDateShouldUpdate() throws Exception{
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
//        LocalDateTime expectedDate = LocalDateTime.parse(TestModelDataBuilder.APP_DATE_COMPLETED, formatter);
//        runSuccessScenario(MockMvcRequestBuilders.post(BASE_URL + "/update-date-completed").contentType(MediaType.APPLICATION_JSON)
//                .content(TestModelDataBuilder.getUpdateAppDateCompletedJson())
//                .contentType(MediaType.APPLICATION_JSON));
//        RepOrderEntity repOrderEntity = repOrderRepository.getById(TestModelDataBuilder.REP_ORDERS_ID);
//        assertThat(repOrderEntity.getId()).isEqualTo(TestModelDataBuilder.REP_ORDERS_ID);
//        assertThat(repOrderEntity.getAssessmentDateCompleted()).isEqualTo(expectedDate);
//    }


}
