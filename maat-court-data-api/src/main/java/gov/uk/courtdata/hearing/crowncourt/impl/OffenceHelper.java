package gov.uk.courtdata.hearing.crowncourt.impl;

import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.entity.PleaEntity;
import gov.uk.courtdata.entity.VerdictEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.Plea;
import gov.uk.courtdata.model.Verdict;
import gov.uk.courtdata.repository.OffenceRepository;
import gov.uk.courtdata.repository.PleaRepository;
import gov.uk.courtdata.repository.VerdictRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class OffenceHelper {

    private final WqLinkRegisterRepository wqLinkRegisterRepository;
    private final OffenceRepository offenceRepository;

    private final PleaRepository pleaRepository;
    private final VerdictRepository verdictRepository;

    public List<Offence> getOffences(int maatId) {

        List<WqLinkRegisterEntity> wqLinkRegisterEntities = wqLinkRegisterRepository.findBymaatId(maatId);
        if (!wqLinkRegisterEntities.isEmpty()) {

            int caseId = wqLinkRegisterEntities.get(0).getCaseId();

            List<OffenceEntity> offenceEntityList = offenceRepository.findByCaseId(caseId);

            return offenceEntityList
                    .stream()
                    .filter(no -> Objects.nonNull(no.getOffenceId()))
                    .map(off->
                            Offence.builder()
                                    .offenceId(off.getOffenceId())
                                    .plea(getPlea(off.getOffenceId()))
                                    .verdict(getVerdict(off.getOffenceId()))
                                    .build())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private Plea getPlea(String offenceId) {

        Optional<PleaEntity> pleaEntityOptional = pleaRepository.getLatestPleaByOffence(offenceId);
            if (pleaEntityOptional.isPresent()) {
                return Plea.builder()
                        .pleaValue(pleaEntityOptional.get().getPleaValue())
                        .build();
            }
        return null;
    }

    private Verdict getVerdict(String offenceId) {

        Optional<VerdictEntity> verdictEntityOptional = verdictRepository.getLatestVerdictByOffence(offenceId);
            if (verdictEntityOptional.isPresent()) {
                VerdictEntity recentVerdict = verdictEntityOptional.get();
                return Verdict.builder()
                        .categoryType(recentVerdict.getCategoryType())
                        .category(recentVerdict.getCategory())
                        .build();
            }
        return null;
    }
}
