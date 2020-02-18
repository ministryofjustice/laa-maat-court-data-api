
package gov.uk.courtdata.link;


import com.google.gson.Gson;
import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.SaveAndLinkModel;
import gov.uk.courtdata.repository.DefendantMAATDataRepository;
import gov.uk.courtdata.repository.SolicitorMAATDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SaveAndLinkProcessor {


    private final Gson gson;
    private final SaveAndLinkImpl saveAndLinkImpl;
    private final SolicitorMAATDataRepository solicitorMAATDataRepository;
    private final DefendantMAATDataRepository defendantMAATDataRepository;


    public void process(String linkRequestPayload) {

        mapRequestPayload(linkRequestPayload);

    }


    private void mapRequestPayload(String linkRequestPayload) {
        SaveAndLinkModel saveAndLinkModel = new SaveAndLinkModel();
        CaseDetails caseDetails = gson.fromJson(linkRequestPayload, CaseDetails.class);
        mapSaveAndLinkModel(saveAndLinkModel, caseDetails);
        saveAndLinkImpl.execute(saveAndLinkModel);

    }

    private void mapSaveAndLinkModel(SaveAndLinkModel saveAndLinkModel, CaseDetails caseDetails) {
        Integer maatId = caseDetails.getMaatId();
        saveAndLinkModel.setCaseDetails(caseDetails);
        mapSolicitorMAATDataInfo(maatId, saveAndLinkModel);
        mapDefendantMAATDataInfo(maatId, saveAndLinkModel);

    }

    private void mapDefendantMAATDataInfo(Integer maatId, SaveAndLinkModel saveAndLinkModel) {
        Optional<DefendantMAATDataEntity> defendantDetails = defendantMAATDataRepository.findBymaatId(maatId);
        DefendantMAATDataEntity defendantMAATDataEntity = defendantDetails.orElse(null);
        saveAndLinkModel.setDefendantMAATDataEntity(defendantMAATDataEntity);
        mapIdentifiers(saveAndLinkModel);
    }

    private void mapIdentifiers(SaveAndLinkModel saveAndLinkModel) {
        saveAndLinkModel.setCaseId(defendantMAATDataRepository.getCaseID());
        saveAndLinkModel.setTxId(defendantMAATDataRepository.getTxnID());
        saveAndLinkModel.setProceedingId(defendantMAATDataRepository.getProceedingID());
        saveAndLinkModel.setLibraId(defendantMAATDataRepository.getLibraID());
    }

    private void mapSolicitorMAATDataInfo(Integer maatId, SaveAndLinkModel saveAndLinkModel) {
        Optional<SolicitorMAATDataEntity> solicitorDetails = solicitorMAATDataRepository.findBymaatId(maatId);
        SolicitorMAATDataEntity solicitorMAATData = solicitorDetails.orElse(null);
        saveAndLinkModel.setSolicitorMAATDataEntity(solicitorMAATData);

    }

}




