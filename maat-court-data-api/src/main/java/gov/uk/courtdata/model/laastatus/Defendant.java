package gov.uk.courtdata.model.laastatus;

import lombok.*;
import lombok.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Defendant {

    @SerializedName("data")
    @Expose
    public DefendantData data;
}
