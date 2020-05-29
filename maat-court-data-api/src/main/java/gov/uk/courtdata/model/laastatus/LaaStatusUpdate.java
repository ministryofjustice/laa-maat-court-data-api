package gov.uk.courtdata.model.laastatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaaStatusUpdate {

    @SerializedName("data")
    @Expose
    private RepOrderData data;

}
