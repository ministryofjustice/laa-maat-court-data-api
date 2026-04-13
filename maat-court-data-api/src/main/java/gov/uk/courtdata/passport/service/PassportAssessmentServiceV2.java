package gov.uk.courtdata.passport.service;

import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.passport.mapper.PassportAssessmentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.justice.laa.crime.common.model.passported.ApiCreatePassportedAssessmentRequest;
import uk.gov.justice.laa.crime.common.model.passported.ApiCreatePassportedAssessmentResponse;
import uk.gov.justice.laa.crime.common.model.passported.ApiGetPassportedAssessmentResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class PassportAssessmentServiceV2 {

    private final PassportAssessmentPersistenceService passportAssessmentPersistenceService;
    private final PassportAssessmentMapper passportAssessmentMapper;

    
    @Transactional(readOnly = true)
    public ApiGetPassportedAssessmentResponse find(int passportAssessmentId) {
        PassportAssessmentEntity passportAssessmentEntity = passportAssessmentPersistenceService.find(passportAssessmentId);

        return passportAssessmentMapper.toApiGetPassportedAssessmentResponse(passportAssessmentEntity);
    }

    @Transactional
    public ApiCreatePassportedAssessmentResponse create(ApiCreatePassportedAssessmentRequest request) {

        PassportAssessmentEntity maatEntity = passportAssessmentMapper.toPassportAssessmentEntity(request);

        maatEntity = passportAssessmentPersistenceService.create(maatEntity);

        return passportAssessmentMapper.toApiCreatePassportedAssessmentResponse(maatEntity);
    }
}
