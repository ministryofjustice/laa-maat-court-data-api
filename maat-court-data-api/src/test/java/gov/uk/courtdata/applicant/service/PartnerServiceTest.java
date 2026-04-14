package gov.uk.courtdata.applicant.service;

import static gov.uk.courtdata.builder.TestEntityDataBuilder.REP_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.applicant.dto.RepOrderApplicantLinksDTO;
import gov.uk.courtdata.applicant.mapper.RepOrderApplicantLinksMapper;
import gov.uk.courtdata.applicant.repository.RepOrderApplicantLinksRepository;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PartnerServiceTest {
    
    private static final Integer PARTNER_ID = 11553844;
    
    @Mock
    private RepOrderApplicantLinksRepository repOrderApplicantLinksRepository;

    @Mock
    private RepOrderApplicantLinksMapper repOrderApplicantLinksMapper;
    
    @InjectMocks
    private PartnerService partnerService;
    
    @Test
    void givenNoApplicantLinks_whenGetPartnerLegacyIdIsCalled_thenNoLegacyPartnerIdIsReturned() {
        when(repOrderApplicantLinksRepository.findAllByRepId(anyInt())).thenReturn(List.of());

        Integer partnerLegacyId = partnerService.getPartnerLegacyId(REP_ID);

        assertThat(partnerLegacyId).isNull();
    }

    @Test
    void givenSingleApplicantLinks_whenGetPartnerLegacyIdIsCalled_thenLegacyPartnerIdIsReturned() {
        var applicantLinksEntity = TestEntityDataBuilder.getRepOrderApplicantLinksEntity();

        RepOrderApplicantLinksDTO repOrderApplicantLinksDto = RepOrderApplicantLinksDTO.builder()
            .repId(REP_ID)
            .partnerApplId(PARTNER_ID)
            .unlinkDate(null)
            .linkDate(LocalDate.now())
            .build();

        when(repOrderApplicantLinksRepository.findAllByRepId(anyInt())).thenReturn(List.of(
            applicantLinksEntity));
        when(repOrderApplicantLinksMapper.mapEntityToDTO(List.of(applicantLinksEntity))).thenReturn(List.of(repOrderApplicantLinksDto));

        Integer partnerLegacyId = partnerService.getPartnerLegacyId(REP_ID);

        assertThat(partnerLegacyId).isEqualTo(applicantLinksEntity.getPartnerApplId());
    }
    
    @Test
    void givenMultipleApplicantLinks_whenGetPartnerLegacyIdIsCalled_thenLegacyPartnerIdIsReturned() {
        var previousApplicantLinksEntity = TestEntityDataBuilder.getRepOrderApplicantLinksEntity();
        var currentApplicantLinksEntity = TestEntityDataBuilder.getRepOrderApplicantLinksEntity();
        currentApplicantLinksEntity.setPartnerApplId(3456);

        RepOrderApplicantLinksDTO previousRepOrderApplicantLinksDto = RepOrderApplicantLinksDTO.builder()
            .repId(REP_ID)
            .partnerApplId(previousApplicantLinksEntity.getPartnerApplId())
            .unlinkDate(LocalDate.now())
            .linkDate(LocalDate.now())
            .build();
        
        RepOrderApplicantLinksDTO currentRepOrderApplicantLinksDto = RepOrderApplicantLinksDTO.builder()
            .repId(REP_ID)
            .partnerApplId(currentApplicantLinksEntity.getPartnerApplId())
            .unlinkDate(null)
            .linkDate(LocalDate.now())
            .build();

        when(repOrderApplicantLinksRepository.findAllByRepId(anyInt())).thenReturn(List.of(
            previousApplicantLinksEntity, currentApplicantLinksEntity));
        when(repOrderApplicantLinksMapper.mapEntityToDTO(List.of(
            previousApplicantLinksEntity, currentApplicantLinksEntity))).thenReturn(
                List.of(previousRepOrderApplicantLinksDto, currentRepOrderApplicantLinksDto));

        Integer partnerLegacyId = partnerService.getPartnerLegacyId(REP_ID);

        assertThat(partnerLegacyId).isEqualTo(currentApplicantLinksEntity.getPartnerApplId());
    }
}
