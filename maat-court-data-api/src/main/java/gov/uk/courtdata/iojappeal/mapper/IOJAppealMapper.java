package gov.uk.courtdata.iojappeal.mapper;

import gov.uk.courtdata.dto.IOJAppealDTO;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.model.iojAppeal.CreateIOJAppeal;
import gov.uk.courtdata.model.iojAppeal.UpdateIOJAppeal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import uk.gov.justice.laa.crime.common.model.ioj.ApiCreateIojAppealRequest;
import uk.gov.justice.laa.crime.common.model.ioj.ApiCreateIojAppealResponse;
import uk.gov.justice.laa.crime.common.model.ioj.ApiGetIojAppealResponse;
import uk.gov.justice.laa.crime.common.model.ioj.IojAppeal;
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
    IOJAppealEntity toIojAppealEntity(IOJAppealDTO iojAppealDTO);

    @Mapping(target = "repOrder.id", source = "iojAppealMetadata.legacyApplicationId")
    @Mapping(target = "appealSetupDate", source = "iojAppeal.receivedDate", qualifiedByName = "mapDateToDateTime")
    @Mapping(target = "nworCode", source = "iojAppeal.appealReason")
    @Mapping(target = "userCreated", source = "iojAppealMetadata.userSession.userName")
    @Mapping(target = "cmuId", source = "iojAppealMetadata.caseManagementUnitId")
    @Mapping(target = "appealSetupResult", source = "iojAppeal", qualifiedByName = "mapAppealSetupResult")
    @Mapping(target = "decisionDate", source = "iojAppeal.decisionDate", qualifiedByName = "mapDateToDateTime")
    @Mapping(target = "decisionResult", source = "iojAppeal", qualifiedByName = "mapDecisionResult")
    @Mapping(target = "iderCode", source = "iojAppeal.decisionReason", qualifiedByName = "mapIderCode")
    @Mapping(target = "notes", source = "iojAppeal.notes")
    @Mapping(target = "iapsStatus", constant = "COMPLETE")
    IOJAppealEntity toIojAppealEntity(ApiCreateIojAppealRequest apiCreateIojAppealRequest);

    @Mapping(target = "legacyAppealId", source = "id")
    ApiCreateIojAppealResponse toApiCreateIojAppealResponse(IOJAppealEntity iojAppealEntity);

    @Mapping(target = "legacyAppealId", source = "id")
    @Mapping(target = "receivedDate", source = "appealSetupDate", qualifiedByName = "mapDateTimeToDate")
    @Mapping(target = "appealReason", source = "nworCode")
    @Mapping(target = "appealAssessor", source = "appealSetupResult", qualifiedByName = "mapAppealAssessor")
    @Mapping(target = "appealSuccessful", source = "decisionResult", qualifiedByName = "isAppealSuccessful")
    @Mapping(target = "decisionReason", source = "iderCode", qualifiedByName = "mapDecisionReason")
    @Mapping(target = "decisionDate", source = "decisionDate", qualifiedByName = "mapDateTimeToDate")
    @Mapping(target = "caseManagementUnitId", source = "cmuId")
    ApiGetIojAppealResponse toApiGetIojAppealResponse(IOJAppealEntity iojAppealEntity);

    @Named("mapAppealSetupResult")
    static String mapAppealSetupResult(IojAppeal iojAppeal) {
        boolean appealSuccessful = iojAppeal.getAppealSuccessful();
        IojAppealAssessor iojAppealAssessor = iojAppeal.getAppealAssessor();
        if (iojAppealAssessor == IojAppealAssessor.JUDGE) {
            return "REFER";
        }
        if (appealSuccessful) {
            return "GRANT";
        }
        return "REFUSED";
    }

    @Named("mapDecisionResult")
    static String mapDecisionResult(IojAppeal iojAppeal) {
        boolean appealSuccessful = iojAppeal.getAppealSuccessful();
        if (appealSuccessful) {
            return "PASS";
        }
        return "FAIL";
    }

    @Named("mapIderCode")
    static String mapIderCode(IojAppealDecisionReason decisionReason) {
        return decisionReason.getCode();
    }

    @Named("mapDateToDateTime")
    static LocalDateTime mapDateToDateTime(LocalDate date) {
        return date.atStartOfDay();
    }

    @Named("mapDateTimeToDate")
    static LocalDate mapDateTimeToDate(LocalDateTime dateTime) {
        return dateTime.toLocalDate();
    }

    @Named("mapAppealAssessor")
    static IojAppealAssessor mapAppealAssessor(String appealSetupResult) {
        if (appealSetupResult.equalsIgnoreCase("REFER")) {
            return IojAppealAssessor.JUDGE;
        }
        return IojAppealAssessor.CASEWORKER;
    }

    @Named("isAppealSuccessful")
    static boolean isAppealSuccessful(String decisionResult) {
        return decisionResult.equalsIgnoreCase("PASS");
    }

    @Named("mapDecisionReason")
    static IojAppealDecisionReason mapDecisionReason(String iderCode) {
        return IojAppealDecisionReason.getFrom(iderCode);
    }
}
