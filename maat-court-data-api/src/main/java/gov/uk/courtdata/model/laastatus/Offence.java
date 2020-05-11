package gov.uk.courtdata.model.laastatus;

import lombok.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@ToString
@Data
@Builder
@Getter
@Setter
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