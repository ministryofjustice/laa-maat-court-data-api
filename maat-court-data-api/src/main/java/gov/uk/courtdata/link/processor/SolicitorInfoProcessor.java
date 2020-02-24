package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.CreateLinkDto;
import gov.uk.courtdata.entity.SolicitorEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.repository.SolicitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolicitorInfoProcessor implements Process {

    private final SolicitorRepository solicitorRepository;

    @Override
    public void process(CreateLinkDto saveAndLinkModel) {

        SolicitorMAATDataEntity solicitorMAATDataEntity = saveAndLinkModel.getSolicitorMAATDataEntity();

        SolicitorEntity solicitorEntity = SolicitorEntity.builder()
                .caseId(saveAndLinkModel.getCaseId())
                .txId(saveAndLinkModel.getTxId())
                .firmName(solicitorMAATDataEntity.getAccountName())
                .contactName(solicitorMAATDataEntity.getSolicitorName())
                .address_line1(solicitorMAATDataEntity.getLine1())
                .address_line2(solicitorMAATDataEntity.getLine2())
                .address_line3(solicitorMAATDataEntity.getLine3())
                .address_line4(solicitorMAATDataEntity.getCity())
                .address_line5(solicitorMAATDataEntity.getCounty())
                .email(solicitorMAATDataEntity.getEmail())
                .adminEmail(solicitorMAATDataEntity.getAdminEmail())
                .postCode(solicitorMAATDataEntity.getPostcode())
                .laaOfficeAccount(solicitorMAATDataEntity.getAccountCode())
                .telephone(solicitorMAATDataEntity.getPhone())
                .build();

        solicitorRepository.save(solicitorEntity);
    }
}
