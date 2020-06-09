package gov.uk.courtdata.unlink.service;

import com.google.gson.Gson;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.unlink.processor.UnLinkProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @JmsListener(destination = "${cloud-platform.aws.sqs.queue.unlink}")
    public void receive(@Payload final String message)  {

        Unlink unlink = gson.fromJson(message, Unlink.class);
        unLinkProcessor.process(unlink);
    }
}
