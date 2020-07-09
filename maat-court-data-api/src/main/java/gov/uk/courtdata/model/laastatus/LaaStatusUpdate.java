package gov.uk.courtdata.model.laastatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LaaStatusUpdate {

    @SerializedName("data")
    @Expose
    private RepOrderData data;

}
