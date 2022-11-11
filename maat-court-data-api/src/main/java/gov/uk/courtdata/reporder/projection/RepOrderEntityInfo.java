package gov.uk.courtdata.reporder.projection;

import java.time.LocalDateTime;

/**
 * A Projection for the {@link gov.uk.courtdata.entity.RepOrderEntity} entity
 */
public interface RepOrderEntityInfo {
    Integer getId();

    LocalDateTime getDateCreated();
}