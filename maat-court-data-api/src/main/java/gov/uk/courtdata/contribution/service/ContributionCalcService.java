package gov.uk.courtdata.contribution.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.contribution.dto.ContributionCalcParametersDTO;
import gov.uk.courtdata.contribution.mapper.ContributionsCalcParametersMapper;
import gov.uk.courtdata.entity.ContribCalcParametersEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.repository.ContribCalcParametersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@XRayEnabled
public class ContributionCalcService {

    private final ContribCalcParametersRepository contribCalcParametersRepository;
    private final ContributionsCalcParametersMapper contributionsCalcParametersMapper;

    @Transactional(readOnly = true)
    public ContributionCalcParametersDTO getContributionCalcParameters(final String effectiveDate) {
        ContribCalcParametersEntity contribCalcParametersEntity = contribCalcParametersRepository
                .findCurrentContribCalcParameters(effectiveDate);
        if (contribCalcParametersEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("No Contribution Calc Parameters found with the effective date: %s",
                    effectiveDate));
        }
        return contributionsCalcParametersMapper.contributionsEntityToContributionsDTO(contribCalcParametersEntity);
    }
}
