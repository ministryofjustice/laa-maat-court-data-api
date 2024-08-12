package gov.uk.courtdata.correspondence.service;

import gov.uk.courtdata.entity.CorrespondenceStateEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.repository.CorrespondenceStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.justice.laa.crime.enums.contribution.CorrespondenceStatus;

@Slf4j
@Service
@RequiredArgsConstructor
public class CorrespondenceStateService {

    private final CorrespondenceStateRepository correspondenceStateRepository;

    @Transactional(readOnly = true)
    public CorrespondenceStatus getCorrespondenceStatus(final int repId) {
        log.info("Get correspondence status for repId: {}", repId);
        CorrespondenceStateEntity state = correspondenceStateRepository.findByRepId(repId);
        if (state == null) {
            throw new RequestedObjectNotFoundException(
                    String.format("No correspondence state found for repId= %s", repId));
        }
        return CorrespondenceStatus.getFrom(state.getStatus());
    }

    @Transactional
    public CorrespondenceStatus createCorrespondenceState(final int repId, final CorrespondenceStatus corrStateDTO) {
        log.info("Create correspondence state for {}", corrStateDTO);
        CorrespondenceStateEntity correspondenceStateEntity =
                CorrespondenceStateEntity.builder()
                        .repId(repId)
                        .status(corrStateDTO.getStatus())
                        .build();
        CorrespondenceStateEntity saved = correspondenceStateRepository.saveAndFlush(correspondenceStateEntity);
        return CorrespondenceStatus.getFrom(saved.getStatus());
    }

    @Transactional
    public CorrespondenceStatus updateCorrespondenceState(final int repId, final CorrespondenceStatus corrStateDTO) {
        log.info("Create or Update correspondence state for repId {}", repId);
        CorrespondenceStateEntity entity = correspondenceStateRepository.findByRepId(repId);
        if (entity == null) {
            throw new RequestedObjectNotFoundException(
                    String.format("No correspondence state found for repId= %s", repId));
        } else {
            entity.setStatus(corrStateDTO.getStatus());
            CorrespondenceStateEntity saved = correspondenceStateRepository.saveAndFlush(entity);
            return CorrespondenceStatus.getFrom(saved.getStatus());
        }
    }
}
