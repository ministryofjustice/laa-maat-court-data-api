package gov.uk.courtdata.builder;

import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.enums.WQType;
import gov.uk.courtdata.model.hearing.CCOutComeData;
import gov.uk.courtdata.model.hearing.HearingResulted;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static gov.uk.courtdata.constants.CourtDataConstants.*;

@Component
public class TestEntityDataBuilder {



    public RepOrderCPDataEntity getRepOrderEntity() {
        return RepOrderCPDataEntity.builder()
                .repOrderId(1234)
                .caseUrn("caseurn1")
                .dateCreated(LocalDate.now())
                .userCreated("user")
                .dateModified(LocalDateTime.now())
                .build();
    }

    public RepOrderEntity getRepOrder() {
        return RepOrderEntity.builder()
                .id(1234)
                .dateModified(LocalDateTime.now())
                .build();
    }

    public WqLinkRegisterEntity getWqLinkRegisterEntity() {
        return WqLinkRegisterEntity.builder()
                .caseId(345)
                .createdTxId(123)
                .proceedingId(345)
                .cjsAreaCode("16")
                .maatCat(1)
                .mlrCat(1)
                .maatId(1234)
                .build();
    }

    public DefendantMAATDataEntity getDefendantMAATDataEntity() {
        return DefendantMAATDataEntity.builder()
                .maatId(1234)
                .firstName("T First Name")
                .lastName("T Last Name")
                .libraId("libra id")
                .dob("07041960")
                .build();
    }

    public SolicitorMAATDataEntity getSolicitorMAATDataEntity() {
        return SolicitorMAATDataEntity.builder()
                .maatId(1234)
                .accountCode("Acc")
                .accountName("Acc Name")
                .cmuId(123)
                .build();
    }

    public RepOrderCPDataEntity getRepOrderCPDataEntity() {
        return RepOrderCPDataEntity.builder()
                .repOrderId(1234)
                .caseUrn("testCaseURN")
                .build();
    }

    public CrownCourtOutComeEntity getCrownCourtOutComeEntity () {
        return CrownCourtOutComeEntity.builder()
                .outcome("SUCCESSFUL")
                .build();
    }

    public HearingResulted getHearingResulted() {
        return HearingResulted.builder()
                .caseUrn("")
                .ccOutComeData(getCCOutComeData())
                .maatId(1234)
                .build();
    }

    public CCOutComeData getCCOutComeData () {
        return CCOutComeData.builder()
                .ccooOutcome("SUCCESSFUL")
                .appealType("ACN")
                .benchWarrantIssuedYn("Y")
                .appealType("ACN")
                .ccImprisioned("Y")
                .crownCourtCode("453")
                .build();
    }
    public AppealTypeEntity getAppealTypeEntity () {
        return AppealTypeEntity.builder()
                .code("ACN")
                .build();
    }

    public XLATOffenceEntity getXLATOffenceEntity() {
        String offenceCode = "CD98072";
        return XLATOffenceEntity.builder()
                .offenceCode(offenceCode)
                .parentCode(offenceCode.length() >= 4 ? offenceCode.substring(0, 4) : offenceCode)
                .codeMeaning(UNKNOWN_OFFENCE)
                .applicationFlag(G_NO)
                .codeStart(LocalDate.now())
                .createdUser(AUTO_USER)
                .createdDate(LocalDate.now())
                .build();
    }

    public XLATResultEntity getXLATResultEntity () {
        int resultCode =4600;
        return XLATResultEntity.builder()
                .cjsResultCode(resultCode)
                .resultDescription(RESULT_CODE_DESCRIPTION)
                .englandAndWales(YES)
                .wqType(WQType.USER_INTERVENTIONS_QUEUE.value())
                .createdUser(AUTO_USER)
                .createdDate(LocalDate.now())
                .build();
    }
}
