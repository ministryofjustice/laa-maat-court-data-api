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
public class Contact {

    @SerializedName("home")
    @Expose
    public String home;

    @SerializedName("work")
    @Expose
    public String work;

    @SerializedName("mobile")
    @Expose
    public String mobile;

    @SerializedName("primary_email")
    @Expose
    public String primaryEmail;

    @SerializedName("secondary_email")
    @Expose
    public String secondaryEmail;

    @SerializedName("fax")
    @Expose
    public String fax;
}
