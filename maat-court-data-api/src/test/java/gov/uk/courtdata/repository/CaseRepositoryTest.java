
package gov.uk.courtdata.repository;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.CaseEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class CaseRepositoryTest {


    @Autowired
    private CaseRepository caseRepository;
    @Autowired
    private TestEntityDataBuilder testEntityDataBuilder;

    @Test
    public void givenCaseEntityRepository_whenSaveAndRetrieveEntity_thenOK() {

        // given
        CaseEntity caseEntity = testEntityDataBuilder.getCaseEntity();
        caseRepository.save(caseEntity);

        // when
        Optional<CaseEntity> foundCaseOptionalEntity = caseRepository.findById(caseEntity.getTxId());
        CaseEntity foundCase = foundCaseOptionalEntity.orElse(null);

        // then
        assert foundCase != null;
        assertThat(foundCase.getCaseId()).isEqualTo(caseEntity.getCaseId());
        assertThat(foundCase.getTxId()).isEqualTo(caseEntity.getTxId());
        assertThat(foundCase.getAsn()).isEqualTo(caseEntity.getAsn());
        assertThat(foundCase.getDocLanguage()).isEqualTo(caseEntity.getDocLanguage());

    }


}

