package gov.uk.courtdata.model.eformsApplication.xmlModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TblProcessedAttachments {
    @JsonProperty("Dtsubmitted")
    private String dtSubmitted;
    @JsonProperty("Filename")
    private String filename;
    @JsonProperty("File_size_bytes")
    private Long fileSizeBytes;
    @JsonProperty("File_size_mb")
    private BigDecimal fileSizeMb;
    @JsonProperty("Evidence_type")
    private String evidenceType;
    @JsonProperty("Provider_notes")
    private String providerNotes;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Caseworker_notes")
    private String caseworkerNotes;
    @JsonProperty("Dtprocessed")
    private String dtProcessed;
    @JsonProperty("Provider_firm_id")
    private Integer providerFirmId;
}
