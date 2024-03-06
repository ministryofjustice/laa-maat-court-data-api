/**
 *
 */
package gov.uk.courtdata.dto.application;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.HashSet;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DrcSummaryDTO extends GenericDTO {
    private long repId;

    @Builder.Default
    private Collection<DrcConversationDTO> conversations = new HashSet<>();
}
