package gov.uk.courtdata.applicant.service;

import static gov.uk.courtdata.builder.TestEntityDataBuilder.REP_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.applicant.repository.RepOrderApplicantLinksRepository;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PartnerResolverTest {
    
    @Mock
    private RepOrderApplicantLinksRepository repOrderApplicantLinksRepository;
    
    @InjectMocks
    private PartnerResolver partnerResolver;
    
    @Test
    void givenNoApplicantLinks_whenGetPartnerLegacyIdIsCalled_thenNoLegacyPartnerIdIsReturned() {
        when(repOrderApplicantLinksRepository.findFirstByRepIdAndLinkDateIsNotNullAndUnlinkDateIsNull(anyInt())).thenReturn(Optional.empty());

        Integer partnerLegacyId = partnerResolver.getPartnerLegacyId(REP_ID);

        assertThat(partnerLegacyId).isNull();
    }

    @Test
    void givenSingleApplicantLinks_whenGetPartnerLegacyIdIsCalled_thenLegacyPartnerIdIsReturned() {
        var applicantLinksEntity = TestEntityDataBuilder.getRepOrderApplicantLinksEntity();

        when(repOrderApplicantLinksRepository.findFirstByRepIdAndLinkDateIsNotNullAndUnlinkDateIsNull(anyInt())).thenReturn(
            Optional.of(applicantLinksEntity));

        Integer partnerLegacyId = partnerResolver.getPartnerLegacyId(REP_ID);

        assertThat(partnerLegacyId).isEqualTo(applicantLinksEntity.getPartnerApplId());
    }
}
