package gov.uk.courtdata.builder;

import gov.uk.courtdata.entity.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class TestEntityDataBuilder {

    public CaseEntity getCaseEntity() {

        return CaseEntity.builder()
                .txId(113)
                .caseId(59)
                .cjsAreaCode("16")
                .asn("123")
                .docLanguage("EN")
                .build();
    }

    public DefendantEntity getDefendantEntity() {
        return DefendantEntity.builder()
                .txId(113)
                .caseId(59)
                .nino("1224")
                .dateOfBirth(LocalDate.now())
                .build();

    }

    public OffenceEntity getOffenceEntity() {
        return OffenceEntity.builder()
                .txId(1234)
                .offenceCode("FOUL")
                .wqOffence(2)
                .asnSeq("123")
                .offenceWording("Offence Wording")
                .build();
    }

    public RepOrderEntity getRepOrderEntity() {
        return RepOrderEntity.builder()
                .repOrderId(1234)
                .caseUrn("caseurn1")
                .dateCreated(LocalDate.now())
                .userCreated("user")
                .dateModified(LocalDateTime.now())
                .build();
    }

    public ProceedingEntity getProceedingEntity() {
        return ProceedingEntity.builder()
                .createdTxid(123)
                .maatId(345)
                .createdUser("testUser")
                .proceedingId(1234)
                .build();
    }

    public ResultEntity getResultEntity() {
        return ResultEntity.builder()
                .wqResult(0)
                .resultCode("3606")
                .txId(123)
                .caseId(345)
                .resultShortTitle("Result Title")
                .build();
    }

    public SessionEntity getSessionEntity() {
        return SessionEntity.builder()
                .txId(123)
                .caseId(345)
                .postHearingCustody("R")
                .courtLocation("London")
                .dateOfHearing(LocalDate.now())
                .build();
    }

    public SolicitorEntity getSolicitorEntity() {
        return SolicitorEntity.builder()
                .txId(123)
                .caseId(345)
                .laaOfficeAccount("Sol Account")
                .firmName("Test Firm Name")
                .email("abac@abo")
                .build();
    }

    public UnlinkEntity getUnlinkEntity() {
        return UnlinkEntity.builder()
                .txId(123)
                .caseId(345)
                .otherReason("test reason")
                .reasonId(1)
                .build();
    }

    public WqCoreEntity getWqCoreEntity() {
        return WqCoreEntity.builder()
                .txId(123)
                .caseId(345)
                .createdTime(LocalDate.now())
                .wqStatus(1)
                .maatUpdateStatus(1)
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

}
