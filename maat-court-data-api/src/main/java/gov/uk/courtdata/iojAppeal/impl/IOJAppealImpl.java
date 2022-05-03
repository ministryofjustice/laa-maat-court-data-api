package gov.uk.courtdata.iojAppeal.impl;

import gov.uk.courtdata.dto.IOJAppealDTO;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.iojAppeal.mapper.IOJAppealMapper;
import gov.uk.courtdata.repository.IOJAppealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IOJAppealImpl {

    private final IOJAppealRepository iojAppealRepository;
    private final IOJAppealMapper iojAppealMapper;

    public IOJAppealEntity find(Integer iojAppealId) {
        return iojAppealRepository.getById(iojAppealId);
    }

    public IOJAppealEntity findByRepId(int repId) {
        return iojAppealRepository.findByRepId(repId);
    }

    public IOJAppealEntity create(IOJAppealDTO iojAppealDTO) {
        var iojAppealEntity = iojAppealMapper.toIOJIojAppealEntity(iojAppealDTO);
        return iojAppealRepository.save(iojAppealEntity);
    }

    public void setOldIOJAppealsReplaced(Integer repId, Integer iojAppealIDNotToUpdate) {
        iojAppealRepository.setOldIOJAppealsReplaced(repId, iojAppealIDNotToUpdate);
    }

    public IOJAppealEntity update(IOJAppealDTO iojAppealDTO) {
        var existingIOJAppealEntity = iojAppealRepository.getById(iojAppealDTO.getId());

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
}
