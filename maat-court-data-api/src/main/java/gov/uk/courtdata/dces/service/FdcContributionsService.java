package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.dces.response.FdcContributionEntry;
import gov.uk.courtdata.dces.response.FdcContributionsResponse;
import gov.uk.courtdata.entity.FdcContributionsEntity;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import gov.uk.courtdata.repository.FdcContributionsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class FdcContributionsService {
    private final FdcContributionsRepository fdcContributionsRepository;

    private static final Function<FdcContributionsEntity, FdcContributionEntry> BUILD_FDC_ENTRY =
            entity -> FdcContributionEntry.builder()
                    .id(entity.getId())
                    .maatId(entity.getRepOrderEntity().getId())
                    .finalCost(entity.getFinalCost())
                    .dateCalculated(entity.getDateCalculated())
                    .lgfsCost(entity.getLgfsCost())
                    .agfsCost(entity.getAgfsCost())
                    .sentenceOrderDate(Objects.nonNull(entity.getRepOrderEntity())?entity.getRepOrderEntity().getSentenceOrderDate():null)
                    .build();

    public FdcContributionsResponse getFdcContributionFiles(FdcContributionsStatus status) {
        log.info("Getting fdc contribution file with status with the -> {}", status);
        final List<FdcContributionsEntity> fdcFileList = fdcContributionsRepository.findByStatus(status);

        List<FdcContributionEntry> fdcContributionEntries = fdcFileList.stream().map(
                BUILD_FDC_ENTRY
        ).toList();
        return FdcContributionsResponse.builder().fdcContributions(fdcContributionEntries).build();
    }

}