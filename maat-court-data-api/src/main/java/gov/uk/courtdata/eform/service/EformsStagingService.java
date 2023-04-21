package gov.uk.courtdata.eform.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.EformsStagingDTO;
import gov.uk.courtdata.eform.mapper.EformsStagingDTOMapper;
import gov.uk.courtdata.eform.repository.EformsStagingRepository;
import gov.uk.courtdata.entity.EformsStagingEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@XRayEnabled
public class EformsStagingService {

    // TODO create interface for this
    private final EformsStagingRepository eformsStagingRepository;
    private final EformsStagingDTOMapper eformsStagingDTOMapper;

    public void create(EformsStagingDTO eformsStagingDTO) {
        // TODO Check if exists, if so error
        EformsStagingEntity eformsStagingEntity = eformsStagingDTOMapper.toEformsStagingEntity(eformsStagingDTO);
        eformsStagingRepository.save(eformsStagingEntity);
    }

    public void update(EformsStagingDTO eformsStagingDTO) {
        // TODO Check if exists, if not, throw error
        this.create(eformsStagingDTO);
    }

    public Optional<EformsStagingDTO> retrieve(int usn) {
        Optional<EformsStagingEntity> eformsStagingEntity = eformsStagingRepository.findById(usn);
        if(eformsStagingEntity.isEmpty()){
            return Optional.empty();
        }

        return Optional.of(eformsStagingDTOMapper.toEformsStagingDTO(eformsStagingEntity.get()));
    }

    public void delete(int usn) {
        eformsStagingRepository.deleteById(usn);
    }
}