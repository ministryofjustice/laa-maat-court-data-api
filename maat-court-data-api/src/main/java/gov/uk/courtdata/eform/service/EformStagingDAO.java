package gov.uk.courtdata.eform.service;

import gov.uk.courtdata.eform.dto.EformStagingDTO;

import java.util.Optional;

/**
 * The responsibility of this class is to provide an abstraction on top of the Database layer.
 * Specifically to allow CRUD operations on the HUB.EFORMS_STAGING table.
 */
public interface EformStagingDAO {
    void update(EformStagingDTO eformStagingDTO);

    Optional<EformStagingDTO> retrieve(int usn);

    void delete(int usn);

    void create(EformStagingDTO eformStagingDTO);
}
