package gov.uk.courtdata.prosecutionconcluded.listner.request.crowncourt;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProsecutionConcludedRequest {

    @SerializedName("prosecutionConcluded")
    private List<ProsecutionConcluded> prosecutionConcludedList;
    private int messageRetryCounter;

}