package gov.uk.courtdata.model.laastatus;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import lombok.Data;


@ToString
@Data
@Builder
@Getter
@Setter
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
