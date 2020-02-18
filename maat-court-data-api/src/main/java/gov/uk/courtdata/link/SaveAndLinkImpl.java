package gov.uk.courtdata.link;

import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.model.*;
import gov.uk.courtdata.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static gov.uk.courtdata.constants.CourtDataConstants.*;

@Component
@RequiredArgsConstructor
public class SaveAndLinkImpl {

    private final CaseRepository caseRepository;
    private final WqCoreRepository wqCoreRepository;
    private final WqLinkRegisterRepository wqLinkRegisterRepository;
    private final SolicitorRepository solicitorRepository;
    private final ProceedingRepository proceedingRepository;
    private final DefendantRepository defendantRepository;
    private final SessionRepository sessionRepository;
    private final OffenceRepository offenceRepository;
    private final ResultRepository resultRepository;


    public void execute(SaveAndLinkModel saveAndLinkModel) {

        processCaseInfo(saveAndLinkModel);
        processWQCoreInfo(saveAndLinkModel);
        processWQLinkRegister(saveAndLinkModel);
        processSolicitor(saveAndLinkModel);
        processProceedings(saveAndLinkModel);
        processDefendant(saveAndLinkModel);
        processSessionInfo(saveAndLinkModel);
        processOffences(saveAndLinkModel);
        processResults(saveAndLinkModel);
    }

    private void processResults(SaveAndLinkModel saveAndLinkModel) {
        saveAndLinkModel.getCaseDetails().getDefendant().getOffences()
                .forEach(offence -> buildResultsList(offence.getResults(), saveAndLinkModel));
    }

    private void buildResultsList(List<Result> resultList, SaveAndLinkModel saveAndLinkModel) {

        List<ResultEntity> resultEntityList = resultList
                .stream()
                .map(result -> buildResult(result, saveAndLinkModel))
                .collect(Collectors.toList());
        resultRepository.saveAll(resultEntityList);

    }

    private ResultEntity buildResult(Result result, SaveAndLinkModel saveAndLinkModel) {
        return ResultEntity.builder()
                .caseId(saveAndLinkModel.getCaseId())
                .txId(saveAndLinkModel.getTxId())
                .asn(saveAndLinkModel.getCaseDetails().getAsn())
                .courtLocation(result.getCourtLocation())
                .asnSeq(result.getAsnSeq())
                .contactName(result.getContactName())
                .firmName(result.getFirstName())
                .laaOfficeAccount(result.getLaaOfficeAccount())
                .legalAidWithdrawalDate(getDate(result.getLegalAidWithdrawalDate()))
                .nextHearingDate(getDate(result.getNextHearingDate()))
                .nextHearingLocation(result.getNextHearingLocation())
                .receivedDate(result.getReceivedDate())
                .resultCode(result.getResultCode())
                .resultCodeQualifiers(result.getResultCodeQualifiers())
                .resultShortTitle(result.getResultShortTitle())
                .sessionValidateDate(getDate(result.getSessionValidateDate()))
                .legalAidWithdrawalDate(getDate(result.getLegalAidWithdrawalDate()))
                .dateOfHearing(getDate(result.getDateOfHearing()))
                .wqResult(G_NO)
                .build();
    }

    private void processOffences(SaveAndLinkModel saveAndLinkModel) {
        List<OffenceEntity> offenceEntityList = saveAndLinkModel.getCaseDetails().getDefendant().getOffences()
                .stream()
                .map(offence -> buildOffences(offence, saveAndLinkModel))
                .collect(Collectors.toList());
        offenceRepository.saveAll(offenceEntityList);

    }

    private OffenceEntity buildOffences(Offence offence, SaveAndLinkModel saveAndLinkModel) {
        return OffenceEntity.builder()
                .caseId(saveAndLinkModel.getCaseId())
                .txId(saveAndLinkModel.getTxId())
                .asnSeq(offence.getAsnSeq())
                .offenceCode(offence.getOffenceCode())
                .offenceClassification(offence.getOffenceClassification())
                .legalAidStatus(offence.getLegalAidStatus())
                .legalAidStatusDate(offence.getLegalAidStatusDate())
                .legalaidReason(offence.getLegalAidReason())
                .offenceDate(getDate(offence.getOffenceDate()))
                .offenceShortTitle(offence.getOffenceShortTitle())
                .modeOfTrial(offence.getModeOfTrail())
                .offenceWording(offence.getOffenceWording())
                .iojDecision(PENDING_IOJ_DECISION)
                .wqOffence(G_NO)
                .applicationFlag(0) // Needs to check
                .build();
    }

    private void processSessionInfo(SaveAndLinkModel saveAndLinkModel) {

        List<SessionEntity> sessionEntityList = saveAndLinkModel.getCaseDetails().getSessions()
                .stream()
                .map(s -> buildSession(s, saveAndLinkModel))
                .collect(Collectors.toList());
        sessionRepository.saveAll(sessionEntityList);
    }

    private SessionEntity buildSession(Session session, SaveAndLinkModel saveAndLinkModel) {
        return SessionEntity.builder()
                .caseId(saveAndLinkModel.getCaseId())
                .txId(saveAndLinkModel.getTxId())
                .dateOfHearing(LocalDate.parse(session.getDateOfHearing()))
                .courtLocation(session.getCourtLocation())
                .postHearingCustody(session.getPostHearingCustody() != null ? session.getPostHearingCustody() : DEFAULT_HEARING_CUS_STATUS)
                .sessionvalidatedate(getDate(session.getSessionvalidateddate()))
                .build();
    }


