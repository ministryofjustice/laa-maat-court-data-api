package gov.uk.courtdata.repository;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.ProceedingEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class ProceedingRepositoryTest {


    @Autowired
    private ProceedingRepository proceedingRepository;
    @Autowired
    private TestEntityDataBuilder testEntityDataBuilder;

    @Test
    public void givenProceedingEntityRepository_whenSaveAndRetrieveEntity_thenOK() {

        // given
        ProceedingEntity proceedingEntity = testEntityDataBuilder.getProceedingEntity();
        proceedingRepository.save(proceedingEntity);

        // when
        Optional<ProceedingEntity> foundProceedingOptionalEntity = proceedingRepository.findById(proceedingEntity.getCreatedTxid());
        ProceedingEntity foundProceeding = foundProceedingOptionalEntity.orElse(null);

        // then
        assert foundProceeding != null;
        assertThat(foundProceeding.getCreatedTxid()).isEqualTo(proceedingEntity.getCreatedTxid());
        assertThat(foundProceeding.getMaatId()).isEqualTo(proceedingEntity.getMaatId());
        assertThat(foundProceeding.getProceedingId()).isEqualTo(proceedingEntity.getProceedingId());
        assertThat(foundProceeding.getCreatedUser()).isEqualTo(proceedingEntity.getCreatedUser());

    }
}
