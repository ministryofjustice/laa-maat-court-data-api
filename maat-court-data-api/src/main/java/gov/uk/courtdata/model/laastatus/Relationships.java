package gov.uk.courtdata.model.laastatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import lombok.Data;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Relationships {

    @SerializedName("defendant")
    @Expose
    public Defendant defendant;

}
