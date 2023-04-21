package gov.uk.courtdata.eforms.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.EformsStagingDTO;
import gov.uk.courtdata.eforms.mapper.EformsStagingDTOMapper;
import gov.uk.courtdata.entity.EformsStagingEntity;
import gov.uk.courtdata.repository.EformsStagingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@XRayEnabled
public class EformsStagingService {

    private final EformsStagingRepository eformsStagingRepository;
    private final EformsStagingDTOMapper eformsStagingDTOMapper;

    public void create(EformsStagingDTO eformsStagingDTO){
        EformsStagingEntity eformsStagingEntity = eformsStagingDTOMapper.toEformsStagingEntity(eformsStagingDTO);
        eformsStagingRepository.save(eformsStagingEntity);
    }

    public void update(EformsStagingDTO eformsStagingDTO){
        this.create(eformsStagingDTO);
    }

    public EformsStagingDTO retrieve(int usn){
        EformsStagingEntity eformsStagingEntity = eformsStagingRepository.findById(usn).get();
        return eformsStagingDTOMapper.toEformsStagingDTO(eformsStagingEntity);
    }

    public void delete(int usn){
        eformsStagingRepository.deleteById(usn);
    }
}