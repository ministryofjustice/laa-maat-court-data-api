package gov.uk.courtdata.repository;


import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class DefendantMAATDataRepositoryTest {

    @Autowired
    private TestEntityDataBuilder testEntityDataBuilder;

    @Autowired
    private DefendantMAATDataRepository defendantMAATDataRepository;

    @Test
    public void givenDefendantMAATDataEntityRepository_whenSaveAndRetrieveEntity_thenOK() {

        // given
        DefendantMAATDataEntity defendantEntity = testEntityDataBuilder.getDefendantMAATDataEntity();
        defendantMAATDataRepository.save(defendantEntity);

        // when
        Optional<DefendantMAATDataEntity> foundDefendantOptional = defendantMAATDataRepository.findById(defendantEntity.getMaatId());
        DefendantMAATDataEntity foundDefendant = foundDefendantOptional.orElse(null);

        // then
        assert foundDefendant != null;
        assertThat(foundDefendant.getMaatId()).isEqualTo(defendantEntity.getMaatId());
        assertThat(foundDefendant.getDob()).isEqualTo(defendantEntity.getDob());
        assertThat(foundDefendant.getLibraId()).isEqualTo(defendantEntity.getLibraId());


    }
}
