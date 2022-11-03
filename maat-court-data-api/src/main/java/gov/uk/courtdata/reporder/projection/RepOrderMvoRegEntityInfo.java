package gov.uk.courtdata.reporder.projection;

import java.time.LocalDate;

/**
 * A Projection for the {@link gov.uk.courtdata.entity.RepOrderMvoRegEntity} entity
 */
public interface RepOrderMvoRegEntityInfo {
    Integer getId();

    LocalDate getDateDeleted();

    String getRegistration();
}