package gov.uk.courtdata.converters;

import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.enums.*;
import gov.uk.courtdata.repository.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
public class EnumConverterTest {

    @Autowired
    private FinancialAssessmentDetailsRepository financialAssessmentDetailsRepository;

    @Autowired
    private HardshipReviewRepository hardshipReviewRepository;

    @Autowired
    private HardshipReviewDetailRepository hardshipReviewDetailRepository;

    @Autowired
    private HardshipReviewDetailReasonRepository hardshipReviewDetailReasonRepository;

    @Autowired
    private HardshipReviewProgressRepository hardshipReviewProgressRepository;

    @Test
    public void givenFinancialAssessmentDetailEntity_whenEntityIsSaved_thenEnumsArePersisted() {

        FinancialAssessmentDetailEntity mockEntity = FinancialAssessmentDetailEntity.builder()
                .dateCreated(LocalDateTime.now())
                .userCreated("test-s")
                .applicantFrequency(Frequency.TWO_WEEKLY)
                .partnerFrequency(Frequency.FOUR_WEEKLY)
                .build();

        FinancialAssessmentDetailEntity returned = financialAssessmentDetailsRepository.save(mockEntity);
        assertThat(returned.getApplicantFrequency()).isEqualTo(Frequency.TWO_WEEKLY);
        assertThat(returned.getPartnerFrequency()).isEqualTo(Frequency.FOUR_WEEKLY);
    }

    @Test
    public void givenHardshipReviewEntity_whenEntityIsSaved_thenEnumsArePersisted() {

        HardshipReviewEntity mockEntity = HardshipReviewEntity.builder()
                .dateCreated(LocalDateTime.now())
                .userCreated("test-s")
                .status(HardshipReviewStatus.IN_PROGRESS)
                .build();

        HardshipReviewEntity returned = hardshipReviewRepository.save(mockEntity);
        assertThat(returned.getStatus()).isEqualTo(HardshipReviewStatus.IN_PROGRESS);
    }

    @Test
    public void givenHardshipReviewDetailEntity_whenEntityIsSaved_thenEnumArePersisted() {

        HardshipReviewDetailEntity mockEntity = HardshipReviewDetailEntity.builder()
                .dateCreated(LocalDateTime.now())
                .userCreated("test-s")
                .detailType(HardshipReviewDetailType.INCOME)
                .detailCode(HardshipReviewDetailCode.DEBTS)
                .frequency(Frequency.ANNUALLY)
                .build();

        HardshipReviewDetailEntity returned = hardshipReviewDetailRepository.save(mockEntity);
        assertThat(returned.getFrequency()).isEqualTo(Frequency.ANNUALLY);
        assertThat(returned.getDetailCode()).isEqualTo(HardshipReviewDetailCode.DEBTS);
        assertThat(returned.getDetailType()).isEqualTo(HardshipReviewDetailType.INCOME);

    }

    @Test
    public void givenHardshipReviewDetailReasonEntity_whenEntityIsSaved_thenEnumArePersisted() {

        HardshipReviewDetailReasonEntity mockEntity = HardshipReviewDetailReasonEntity.builder()
                .id(1000)
                .dateCreated(LocalDateTime.now())
                .userCreated("test-s")
                .detailType(HardshipReviewDetailType.EXPENDITURE)
                .build();

        HardshipReviewDetailReasonEntity returned = hardshipReviewDetailReasonRepository.save(mockEntity);
        assertThat(returned.getDetailType()).isEqualTo(HardshipReviewDetailType.EXPENDITURE);

    }

    @Test
    public void givenHardshipReviewProgressEntity_whenEntityIsSaved_thenEnumArePersisted() {

        HardshipReviewProgressEntity mockEntity = HardshipReviewProgressEntity.builder()
                .hardshipReviewId(1000)
                .dateCreated(LocalDateTime.now())
                .userCreated("test-s")
                .dateRequested(LocalDateTime.now())
                .progressAction(HardshipReviewProgressAction.ADDITIONAL_EVIDENCE)
                .progressResponse(HardshipReviewProgressResponse.FURTHER_RECEIVED)
                .build();

        HardshipReviewProgressEntity returned = hardshipReviewProgressRepository.save(mockEntity);
        assertThat(returned.getProgressAction()).isEqualTo(HardshipReviewProgressAction.ADDITIONAL_EVIDENCE);
        assertThat(returned.getProgressResponse()).isEqualTo(HardshipReviewProgressResponse.FURTHER_RECEIVED);
    }
}
