package gov.uk.courtdata.eform.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.eform.dto.EformStagingDTO;
import gov.uk.courtdata.eform.mapper.EformStagingDTOMapper;
import gov.uk.courtdata.eform.repository.EformStagingRepository;
import gov.uk.courtdata.eform.repository.entity.EformsStagingEntity;
import gov.uk.courtdata.eform.validator.UsnValidator;
import gov.uk.courtdata.exception.USNValidationException;
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
    private final UsnValidator usnValidator;

    @Override
    public void create(EformStagingDTO eformStagingDTO) {
        EformsStagingEntity eformsStagingEntity = eformStagingDTOMapper.toEformsStagingEntity(eformStagingDTO);

        usnValidator.validate(eformStagingDTO.getUsn());
        eformStagingRepository.save(eformsStagingEntity);
    }

    @Override
    public void update(EformStagingDTO eformStagingDTO) {
        EformsStagingEntity eformsStagingEntity = eformStagingDTOMapper.toEformsStagingEntity(eformStagingDTO);

        if (eformStagingRepository.existsById(eformsStagingEntity.getUsn())) {
            eformStagingRepository.save(eformsStagingEntity);
        } else {
            throw new USNValidationException("The USN number entered is not valid.");
        }
    }

    @Override
    public Optional<EformStagingDTO> retrieve(int usn) {
        Optional<EformsStagingEntity> eformsStagingEntity = eformStagingRepository.findById(usn);
        if (eformsStagingEntity.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(eformStagingDTOMapper.toEformsStagingDTO(eformsStagingEntity.get()));
    }

    @Override
    public void delete(int usn) {
        eformStagingRepository.deleteById(usn);
    }
}