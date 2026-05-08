package gov.uk.courtdata.model.laastatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Value
public class LaaStatusUpdate {

    @SerializedName("data")
    @Expose
    RepOrderData data;
}
