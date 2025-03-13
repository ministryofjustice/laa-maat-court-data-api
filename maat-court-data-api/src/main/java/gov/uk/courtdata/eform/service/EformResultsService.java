package gov.uk.courtdata.eform.service;

import gov.uk.courtdata.eform.exception.UsnException;
import gov.uk.courtdata.eform.repository.EformResultsRepository;
import gov.uk.courtdata.eform.repository.entity.EformResultsEntity;
import gov.uk.courtdata.helper.ReflectionHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EformResultsService {

    private static final String USN_NOT_FOUND = "The USN [%d] not found in Eform Results table";

    private final EformResultsRepository eformResultsRepository;

    @Transactional(readOnly = true)
    public List<EformResultsEntity> getAllEformResults(Integer usn) {
        return eformResultsRepository.findAllByUsn(usn);
    }

    @Transactional
    public void create(EformResultsEntity eformResultsEntity) {
        eformResultsRepository.save(eformResultsEntity);
    }

    @Transactional
    public void delete(Integer usn) {
        eformResultsRepository.deleteAllByUsn(usn);
    }

    @Transactional
    public void updateEformResultFields(Integer usn, EformResultsEntity eformResultsEntity) {
        EformResultsEntity latestEformResult = Optional.ofNullable(eformResultsRepository.findTopByUsnOrderByIdDesc(usn))
            .orElseThrow(() -> new UsnException(HttpStatus.NOT_FOUND, String.format(USN_NOT_FOUND, usn)));

        ReflectionHelper.updateEntityFromObject(latestEformResult, eformResultsEntity);
        eformResultsRepository.save(latestEformResult);
    }
}
