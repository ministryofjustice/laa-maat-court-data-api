package gov.uk.courtdata.reporder.projection;

/**
 * A Projection for the {@link gov.uk.courtdata.entity.RepOrderMvoEntity} entity
 */
public interface RepOrderMvoEntityInfo {
    Integer getId();

    String getVehicleOwner();

    RepOrderEntityInfo getRep();
}