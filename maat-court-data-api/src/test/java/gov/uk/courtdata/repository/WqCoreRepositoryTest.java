package gov.uk.courtdata.repository;


import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.WqCoreEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class WqCoreRepositoryTest {

    @Autowired
    private TestEntityDataBuilder testEntityDataBuilder;

    @Autowired
    private WqCoreRepository wqCoreRepository;

    @Test
    public void givenWqCoreDataRepository_whenSaveAndRetrieveEntity_thenOK() {

        //given
        WqCoreEntity wqCoreEntity = testEntityDataBuilder.getWqCoreEntity();
        wqCoreRepository.save(wqCoreEntity);

        // when
        Optional<WqCoreEntity> foundOptionalWqCoreEntity = wqCoreRepository.findById(wqCoreEntity.getTxId());
        WqCoreEntity found = foundOptionalWqCoreEntity.orElse(null);

        // then
        assert found != null;
        assertThat(found.getTxId()).isEqualTo(wqCoreEntity.getTxId());
        assertThat(found.getCaseId()).isEqualTo(wqCoreEntity.getCaseId());
        assertThat(found.getCreatedTime()).isEqualTo(wqCoreEntity.getCreatedTime());
        assertThat(found.getWqStatus()).isEqualTo(wqCoreEntity.getWqStatus());
    }


}
