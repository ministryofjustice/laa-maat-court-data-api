package gov.uk.courtdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.model.CaseDetails;

import java.io.File;
import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(new File("/Users/ahilansanthanlingam/laa-maat-court-data-api/maat-court-data-api/saveAndLink.json"), new CaseDetails());

    }
}
