package gov.uk.courtdata.eform.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.eform.dto.EformStagingDTO;
import gov.uk.courtdata.eform.mapper.EformStagingDTOMapper;
import gov.uk.courtdata.eform.repository.EformStagingRepository;
import gov.uk.courtdata.eform.repository.entity.EformsStagingEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * The responsibility of this class is to provide data repository access without verification,
 * verification is the responsibility of the calling class.
 * e.g. verify that a given entity exists before attempting to delete it
 */
@Component
@RequiredArgsConstructor
@Slf4j
@XRayEnabled
public class EformStagingDAOImpl implements EformStagingDAO {

    private final EformStagingRepository eformStagingRepository;
    private final EformStagingDTOMapper eformStagingDTOMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void create(EformStagingDTO eformStagingDTO) {
        EformsStagingEntity eformsStagingEntity = eformStagingDTOMapper.toEformsStagingEntity(eformStagingDTO);

        eformStagingRepository.saveAndFlush(eformsStagingEntity);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void update(EformStagingDTO oldEformStagingDTO, EformStagingDTO newEformStagingDTO) {
        EformsStagingEntity oldEformsStagingEntity = eformStagingDTOMapper.toEformsStagingEntity(oldEformStagingDTO);
        EformsStagingEntity newEformsStagingEntity = eformStagingDTOMapper.toEformsStagingEntity(newEformStagingDTO);
        EformsStagingEntity oldEntry = eformStagingRepository.findById(oldEformsStagingEntity.getUsn()).get();

        oldEntry.setUsn(newEformsStagingEntity.getUsn());
        eformStagingRepository.saveAndFlush(oldEntry);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @Override
    public EformStagingDTO retrieve(int usn) {
        Optional<EformsStagingEntity> eformsStagingEntity = eformStagingRepository.findById(usn);

        return eformStagingDTOMapper.toEformStagingDTO(eformsStagingEntity.get());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void delete(int usn) {
        eformStagingRepository.deleteById(usn);
    }

    @Override
    public boolean isUsnPresentInDB(int usn) {
        return eformStagingRepository.existsById(usn);
    }
}