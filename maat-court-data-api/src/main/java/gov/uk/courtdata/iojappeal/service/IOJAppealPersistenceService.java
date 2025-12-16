package gov.uk.courtdata.iojappeal.service;

import gov.uk.courtdata.dto.IOJAppealDTO;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.iojappeal.mapper.IOJAppealMapper;
import gov.uk.courtdata.repository.IOJAppealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.common.model.ioj.ApiCreateIojAppealRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class IOJAppealPersistenceService {

    private final IOJAppealRepository iojAppealRepository;

    public IOJAppealEntity find(Integer iojAppealId) {
        return iojAppealRepository.getReferenceById(iojAppealId);
    }

    public IOJAppealEntity findByRepId(int repId) {
        return iojAppealRepository.findByRepId(repId);
    }

    public void create(IOJAppealEntity iojAppealEntity) {
        iojAppealRepository.save(iojAppealEntity);
    }

    public void setOldIOJAppealsReplaced(Integer repId, Integer iojAppealIDNotToUpdate) {
        iojAppealRepository.setOldIOJAppealsReplaced(repId, iojAppealIDNotToUpdate);
    }

    public IOJAppealEntity update(IOJAppealDTO iojAppealDTO) {
        var existingIOJAppealEntity = iojAppealRepository.getReferenceById(iojAppealDTO.getId());

        existingIOJAppealEntity.setAppealSetupDate(iojAppealDTO.getAppealSetupDate());
        existingIOJAppealEntity.setNworCode(iojAppealDTO.getNworCode());
        existingIOJAppealEntity.setCmuId(iojAppealDTO.getCmuId());
        existingIOJAppealEntity.setIapsStatus(iojAppealDTO.getIapsStatus());
        existingIOJAppealEntity.setAppealSetupResult(iojAppealDTO.getAppealSetupResult());
        existingIOJAppealEntity.setDecisionDate(iojAppealDTO.getDecisionDate());
        existingIOJAppealEntity.setDecisionResult(iojAppealDTO.getDecisionResult());
        existingIOJAppealEntity.setIderCode(iojAppealDTO.getIderCode());
        existingIOJAppealEntity.setNotes(iojAppealDTO.getNotes());
        existingIOJAppealEntity.setUserModified(iojAppealDTO.getUserModified());

        return iojAppealRepository.save(existingIOJAppealEntity);
    }

    public IOJAppealEntity findCurrentPassedByRepId(int repId) {
        return iojAppealRepository.findByRepIdAndReplacedAndDecisionResult(repId, "N", "PASS");
    }
}
