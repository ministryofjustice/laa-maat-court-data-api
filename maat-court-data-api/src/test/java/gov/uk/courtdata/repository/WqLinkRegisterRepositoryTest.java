package gov.uk.courtdata.repository;


import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class WqLinkRegisterRepositoryTest {
    @Autowired
    private TestEntityDataBuilder testEntityDataBuilder;

    @Autowired
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @Test
    public void givenWqLinkRegisterDataRepository_whenSaveAndRetrieveEntity_thenOK() {

        //given
        WqLinkRegisterEntity wqLinkRegisterEntity = testEntityDataBuilder.getWqLinkRegisterEntity();
        wqLinkRegisterRepository.save(wqLinkRegisterEntity);

        // when
        Optional<WqLinkRegisterEntity> foundOptionalWqLinkRegisterEntity = wqLinkRegisterRepository.findById(wqLinkRegisterEntity.getCreatedTxId());
        WqLinkRegisterEntity found = foundOptionalWqLinkRegisterEntity.orElse(null);

        // then
        assert found != null;
        assertThat(found.getCreatedTxId()).isEqualTo(wqLinkRegisterEntity.getCreatedTxId());
        assertThat(found.getCaseId()).isEqualTo(wqLinkRegisterEntity.getCaseId());
        assertThat(found.getMaatCat()).isEqualTo(wqLinkRegisterEntity.getMaatCat());
        assertThat(found.getMlrCat()).isEqualTo(wqLinkRegisterEntity.getMlrCat());
        assertThat(found.getMaatId()).isEqualTo(wqLinkRegisterEntity.getMaatId());
    }


}
