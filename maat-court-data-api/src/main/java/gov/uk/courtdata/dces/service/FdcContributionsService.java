package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.dces.response.FdcContributionEntry;
import gov.uk.courtdata.dces.response.FdcContributionsGlobalUpdateResponse;
import gov.uk.courtdata.dces.response.FdcContributionsResponse;
import gov.uk.courtdata.entity.FdcContributionsEntity;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import gov.uk.courtdata.repository.FdcContributionsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class FdcContributionsService {
    private final FdcContributionsRepository fdcContributionsRepository;
    private final DebtCollectionRepository debtCollectionRepository;

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

        List<FdcContributionEntry> fdcContributionEntries = fdcFileList.stream()
                .map(BUILD_FDC_ENTRY)
                .toList();
        return FdcContributionsResponse.builder().fdcContributions(fdcContributionEntries).build();
    }

    public FdcContributionsGlobalUpdateResponse fdcContributionGlobalUpdate(){
        log.info("Running global update process for Final Defence Cost contributions.");
        int numberOfUpdates;
        boolean updateSuccessful;
        try{
            numberOfUpdates = executeGlobalUpdate();
            updateSuccessful=true;
        } catch(Exception e){
            log.error("FDC Global update failed with: {}", e.getMessage(), e);
            throw e;
        }
        return FdcContributionsGlobalUpdateResponse.builder().numberOfUpdates(numberOfUpdates).successful(updateSuccessful).build();
    }


    private int executeGlobalUpdate(){
        log.info("executeGlobalUpdate entered");
        int[] update1Result = debtCollectionRepository.globalUpdatePart1();
        log.info("FDC Global update Part 1 affected: {}", getResult(update1Result));
        int[] update2Result = debtCollectionRepository.globalUpdatePart2();
        log.info("FDC Global update Part 2 affected: {}", getResult(update2Result));
        int response = combineGlobalUpdateResults(update1Result, update2Result);
        log.info("executeGlobalUpdate exiting");
        return response;
    }

    private int combineGlobalUpdateResults(int[] part1Results, int[] part2Results){
        return getResult(part1Results)+getResult(part2Results);
    }
    private int getResult(int[] results){
        return ArrayUtils.isNotEmpty(results) ? results[0] : 0;
    }

}