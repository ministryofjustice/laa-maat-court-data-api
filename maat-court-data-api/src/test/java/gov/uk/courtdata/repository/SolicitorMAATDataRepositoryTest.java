package gov.uk.courtdata.repository;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class SolicitorMAATDataRepositoryTest {

    @Autowired
    private TestEntityDataBuilder testEntityDataBuilder;

    @Autowired
    private SolicitorMAATDataRepository solicitorMAATDataRepository;

    @Test
    public void givenSolicitorMAATDataRepository_whenSaveAndRetrieveEntity_thenOK() {

        //given
        SolicitorMAATDataEntity solicitorEntity = testEntityDataBuilder.getSolicitorMAATDataEntity();
        solicitorMAATDataRepository.save(solicitorEntity);

        // when
        Optional<SolicitorMAATDataEntity> foundOptionalSolicitorEntity = solicitorMAATDataRepository.findById(solicitorEntity.getMaatId());
        SolicitorMAATDataEntity found = foundOptionalSolicitorEntity.orElse(null);

        // then
        assert found != null;
        assertThat(found.getMaatId()).isEqualTo(solicitorEntity.getMaatId());
        assertThat(found.getAccountCode()).isEqualTo(solicitorEntity.getAccountCode());
        assertThat(found.getAccountName()).isEqualTo(solicitorEntity.getAccountName());
        assertThat(found.getCmuId()).isEqualTo(solicitorEntity.getCmuId());
    }

}
