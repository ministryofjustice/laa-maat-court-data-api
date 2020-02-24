
package gov.uk.courtdata.link;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveAndLinkProcessor {

//TODO: To be removed along with TestController.

//    private final Gson gson;
//    private final SaveAndLinkImpl saveAndLinkImpl;
//    private final SolicitorMAATDataRepository solicitorMAATDataRepository;
//    private final DefendantMAATDataRepository defendantMAATDataRepository;
//
//
//
//    public void process(String linkRequestPayload) {
//
//        mapRequestPayload(linkRequestPayload);
//
//    }
//
//
//    private void mapRequestPayload(String linkRequestPayload) {
//        SaveAndLinkModel saveAndLinkModel = new SaveAndLinkModel();
//        CaseDetails caseDetails = gson.fromJson(linkRequestPayload, CaseDetails.class);
//        mapSaveAndLinkModel(saveAndLinkModel, caseDetails);
//        saveAndLinkImpl.execute(saveAndLinkModel);
//
//    }
//
//    private void mapSaveAndLinkModel(SaveAndLinkModel saveAndLinkModel, CaseDetails caseDetails) {
//        Integer maatId = caseDetails.getMaatId();
//        saveAndLinkModel.setCaseDetails(caseDetails);
//        mapSolicitorMAATDataInfo(maatId, saveAndLinkModel);
//        mapDefendantMAATDataInfo(maatId, saveAndLinkModel);
//
//    }
//
//    private void mapDefendantMAATDataInfo(Integer maatId, SaveAndLinkModel saveAndLinkModel) {
//        Optional<DefendantMAATDataEntity> defendantDetails = defendantMAATDataRepository.findBymaatId(maatId);
//        DefendantMAATDataEntity defendantMAATDataEntity = defendantDetails.orElse(null);
//        saveAndLinkModel.setDefendantMAATDataEntity(defendantMAATDataEntity);
//
//    }
//
//
//    private void mapSolicitorMAATDataInfo(Integer maatId, SaveAndLinkModel saveAndLinkModel) {
//        Optional<SolicitorMAATDataEntity> solicitorDetails = solicitorMAATDataRepository.findBymaatId(maatId);
//        SolicitorMAATDataEntity solicitorMAATData = solicitorDetails.orElse(null);
//        saveAndLinkModel.setSolicitorMAATDataEntity(solicitorMAATData);
//
//    }

}




