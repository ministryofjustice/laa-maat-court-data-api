package gov.uk.courtdata.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.function.Consumer;

@UtilityClass
public class RepositoryUtil {

    private final Consumer<JpaRepository<?, ?>> DELETE_ALL_AND_FLUSH = repository -> {
        repository.deleteAll();
        repository.flush();
    };

    /**
     * The responsibility of this method is to delete all and flush all entities within all the supplied repositories.
     *
     * @param repositories The repositories to be cleared
     */
    public void clearUp(JpaRepository<?, ?>... repositories) {
        Arrays.stream(repositories).forEach(DELETE_ALL_AND_FLUSH);
    }
}
