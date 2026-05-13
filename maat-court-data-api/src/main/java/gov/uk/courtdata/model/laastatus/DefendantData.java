package gov.uk.courtdata.model.laastatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefendantData {

    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("id")
    @Expose
    public String id;
}
