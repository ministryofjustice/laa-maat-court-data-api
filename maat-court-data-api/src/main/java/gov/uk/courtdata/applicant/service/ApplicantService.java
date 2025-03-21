package gov.uk.courtdata.applicant.service;

import gov.uk.courtdata.applicant.dto.SendToCCLFDTO;
import gov.uk.courtdata.applicant.repository.ApplicantRepository;
import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.helper.ReflectionHelper;
import gov.uk.courtdata.reporder.service.RepOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicantService {

    private final ApplicantRepository applicantRepository;
    private final RepOrderService repOrderService;
    private final ApplicantHistoryService applicantHistoryService;

    public Applicant find(Integer id) {
        log.info("ApplicantService::find - Start");
        return applicantRepository.findById(id)
                .orElseThrow(() -> new RequestedObjectNotFoundException(String.format("Applicant not found for id %d", id)));
    }

    public void update(Integer id, Map<String, Object> applicant) {
        log.info("ApplicantService::update - Start");
        Applicant currentApplicant = applicantRepository.findById(id)
                .orElseThrow(() -> new RequestedObjectNotFoundException(String.format("Applicant not found for id %d", id)));

        ReflectionHelper.updateEntityFromMap(currentApplicant, applicant);
        applicantRepository.save(currentApplicant);
    }

    public void delete(Integer id) {
        log.info("ApplicantService::delete - Start");
        applicantRepository.deleteById(id);
    }

    public Applicant create(Applicant applicant) {
        log.info("ApplicantService::create - Start");
        return applicantRepository.saveAndFlush(applicant);
    }

    public void updateSendToCCLF(SendToCCLFDTO sendToCCLFDTO) {
        log.info("ApplicantService::UpdateSendToCCLF - Start");
        this.update(sendToCCLFDTO.getApplId(), Map.of("sendToCclf", "Y"));
        repOrderService.update(sendToCCLFDTO.getRepId(), Map.of("isSendToCCLF", true));
        applicantHistoryService.update(sendToCCLFDTO.getApplId(), Map.of("sendToCclf", "Y"));
    }

}
