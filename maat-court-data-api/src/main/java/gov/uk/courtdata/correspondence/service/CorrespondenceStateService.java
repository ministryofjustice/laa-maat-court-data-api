package gov.uk.courtdata.correspondence.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.correspondence.dto.CorrespondenceStateDTO;
import gov.uk.courtdata.entity.CorrespondenceStateEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.repository.CorrespondenceStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@XRayEnabled
public class CorrespondenceStateService {

    private final CorrespondenceStateRepository correspondenceStateRepository;

    @Transactional(readOnly = true)
    public String getCorrespondenceStatus(final int repId) {
        log.info("Get correspondence status for repId: {}", repId);
        CorrespondenceStateEntity correspondenceState = correspondenceStateRepository.findByRepId(repId);
        if (correspondenceState == null) {
            throw new RequestedObjectNotFoundException(String.format("No corresponsdence state found for repId= %s", repId));
        }
        return correspondenceState.getStatus();
    }

    @Transactional
    public CorrespondenceStateDTO createCorrespondenceState(final CorrespondenceStateDTO corrStateDTO) {
        log.info("Create correspondence state for {}", corrStateDTO);
        CorrespondenceStateEntity correspondenceStateEntity = CorrespondenceStateEntity.builder()
                .repId(corrStateDTO.getRepId())
                .status(corrStateDTO.getStatus()).build();
        CorrespondenceStateEntity savedEntity = correspondenceStateRepository.saveAndFlush(correspondenceStateEntity);
        return CorrespondenceStateDTO.builder()
                .status(savedEntity.getStatus())
                .repId(savedEntity.getRepId())
                .build();
    }

    @Transactional
    public CorrespondenceStateDTO updateCorrespondenceState(final CorrespondenceStateDTO corrStateDTO) {
        log.info("Update correspondence state for repId {}", corrStateDTO.getRepId());
        CorrespondenceStateEntity entity = correspondenceStateRepository.findByRepId(corrStateDTO.getRepId());
        if (entity == null) {
            throw new RequestedObjectNotFoundException(String.format("No corresponsdence state found for repId=%s", corrStateDTO.getRepId()));
        } else {
            entity.setStatus(corrStateDTO.getStatus());
            CorrespondenceStateEntity savedEntity = correspondenceStateRepository.saveAndFlush(entity);
            return CorrespondenceStateDTO.builder()
                    .status(savedEntity.getStatus())
                    .repId(savedEntity.getRepId())
                    .build();
        }
    }

}
