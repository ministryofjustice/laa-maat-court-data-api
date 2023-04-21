package gov.uk.courtdata.eform.model.xmlModels;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "formData", namespace = "http://eforms.legalservices.gov.uk/lscservice")
public class ParentFormData {

    @JacksonXmlProperty(localName = "formdata", namespace = "urn:www-toplev-com:officeformsofd")
    private FormData formData;
}
