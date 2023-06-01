package gov.uk.courtdata.contribution.service;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.contribution.dto.ContributionCalcParametersDTO;
import gov.uk.courtdata.contribution.mapper.ContributionsCalcParametersMapper;
import gov.uk.courtdata.entity.ContribCalcParametersEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.repository.ContribCalcParametersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static gov.uk.courtdata.builder.TestModelDataBuilder.EFFECTIVE_DATE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContributionCalcServiceTest {

    @InjectMocks
    private ContributionCalcService contributionCalcService;
    @Mock
    private ContribCalcParametersRepository contribCalcParametersRepository;
    @Mock
    private ContributionsCalcParametersMapper contributionsCalcParametersMapper;

    @Test
    void givenValidEffectiveDate_whenGetContributionCalcParametersIsInvoked_thenContributionCalcParametersDTOIsReturned() {
        when(contribCalcParametersRepository.findCurrentContribCalcParameters(EFFECTIVE_DATE))
                .thenReturn(new ContribCalcParametersEntity());
        ContributionCalcParametersDTO expected = TestModelDataBuilder.getContributionCalcParametersDTO();
        when(contributionsCalcParametersMapper.mapEntityToDTO(any(ContribCalcParametersEntity.class)))
                .thenReturn(expected);

        ContributionCalcParametersDTO contributionCalcParametersDTO = contributionCalcService.getContributionCalcParameters(EFFECTIVE_DATE);

        verify(contribCalcParametersRepository).findCurrentContribCalcParameters(EFFECTIVE_DATE);
        assertThat(contributionCalcParametersDTO).isEqualTo(expected);
    }

    @Test
    void givenInvalidEffectiveDate_whenGetContributionCalcParametersIsInvoked_thenErrorIsThrown() {
        assertThatThrownBy(() -> contributionCalcService.getContributionCalcParameters(EFFECTIVE_DATE))
                .isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("No Contribution Calc Parameters found with the effective date: " + EFFECTIVE_DATE);
    }
}
