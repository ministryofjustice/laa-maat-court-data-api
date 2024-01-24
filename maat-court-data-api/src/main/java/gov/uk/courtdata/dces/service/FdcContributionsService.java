package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.dces.response.FdcContributionsResponse;
import gov.uk.courtdata.entity.FdcContributionsEntity;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import gov.uk.courtdata.repository.FdcContributionsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FdcContributionsService {
    private final FdcContributionsRepository fdcContributionsRepository;

    public List<FdcContributionsResponse> getFdcContributionFiles(FdcContributionsStatus status) {
        log.info("Getting fdc contribution file with status with the -> {}", status);
        final List<FdcContributionsEntity> fdcFileList = fdcContributionsRepository.findByStatus(status);

        return fdcFileList.stream().map( cc -> FdcContributionsResponse.builder()
                        .id(cc.getId())
                        .finalCost(cc.getFinalCost())
                        .dateCalculated(cc.getDateCalculated())
                        .lgfsCost(cc.getLgfsCost())
                        .agfsCost(cc.getAgfsCost())
                        .sentenceOrderDate(Objects.nonNull(cc.getRepOrderEntity().getSentenceOrderDate())?cc.getRepOrderEntity().getSentenceOrderDate():null)
                        .build()).toList();
    }

}