package gov.uk.courtdata.link.service;

import com.google.gson.Gson;
import gov.uk.courtdata.exception.MaatCourtDataException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.link.service.CreateLinkService;
import gov.uk.courtdata.model.CaseDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * <code>CreateLinkListener</code> a JMS listener to consume create link messages from queue.
 */
@Slf4j
@AllArgsConstructor
@Service
public class CreateLinkListener {

    private final CreateLinkService createLinkService;

    private final Gson gson;

    /**
     * @param message
     * @throws JmsException
     */
    @JmsListener(destination = "${aws.sqs.queue.link}")
    public void receive(@Payload final String message) throws JmsException {

        try {

            log.info("Received JSON Message  {}", message);
            CaseDetails linkMessage = gson.fromJson(message, CaseDetails.class);
            log.info("Message converted {} ", linkMessage);
            createLinkService.saveAndLink(linkMessage);

        } catch (ValidationException vex) {
            log.warn("validation failed.");
            log.error("Validation error {}", vex);
        } catch (MaatCourtDataException mex) {
            log.warn("Create link failed.");
            log.error("MaatCourtDataExceptiontion  {}", mex);
            mex.printStackTrace();
        }
    }


}
