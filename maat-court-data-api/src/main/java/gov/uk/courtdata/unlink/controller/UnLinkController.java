package gov.uk.courtdata.unlink.controller;

import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.unlink.validator.UnlinkValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UnLinkController {

        @Autowired
        private UnlinkValidator unlinkValidator;

        @PostMapping("/valid")
        public ResponseEntity unLinkCase(@RequestHeader("Laa-Transaction-Id") String laaTransactionId,
                                         @RequestBody Unlink unlink) {

            log.info("LAA Status Update Request received - laa-transaction-id:{}", laaTransactionId);

            log.info("Request received: {}", unlink.toString());
            unlinkValidator.validateRequest(unlink);

            //List<WqLinkRegisterEntity> wqLinkRegisterEntityList = wqLinkRegisterRepository.findBymaatId(maatId);
            //unlinkValidator.validateWQLinkRegister(wqLinkRegisterEntityList, maatId);


            return new ResponseEntity(null, HttpStatus.OK);
        }




}

