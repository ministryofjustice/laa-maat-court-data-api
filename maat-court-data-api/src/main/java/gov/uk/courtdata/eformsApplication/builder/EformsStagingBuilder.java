package gov.uk.courtdata.eformsApplication.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import gov.uk.courtdata.eformsApplication.dto.EformsStagingDTO;
import gov.uk.courtdata.model.eformsApplication.EformsApplication;
import gov.uk.courtdata.model.eformsApplication.Offence;
import gov.uk.courtdata.model.eformsApplication.xmlModels.Charge;
import gov.uk.courtdata.model.eformsApplication.xmlModels.EformsApplicationFormData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class EformsStagingBuilder {

    private static final String APPLICATION_TYPE = "CRM14";
    private static final String OFFENCE_CLASS_PREFIX = "Class ";

    public EformsStagingDTO build(EformsApplication eformsApplication) throws SQLException, JsonProcessingException {
        return EformsStagingDTO
                .builder()
                .usn(eformsApplication.getReference())
                .type(APPLICATION_TYPE)
                .xmlDoc(generateXmlString(eformsApplication))
                .build();
    }

    public String generateXmlString(EformsApplication eformsApplication) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.writeValueAsString(buildFormData(eformsApplication));
    }

    public EformsApplicationFormData buildFormData(EformsApplication eformsApplication) {
        return EformsApplicationFormData.builder().build();
    }

    public List<Charge> buildChargeList(List<Offence> offences) {

        List<Charge> charges = new ArrayList<>();

        offences.stream().forEach(offence -> {
            charges.addAll(
                    offence.getDates().stream().map(dates ->
                            Charge.builder()
                                    .charge(offence.getName())
                                    .offenceWhen(dates.getDateTo() != null ? "BETWEEN":"ON")
                                    .offenceDate1(dates.getDateFrom().)
                                    .build()).collect(Collectors.toList())
            );
        });
    }

    public String getOffenceType(List<Offence> offences) {
        List<Offence> offencesWithClass = offences.stream()
                .filter(item -> item.getOffenceClass() != null &&
                                        item.getOffenceClass().startsWith(OFFENCE_CLASS_PREFIX))
                .collect(Collectors.toList());
        offencesWithClass.sort(
                Comparator.comparing(
                        offence -> offence.getOffenceClass().charAt(offence.getOffenceClass().length() - 1)));
        return offencesWithClass.get(0).getOffenceClass();
    }
}
