package gov.uk.courtdata.iojappeal.mapper;

import gov.uk.courtdata.dto.IOJAppealDTO;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.model.iojAppeal.CreateIOJAppeal;
import gov.uk.courtdata.model.iojAppeal.UpdateIOJAppeal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import uk.gov.justice.laa.crime.common.model.ioj.ApiGetIojAppealResponse;
import uk.gov.justice.laa.crime.enums.IojAppealAssessor;
import uk.gov.justice.laa.crime.enums.IojAppealDecisionReason;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface IOJAppealMapper {
    @Mapping(target = "repId", source = "repOrder.id")
    IOJAppealDTO toIOJAppealDTO(IOJAppealEntity iojAppealEntity);

    @Mapping(target = "replaced", constant = "N")
    IOJAppealDTO toIOJAppealDTO(CreateIOJAppeal iojAppeal);

    IOJAppealDTO toIOJAppealDTO(UpdateIOJAppeal iojAppeal);

    @Mapping(target = "repOrder.id", source = "repId")
    IOJAppealEntity toIOJIojAppealEntity(IOJAppealDTO iojAppealDTO);

    @Mapping(target = "legacyAppealId", source = "id")
    @Mapping(target = "receivedDate", source = "appealSetupDate")
    @Mapping(target = "appealReason", source = "nworCode")
    @Mapping(target = "appealAssessor", source = "appealSetupResult", qualifiedByName = "mapAppealAssessor")
    @Mapping(target = "appealSuccessful", source = "decisionResult", qualifiedByName = "isAppealSuccesful")
    @Mapping(target = "decisionReason", source = "iderCode", qualifiedByName = "mapDecisionReason")
    ApiGetIojAppealResponse toApiGetIojAppealResponse(IOJAppealEntity iojAppealEntity);

    @Named("mapAppealAssessor")
    static IojAppealAssessor mapAppealAssessor(String appealSetupResult) {
        if (appealSetupResult.equalsIgnoreCase("REFER")) {
            return IojAppealAssessor.JUDGE;
        }
        return IojAppealAssessor.CASEWORKER;
    }

    @Named("isAppealSuccesful")
    static boolean isAppealSuccesful(String decisionResult) {
        return decisionResult.equalsIgnoreCase("PASS");
    }

    @Named("mapDecisionReason")
    static IojAppealDecisionReason mapDecisionReason(String iderCode) {
        return IojAppealDecisionReason.getFrom(iderCode);
    }
}
