package gov.uk.courtdata.repository;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.SessionEntity;
import gov.uk.courtdata.entity.SolicitorEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class SolicitorRepositoryTest {

    @Autowired
    private TestEntityDataBuilder testEntityDataBuilder;

    @Autowired
    private SolicitorRepository solicitorRepository;

    @Test
    public void givenSolicitorDataRepository_whenSaveAndRetrieveEntity_thenOK() {

        //given
        SolicitorEntity solicitorEntity = testEntityDataBuilder.getSolicitorEntity();
        solicitorRepository.save(solicitorEntity);

        // when
        Optional<SolicitorEntity> foundOptionalSolicitorEntity = solicitorRepository.findById(solicitorEntity.getTxId());
        SolicitorEntity found = foundOptionalSolicitorEntity.orElse(null);

        // then
        assert found != null;
        assertThat(found.getTxId()).isEqualTo(solicitorEntity.getTxId());
        assertThat(found.getCaseId()).isEqualTo(solicitorEntity.getCaseId());
        assertThat(found.getLaaOfficeAccount()).isEqualTo(solicitorEntity.getLaaOfficeAccount());
        assertThat(found.getFirmName()).isEqualTo(solicitorEntity.getFirmName());
    }

}
