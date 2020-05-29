package gov.uk.courtdata.model.laastatus;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @SerializedName("address1")
    @Expose
    private String address1;
    @SerializedName("address2")
    private String address2;
    @SerializedName("address3")
    @Expose
    private String address3;
    @SerializedName("address4")
    @Expose
    private String address4;
    @SerializedName("address5")
    @Expose
    private String address5;
    @SerializedName("postcode")
    @Expose
    private String postcode;

}
