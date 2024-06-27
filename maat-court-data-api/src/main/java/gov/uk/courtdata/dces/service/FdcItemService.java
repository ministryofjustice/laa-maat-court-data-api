package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.entity.FdcItemsEntity;
import gov.uk.courtdata.repository.FdcItemsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FdcItemService {

    private final FdcItemsRepository fdcItemsRepository;


    public void deleteFdcItem(Integer fdcItemId, Integer fdcId) {
        log.info("Deleting FdcItem with fdcItemId: {} and fdcId: {}", fdcItemId, fdcId);
        fdcItemsRepository.deleteById(fdcItemId);
    }
    public void createFdcItem() {
        log.info("Creating FdcItem");

        fdcItemsRepository.save(FdcItemsEntity.builder().build());
    }

}
