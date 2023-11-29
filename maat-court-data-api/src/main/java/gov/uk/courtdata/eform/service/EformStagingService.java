package gov.uk.courtdata.eform.service;

import gov.uk.courtdata.eform.dto.EformStagingDTO;
import gov.uk.courtdata.eform.exception.USNExceptionUtil;
import gov.uk.courtdata.eform.mapper.EformStagingDTOMapper;
import gov.uk.courtdata.eform.repository.EformStagingRepository;
import gov.uk.courtdata.eform.repository.entity.EformsStagingEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * The responsibility of this class is to provide data repository access without verification,
 * verification is the responsibility of the calling class.
 * e.g. verify that a given entity exists before attempting to delete it
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EformStagingService {

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
                .orElseThrow(() -> USNExceptionUtil.nonexistent(usn));

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

    @Transactional
    public void updateEformStagingFields(Integer usn, EformsStagingEntity eformsStaging) {
        Optional<EformsStagingEntity> eformsStagingRecord = eformStagingRepository.findById(usn);

        if (eformsStagingRecord.isPresent()) {
            for (Field declaredField : EformsStagingEntity.class.getDeclaredFields()) {
                ReflectionUtils.makeAccessible(declaredField);
                Object fieldValue = ReflectionUtils.getField(declaredField, eformsStaging);
                if (fieldValue != null) {
                    ReflectionUtils.setField(declaredField, eformsStagingRecord.get(), fieldValue);
                }
            }
            eformStagingRepository.save(eformsStagingRecord.get());
        } else {
            throw USNExceptionUtil.nonexistent(usn);
        }
    }
}