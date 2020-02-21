package gov.uk.courtdata.repository;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.SessionEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class SessionRepositoryTest {

    @Autowired
    private TestEntityDataBuilder testEntityDataBuilder;

    @Autowired
    private SessionRepository sessionRepository;

    @Test
    public void givenSessionDataRepository_whenSaveAndRetrieveEntity_thenOK() {

        //given
        SessionEntity sessionEntity = testEntityDataBuilder.getSessionEntity();
        sessionRepository.save(sessionEntity);

        // when
        Optional<SessionEntity> foundOptionalSessionEntity = sessionRepository.findById(sessionEntity.getTxId());
        SessionEntity found = foundOptionalSessionEntity.orElse(null);

        // then
        assert found != null;
        assertThat(found.getTxId()).isEqualTo(sessionEntity.getTxId());
        assertThat(found.getCaseId()).isEqualTo(sessionEntity.getCaseId());
        assertThat(found.getPostHearingCustody()).isEqualTo(sessionEntity.getPostHearingCustody());

    }

}
