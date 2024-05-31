package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.dces.request.CreateFdcTestDataRequest;
import gov.uk.courtdata.dces.request.FdcNegativeTestType;
import gov.uk.courtdata.entity.FdcContributionsEntity;
import gov.uk.courtdata.entity.FdcItemsEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import gov.uk.courtdata.repository.CrownCourtProcessingRepository;
import gov.uk.courtdata.repository.FdcContributionsRepository;
import gov.uk.courtdata.repository.FdcItemsRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "sentry", name = "environment", havingValue = "development")
public class FdcContributionsTestService {
    private final FdcItemsRepository fdcItemsRepository;
    private final RepOrderRepository repOrderRepository;
    private final CrownCourtProcessingRepository repOrderCrownCourtOutcomeRepository;
    private final FdcContributionsRepository fdcContributionsRepository;
    private final DebtCollectionRepository debtCollectionRepository;


    public boolean createFdcMergeTestData(CreateFdcTestDataRequest request){
        boolean isNegativeTest = request.isNegativeTest();
        FdcNegativeTestType negativeTestType = isNegativeTest ? request.getNegativeTestType() : null; // only populate if negative is wanted.
        int numberOfTestEntries = request.getNumOfTestEntries();

        // find candidates
        List<Integer> repOrderIds = debtCollectionRepository.getMerge1TestCandidates(numberOfTestEntries);
        if(FdcNegativeTestType.SOD.equals(negativeTestType)){
            LocalDate targetDate = LocalDate.now().plus(3, ChronoUnit.MONTHS);
            setSentenceOrderDate(repOrderIds, targetDate);
        }
        if(FdcNegativeTestType.CCO.equals(negativeTestType)){
            // delete from REP_ORDER_CROWN_COURT_OUTCOMES where rep_id=<P_REP_ID>
            repOrderCrownCourtOutcomeRepository.deleteAllByRepOrder_IdIn(repOrderIds);
        }
        // create Fdc Contributions for each one.
        List<Integer> fdcIds = createFdcContribution(repOrderIds);
        if(FdcNegativeTestType.FDC_STATUS.equals(negativeTestType)){
            //update fdc_contributions set status='SENT'
            //where rep_id = <P_REP_ID>
            List<FdcContributionsEntity> fdcContributionList = fdcContributionsRepository.findByRepOrderEntity_IdIn(repOrderIds);
            fdcContributionList.forEach(fdc->fdc.setStatus(FdcContributionsStatus.SENT));
            fdcContributionsRepository.saveAll(fdcContributionList);
        }

        if(FdcNegativeTestType.FDC_ITEM.equals(negativeTestType)){
            // delete from FDC_ITEMS where fdc_id=<P_FDC_ID>
            fdcItemsRepository.deleteAllByFdcIdIn(fdcIds);
        }
        else{
            createFdcItem(fdcIds);
        }
        return true;
    }

    private void setSentenceOrderDate(List<Integer> repIds, LocalDate date){
        if(Objects.nonNull(repIds) && !repIds.isEmpty()){
            List<RepOrderEntity> repOrders = repOrderRepository.findByIdIn(repIds);
            repOrders.forEach(repOrder->repOrder.setSentenceOrderDate(date));
            repOrderRepository.saveAll(repOrders);
            // update rep_orders set sentence_order_date= add_months(trunc(sysdate),3) where id=<P_REP_ID>
        }
    }

    private void createFdcItem(List<Integer> fdcIdList){
        for(Integer fdcId : fdcIdList ) {
            FdcItemsEntity fdcItem = FdcItemsEntity.builder()
                    .fdcId(fdcId)
                    .dateCreated(LocalDate.now())
                    .userCreated("DCES")
                    .build();
            fdcItemsRepository.save(fdcItem);
        }
    }

    private List<Integer> createFdcContribution(List<Integer> repIdList){
        List<Integer> fdcContributionIdList = new ArrayList<>();
        for ( Integer repId: repIdList){
            RepOrderEntity repOrder = RepOrderEntity.builder()
                    .id(repId)
                    .build();
            FdcContributionsEntity fdcContribution = FdcContributionsEntity.builder()
                    .repOrderEntity(repOrder)
                    .status(FdcContributionsStatus.WAITING_ITEMS)
                    .lgfsComplete(true)
                    .agfsComplete(true)
                    .build();
            fdcContributionsRepository.save(fdcContribution);
            fdcContributionIdList.add(fdcContribution.getId());
        }
        return fdcContributionIdList;
    }

}




