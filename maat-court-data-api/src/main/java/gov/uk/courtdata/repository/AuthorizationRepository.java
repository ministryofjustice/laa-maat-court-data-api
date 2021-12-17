package gov.uk.courtdata.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AuthorizationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<String> getAuthorizedActions(@Param("username") String username) {
        Query query = entityManager.createNativeQuery("SELECT ACTION FROM USER_ROLES R " +
                "JOIN ROLE_ACTIONS A on (A.ROLE_NAME = R.ROLE_NAME) " +
                "WHERE R.USER_NAME = :username " +
                "AND A.ENABLED = 'Y'"
        );
        query.setParameter("username", username);
        List<String> authorizedActions = new ArrayList<>();
        for (Object result : query.getResultList()) {
            authorizedActions.add((String) result);
        }
        return authorizedActions;
    }

    public List<String> getAvailableActions() {
        Query query = entityManager.createNativeQuery("SELECT DISTINCT ACTION FROM ROLE_ACTIONS");
        List<String> authorizedActions = new ArrayList<>();
        for (Object result : query.getResultList()) {
            authorizedActions.add((String) result);
        }
        return authorizedActions;
    }
}
