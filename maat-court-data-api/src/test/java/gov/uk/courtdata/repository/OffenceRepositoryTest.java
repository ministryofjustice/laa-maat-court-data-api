
package gov.uk.courtdata.repository;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.OffenceEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class OffenceRepositoryTest {


    @Autowired
    private TestEntityDataBuilder testEntityDataBuilder;

    @Autowired
    private OffenceRepository offenceRepository;

    @Test
    public void givenOffenceEntityRepository_whenSaveAndRetrieveEntity_thenOK() {

        // given
        OffenceEntity offenceEntity = testEntityDataBuilder.getOffenceEntity();
        offenceRepository.save(offenceEntity);


        // when
        Optional<OffenceEntity> foundOffenceOptionalEntity = offenceRepository.findById(offenceEntity.getTxId());
        OffenceEntity foundOffence = foundOffenceOptionalEntity.orElse(null);

        // then
        assert foundOffence != null;
        assertThat(foundOffence.getCaseId()).isEqualTo(offenceEntity.getCaseId());
        assertThat(foundOffence.getTxId()).isEqualTo(offenceEntity.getTxId());
        assertThat(foundOffence.getAsnSeq()).isEqualTo(offenceEntity.getAsnSeq());
        assertThat(foundOffence.getOffenceWording()).isEqualTo(offenceEntity.getOffenceWording());

    }


}

