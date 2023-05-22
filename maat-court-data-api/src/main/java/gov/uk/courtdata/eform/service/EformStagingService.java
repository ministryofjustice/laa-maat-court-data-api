package gov.uk.courtdata.eform.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.eform.dto.EformStagingDTO;
import gov.uk.courtdata.eform.exception.UsnException;
import gov.uk.courtdata.eform.mapper.EformStagingDTOMapper;
import gov.uk.courtdata.eform.repository.EformStagingRepository;
import gov.uk.courtdata.eform.repository.entity.EformsStagingEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The responsibility of this class is to provide data repository access without verification,
 * verification is the responsibility of the calling class.
 * e.g. verify that a given entity exists before attempting to delete it
 */
@Service
@RequiredArgsConstructor
@Slf4j
@XRayEnabled
public class EformStagingService {

    private static final String NONEXISTENT_MESSAGE_FORMAT = "The USN [%d] does not exist in the data store.";

    private final EformStagingRepository eformStagingRepository;
    private final EformStagingDTOMapper eformStagingDTOMapper;

    @Transactional
    public void create(EformStagingDTO eformStagingDTO) {
        EformsStagingEntity eformsStagingEntity = eformStagingDTOMapper.toEformsStagingEntity(eformStagingDTO);

        eformStagingRepository.saveAndFlush(eformsStagingEntity);
    }

    @Transactional(readOnly = true)
    public EformStagingDTO retrieve(int usn) {
        EformsStagingEntity eformsStagingEntity = eformStagingRepository.findById(usn)
                .orElseThrow(() -> UsnException.nonexistent(String.format(NONEXISTENT_MESSAGE_FORMAT, usn)));

        return eformStagingDTOMapper.toEformStagingDTO(eformsStagingEntity);
    }

    @Transactional
    public void delete(int usn) {
        eformStagingRepository.deleteById(usn);
    }

    public boolean isUsnPresentInDB(int usn) {
        return eformStagingRepository.existsById(usn);
    }

    @Transactional
    public EformStagingDTO createOrRetrieve(int usn) {
        if (isUsnPresentInDB(usn)) {
            return retrieve(usn);
        }
        EformStagingDTO eformStagingDTO = EformStagingDTO.builder().usn(usn).build();
        create(eformStagingDTO);
        return eformStagingDTO;
    }
}