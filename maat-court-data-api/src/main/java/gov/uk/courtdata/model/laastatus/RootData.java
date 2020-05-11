package gov.uk.courtdata.model.laastatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.*;

@ToString
@lombok.Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RootData {

    @SerializedName("data")
    @Expose
    private RepOrderData data;
}
