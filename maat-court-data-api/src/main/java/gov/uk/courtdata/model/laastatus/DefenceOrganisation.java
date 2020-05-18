package gov.uk.courtdata.model.laastatus;


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
public class DefenceOrganisation {

    @SerializedName("laa_contract_number")
    @Expose
    public String laaContractNumber;
    @SerializedName("sra_number")
    public String sraNumber;
    @SerializedName("bar_council_membership_number")
    @Expose
    public String barCouncilMembershipNumber;
    @SerializedName("incorporation_number")
    @Expose
    public String incorporationNumber;
    @SerializedName("registered_charity_number")
    @Expose
    public String registeredCharityNumber;
    @SerializedName("organisation")
    @Expose
    public Organisation organisation;

}
