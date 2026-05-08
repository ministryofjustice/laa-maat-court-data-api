package gov.uk.courtdata.model.laastatus;

import lombok.*;
import lombok.Data;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attributes {

    @SerializedName("maat_reference")
    @Expose
    private Integer maatReference;

    @SerializedName("defence_organisation")
    @Expose
    private DefenceOrganisation defenceOrganisation;

    @SerializedName("offences")
    @Expose
    private List<Offence> offences;
}
