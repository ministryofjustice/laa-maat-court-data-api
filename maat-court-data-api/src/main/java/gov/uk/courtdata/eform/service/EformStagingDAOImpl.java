package gov.uk.courtdata.eform.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.eform.dto.EformStagingDTO;
import gov.uk.courtdata.eform.mapper.EformStagingDTOMapper;
import gov.uk.courtdata.eform.repository.EformStagingRepository;
import gov.uk.courtdata.entity.EformsStagingEntity;
import gov.uk.courtdata.exception.RefIdAlreadyExsistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@XRayEnabled
public class EformStagingDAOImpl implements EformStagingDAO {

    private final EformStagingRepository eformStagingRepository;
    private final EformStagingDTOMapper eformStagingDTOMapper;

    public void create(EformStagingDTO eformStagingDTO) {
        EformsStagingEntity eformsStagingEntity = eformStagingDTOMapper.toEformsStagingEntity(eformStagingDTO);

        if (!eformStagingRepository.existsById(eformsStagingEntity.getUsn())) {
            eformStagingRepository.save(eformsStagingEntity);
        } else {
            throw new RefIdAlreadyExsistException("The USN number entered already exists.");
        }
    }

    public void update(EformStagingDTO eformStagingDTO) {
        EformsStagingEntity eformsStagingEntity = eformStagingDTOMapper.toEformsStagingEntity(eformStagingDTO);

        if (eformStagingRepository.existsById(eformsStagingEntity.getUsn())) {
            eformStagingRepository.save(eformsStagingEntity);
        } else {
            this.create(eformStagingDTO); // TODO determine correct behaviour here?
        }
    }

    public Optional<EformStagingDTO> retrieve(int usn) {
        Optional<EformsStagingEntity> eformsStagingEntity = eformStagingRepository.findById(usn);
        if (eformsStagingEntity.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(eformStagingDTOMapper.toEformsStagingDTO(eformsStagingEntity.get()));
    }

    public void delete(int usn) {
        eformStagingRepository.deleteById(usn);
    }
}