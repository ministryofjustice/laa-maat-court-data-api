package gov.uk.courtdata.reporder.projection;

import java.time.LocalDate;

/**
 * A Projection for the {@link gov.uk.courtdata.entity.RepOrderEntity} entity
 */
public interface RepOrderEntityInfo {
    Integer getId();

    LocalDate getDateCreated();
}