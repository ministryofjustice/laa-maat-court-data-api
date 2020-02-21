package gov.uk.courtdata.repository;


import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.ResultEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class ResultRepositoryTest {

    @Autowired
    private TestEntityDataBuilder testEntityDataBuilder;

    @Autowired
    private ResultRepository resultRepository;

    @Test
    public void givenResultDataRepository_whenSaveAndRetrieveEntity_thenOK() {

        //given
        ResultEntity resultEntity = testEntityDataBuilder.getResultEntity();
        resultRepository.save(resultEntity);

        // when
        Optional<ResultEntity> foundOptionalResultEntity = resultRepository.findById(resultEntity.getTxId());
        ResultEntity found = foundOptionalResultEntity.orElse(null);

        // then
        assert found != null;
        assertThat(found.getResultCode()).isEqualTo(resultEntity.getResultCode());
        assertThat(found.getCaseId()).isEqualTo(resultEntity.getCaseId());
        assertThat(found.getResultShortTitle()).isEqualTo(resultEntity.getResultShortTitle());
        assertThat(found.getWqResult()).isEqualTo(resultEntity.getWqResult());

    }

}
