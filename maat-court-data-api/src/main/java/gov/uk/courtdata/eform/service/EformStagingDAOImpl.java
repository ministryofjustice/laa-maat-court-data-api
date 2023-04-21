package gov.uk.courtdata.eform.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.eform.dto.EformStagingDTO;
import gov.uk.courtdata.eform.mapper.EformStagingDTOMapper;
import gov.uk.courtdata.eform.repository.EformStagingRepository;
import gov.uk.courtdata.entity.EformsStagingEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@XRayEnabled
public class EformStagingDAOImpl implements EformStagingDAO {

    // TODO create interface for this
    private final EformStagingRepository eformStagingRepository;
    private final EformStagingDTOMapper eformStagingDTOMapper;

    public void create(EformStagingDTO eformStagingDTO) {
        // TODO Check if exists, if so error
        EformsStagingEntity eformsStagingEntity = eformStagingDTOMapper.toEformsStagingEntity(eformStagingDTO);
        eformStagingRepository.save(eformsStagingEntity);
    }

    public void update(EformStagingDTO eformStagingDTO) {
        // TODO Check if exists, if not, throw error
        this.create(eformStagingDTO);
    }

    public Optional<EformStagingDTO> retrieve(int usn) {
        Optional<EformsStagingEntity> eformsStagingEntity = eformStagingRepository.findById(usn);
        if(eformsStagingEntity.isEmpty()){
            return Optional.empty();
        }

        return Optional.of(eformStagingDTOMapper.toEformsStagingDTO(eformsStagingEntity.get()));
    }

    public void delete(int usn) {
        eformStagingRepository.deleteById(usn);
    }
}