package gov.uk.courtdata.model.laastatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.*;


@Builder
@NoArgsConstructor (force = true)
@AllArgsConstructor
@Value
public class LaaStatusUpdate {

    @SerializedName("data")
    @Expose
    RepOrderData data;
}
