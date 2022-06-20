package gov.uk.courtdata.link.processor;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.SolicitorEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.repository.SolicitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@XRayEnabled
@RequiredArgsConstructor
public class SolicitorInfoProcessor implements Process {

    private final SolicitorRepository solicitorRepository;

    @Override
    public void process(CourtDataDTO courtDataDTO) {

        SolicitorMAATDataEntity solicitorMAATDataEntity = courtDataDTO.getSolicitorMAATDataEntity();

        SolicitorEntity solicitorEntity = SolicitorEntity.builder()
                .caseId(courtDataDTO.getCaseId())
                .txId(courtDataDTO.getTxId())
                .firmName(solicitorMAATDataEntity.getAccountName())
                .contactName(solicitorMAATDataEntity.getSolicitorName())
                .addressLine1(solicitorMAATDataEntity.getLine1())
                .addressLine2(solicitorMAATDataEntity.getLine2())
                .addressLine3(solicitorMAATDataEntity.getLine3())
                .addressLine4(solicitorMAATDataEntity.getCity())
                .addressLine5(solicitorMAATDataEntity.getCounty())
                .email(solicitorMAATDataEntity.getEmail())
                .adminEmail(solicitorMAATDataEntity.getAdminEmail())
                .postCode(solicitorMAATDataEntity.getPostcode())
                .laaOfficeAccount(solicitorMAATDataEntity.getAccountCode())
                .telephone(solicitorMAATDataEntity.getPhone())
                .build();

        solicitorRepository.save(solicitorEntity);
    }
}
