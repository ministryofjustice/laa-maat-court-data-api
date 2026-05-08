package gov.uk.courtdata.model.laastatus;

import lombok.*;
import lombok.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Offence {

    @SerializedName("offence_id")
    @Expose
    public String offenceId;

    @SerializedName("status_code")
    @Expose
    public String statusCode;

    @SerializedName("status_date")
    @Expose
    public String statusDate;

    @SerializedName("effective_start_date")
    @Expose
    public String effectiveStartDate;

    @SerializedName("effective_end_date")
    @Expose
    public String effectiveEndDate;
}
