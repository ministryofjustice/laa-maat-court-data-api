package gov.uk.courtdata.repository;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.UnlinkEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class UnlinkReasonRepositoryTest {

    @Autowired
    private TestEntityDataBuilder testEntityDataBuilder;

    @Autowired
    private UnlinkReasonRepository unlinkReasonRepository;

    @Test
    public void givenUnlinkDataRepository_whenSaveAndRetrieveEntity_thenOK() {

        //given
        UnlinkEntity unlinkEntity = testEntityDataBuilder.getUnlinkEntity();
        unlinkReasonRepository.save(unlinkEntity);

        // when
        Optional<UnlinkEntity> foundOptionalUnlinkEntity = unlinkReasonRepository.findById(unlinkEntity.getTxId());
        UnlinkEntity found = foundOptionalUnlinkEntity.orElse(null);

        // then
        assert found != null;
        assertThat(found.getTxId()).isEqualTo(unlinkEntity.getTxId());
        assertThat(found.getCaseId()).isEqualTo(unlinkEntity.getCaseId());
        assertThat(found.getReasonId()).isEqualTo(unlinkEntity.getReasonId());
        assertThat(found.getOtherReason()).isEqualTo(unlinkEntity.getOtherReason());
    }

}
