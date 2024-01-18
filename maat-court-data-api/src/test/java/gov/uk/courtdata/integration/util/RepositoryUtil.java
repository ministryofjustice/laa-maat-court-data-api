package gov.uk.courtdata.integration.util;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
public class RepositoryUtil {

    /**
     * The responsibility of this method is to delete all and flush all entities within all the supplied repositories.
     *
     * @param repositories The repositories to be cleared
     */
    @Transactional
    public void clearUp(JpaRepository<?, ?>... repositories) {
        Arrays.stream(repositories).forEach(CrudRepository::deleteAll);
        Arrays.stream(repositories).forEach(JpaRepository::flush);
    }
}