    private void processDefendant(SaveAndLinkModel saveAndLinkModel) {
        DefendantMAATDataEntity defendantMAATDataEntity = saveAndLinkModel.getDefendantMAATDataEntity();
        CaseDetails caseDetails = saveAndLinkModel.getCaseDetails();
        Defendant defendant = caseDetails.getDefendant();

        DefendantEntity defendantEntity = DefendantEntity.builder()
                .caseId(saveAndLinkModel.getCaseId())
                .txId(saveAndLinkModel.getTxId())
                .forename(defendant.getForename())
                .surname(defendant.getSurname())
                .organisation(defendant.getOrganization())
                .dateOfBirth(getDate(defendant.getDateOfBirth()))
                .address_line1(defendant.getAddress_line1())
                .address_line2(defendant.getAddress_line2())
                .address_line3(defendant.getAddress_line3())
                .address_line4(defendant.getAddress_line4())
                .address_line5(defendant.getAddress_line5())
                .postCode(defendant.getPostcode())
                .nino(defendant.getNino())
                .telephoneHome(defendant.getTelephoneHome())
                .telephoneWork(defendant.getTelephoneWork())
                .telephoneMobile(defendant.getTelephoneMobile())
                .email1(defendant.getEmail1())
                .email2(defendant.getEmail2())
                .pline1(defendantMAATDataEntity.getPline1())
                .pline2(defendantMAATDataEntity.getPline2())
                .pline3(defendantMAATDataEntity.getPline3())
                .pcity(defendantMAATDataEntity.getPcity())
                .ppostcode(defendantMAATDataEntity.getPpostcode())
                .pcountry(defendantMAATDataEntity.getPcountry())
                .pcountry(defendantMAATDataEntity.getPcountry())
                .useSol(defendantMAATDataEntity.getUseSol())
                .searchType(SEARCH_TYPE_0)
                .datasource(CREATE_LINK)
                .build();
        defendantRepository.save(defendantEntity);

    }

    private void processProceedings(SaveAndLinkModel saveAndLinkModel) {


        final CaseDetails caseDetails = saveAndLinkModel.getCaseDetails();
        ProceedingEntity proceedingEntity = ProceedingEntity.builder()
                .maatId(caseDetails.getMaatId())
                .proceedingId(saveAndLinkModel.getProceedingId())
                .createdTxid(saveAndLinkModel.getTxId())
                .createdUser(caseDetails.getCreatedUser())
                .build();
        proceedingRepository.save(proceedingEntity);
    }

    private void processSolicitor(SaveAndLinkModel saveAndLinkModel) {

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

    private void processWQLinkRegister(SaveAndLinkModel saveAndLinkModel) {

        CaseDetails caseDetails = saveAndLinkModel.getCaseDetails();
        int maatCat = saveAndLinkModel.getSolicitorMAATDataEntity().getCmuId();
        final WqLinkRegisterEntity wqLinkRegisterEntity = WqLinkRegisterEntity.builder()
                .createdTxId(saveAndLinkModel.getTxId())
                .createdDate(LocalDate.now())
                .createdUserId(caseDetails.getCreatedUser())
                .caseId(saveAndLinkModel.getCaseId())
                .libraId("123456")
                .maatId(caseDetails.getMaatId())
                .cjsAreaCode(caseDetails.getCjsAreaCode())
                .cjsLocation(caseDetails.getCjsLocation())
                .proceedingId(saveAndLinkModel.getProceedingId())
                .maatCat(maatCat)
                .mlrCat(maatCat)
                .build();
        wqLinkRegisterRepository.save(wqLinkRegisterEntity);
    }

    private void processWQCoreInfo(SaveAndLinkModel saveAndLinkModel) {

        CaseDetails caseDetails = saveAndLinkModel.getCaseDetails();
        WqCoreEntity wqCoreEntity = WqCoreEntity.builder()
                .txId(saveAndLinkModel.getTxId())
                .caseId(saveAndLinkModel.getCaseId())
                .createdTime(LocalDate.now())
                .createdUserId(caseDetails.getCreatedUser())
                .wqType(WQ_CREATION_EVENT)
                .wqStatus(WQ_WAITING_STATUS)
                .build();
        wqCoreRepository.save(wqCoreEntity);

    }

    private void processCaseInfo(SaveAndLinkModel saveAndLinkModel) {
        CaseDetails caseDetails = saveAndLinkModel.getCaseDetails();
        final CaseEntity caseEntity = CaseEntity.builder()
                .txId(saveAndLinkModel.getTxId())
                .caseId(saveAndLinkModel.getCaseId())
                .asn(caseDetails.getAsn())
                .cjsAreaCode(caseDetails.getCjsAreaCode())
                .inactive(caseDetails.isActive() ? NO : YES)
                .libraCreationDate(LocalDate.parse(caseDetails.getCaseCreationDate()))
                .docLanguage(caseDetails.getDocLanguage())
                .proceedingId(saveAndLinkModel.getProceedingId())
                .build();

        caseRepository.save(caseEntity);
    }

    private LocalDate getDate(String date) {
        return date != null ? LocalDate.parse(date) : null;
    }
}
