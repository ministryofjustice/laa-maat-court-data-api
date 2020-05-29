package gov.uk.courtdata.model.laastatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import lombok.Data;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Organisation {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("address")
    @Expose
    public Address address;
    @SerializedName("contact")
    @Expose
    public Contact contact;

}
