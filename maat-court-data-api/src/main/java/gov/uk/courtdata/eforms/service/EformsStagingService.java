package gov.uk.courtdata.eforms.service;


import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.eforms.builder.EformsStagingBuilder;
import gov.uk.courtdata.dto.EformsStagingDTO;
import gov.uk.courtdata.eforms.mapper.EformsStagingDTOMapper;
import gov.uk.courtdata.entity.EformsStagingEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.exception.ValidationException;
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
    public void execute(final EformsStagingDTO eformsStagingDTO ) {

        if (eformsStagingDTO == null)
            throw new MAATCourtDataException("New work reason code is required");

        log.info("Mapping EformsStagingEntity from EformsStagingDTO");
        EformsStagingEntity eformsStagingEntity = eformsStagingDTOMapper.toEformsStagingEntity(eformsStagingDTO);

        eformsStagingRepository.save(eformsStagingEntity);
    }


}
