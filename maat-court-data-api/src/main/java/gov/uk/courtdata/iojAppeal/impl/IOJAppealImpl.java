package gov.uk.courtdata.iojAppeal.impl;

import gov.uk.courtdata.dto.IOJAppealDTO;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.iojAppeal.mapper.IOJAppealMapper;
import gov.uk.courtdata.repository.IOJAppealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class IOJAppealImpl {

    private final IOJAppealRepository iojAppealRepository;
    private final IOJAppealMapper iojAppealMapper;

    public IOJAppealEntity find(Integer iojAppealId) {
        return iojAppealRepository.findById(iojAppealId).orElseThrow(()-> new NoSuchElementException("No IOJAppeal object found. ID: "+iojAppealId));
    }

    public IOJAppealEntity create(IOJAppealDTO iojAppealDTO){
        var iojAppealEntity = iojAppealMapper.toIOJIojAppealEntity(iojAppealDTO);
        return iojAppealRepository.save(iojAppealEntity);
    }

    public void setOldIOJAppealReplaced(Integer repId, Integer iojAppealIDNotToUpdate) {
        iojAppealRepository.setOldIOJAppealsReplaced(repId, iojAppealIDNotToUpdate);
    }

    public IOJAppealEntity update(IOJAppealDTO iojAppealDTO){
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
