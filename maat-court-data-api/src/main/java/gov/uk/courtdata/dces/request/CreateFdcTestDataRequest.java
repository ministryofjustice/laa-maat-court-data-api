package gov.uk.courtdata.dces.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFdcTestDataRequest{
    private boolean negativeTest;
    private FdcNegativeTestType negativeTestType;
    private int numOfTestEntries;
}