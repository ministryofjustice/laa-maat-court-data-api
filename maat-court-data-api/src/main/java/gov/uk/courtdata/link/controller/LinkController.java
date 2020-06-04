package gov.uk.courtdata.link.controller;

import gov.uk.courtdata.link.validator.PreConditionsValidator;
import gov.uk.courtdata.model.CaseDetailsValidate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/link")
@Slf4j
@RequiredArgsConstructor
public class LinkController {

    private final PreConditionsValidator preConditionsValidator;

    @PostMapping("/validate")
    public ResponseEntity<Object> validate(@RequestBody CaseDetailsValidate caseDetailsValidate) {

        log.info("Validate link request.");
        preConditionsValidator.validate(caseDetailsValidate);

        return ResponseEntity.ok().build();
    }
}
