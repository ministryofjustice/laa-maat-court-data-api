package gov.uk.courtdata.reporder.projection;

import java.time.LocalDate;

/**
 * A Projection for the {@link gov.uk.courtdata.entity.RepOrderMvoRegEntity} entity
 */
public interface RepOrderMvoRegEntityInfo {
    LocalDate getDateDeleted();
    String getRegistration();
}