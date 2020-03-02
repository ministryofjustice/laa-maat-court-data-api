package gov.uk.courtdata.jms;

import com.google.gson.Gson;
import gov.uk.courtdata.exception.MaatCourtDataException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.unlink.UnLinkProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;


@Slf4j
@AllArgsConstructor
@Service
public class UnlinkListener {

    private final Gson gson;

    private final UnLinkProcessor unLinkProcessor;

    /**
     *
     */
    @JmsListener(destination = "${aws.sqs.queue.unlink}")
    public void receive(@Payload final String message) throws JmsException {

        try {

            log.info("Received JSON Message  {}", message);
            Unlink unlink = gson.fromJson(message, Unlink.class);
            unLinkProcessor.process(unlink);
            log.debug("Message converted {} ", unlink);
            log.info(" Unlink success!!!");

        } catch (ValidationException vex) {
            //TODO: Generate SNS to notify to slack channel.
            log.warn("validation failed.");
            log.error("Validation error {}", vex);
        } catch (MaatCourtDataException mex) {
            log.warn("Create link failed.");
            log.error("MaatCourtDataExceptiontion  {}", mex);
            mex.printStackTrace();
        }
    }


}
