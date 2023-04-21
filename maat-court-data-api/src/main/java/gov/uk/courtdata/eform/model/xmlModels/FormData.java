package gov.uk.courtdata.eform.model.xmlModels;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class FormData {

   @JacksonXmlProperty(localName = "fielddata")
    private FieldData fieldData;
}
