package gov.uk.courtdata.offence.impl;

import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.repository.OffenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OffenceImpl {

    private final OffenceRepository offenceRepository;

    public List<OffenceEntity> findByCaseId(Integer caseId) {
        return offenceRepository.findByCaseId(caseId);
    }

    public Integer getNewOffenceCount(Integer caseId, String offenceId) {
        return offenceRepository.getNewOffenceCount(caseId, offenceId);
    }
}
