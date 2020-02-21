package gov.uk.courtdata.repository;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.DefendantEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class DefendantRepositoryTest {

    @Autowired
    private TestEntityDataBuilder testEntityDataBuilder;

    @Autowired
    private DefendantRepository defendantRepository;

    @Test
    public void givenDefendantEntityRepository_whenSaveAndRetrieveEntity_thenOK() {

        // given
        DefendantEntity defendantEntity = testEntityDataBuilder.getDefendantEntity();
        defendantRepository.save(defendantEntity);

        // when
        Optional<DefendantEntity> foundDefendantOptional = defendantRepository.findById(defendantEntity.getTxId());
        DefendantEntity foundDefendant = foundDefendantOptional.orElse(null);

        // then
        assert foundDefendant != null;
        assertThat(foundDefendant.getCaseId()).isEqualTo(defendantEntity.getCaseId());
        assertThat(foundDefendant.getTxId()).isEqualTo(defendantEntity.getTxId());
        assertThat(foundDefendant.getDateOfBirth()).isEqualTo(defendantEntity.getDateOfBirth());

    }


}
