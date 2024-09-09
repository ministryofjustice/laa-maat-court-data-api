package gov.uk.courtdata.service;

import gov.uk.courtdata.dto.FeatureToggleDTO;
import gov.uk.courtdata.entity.FeatureToggleEntity;
import gov.uk.courtdata.mapper.FeatureToggleMapper;
import gov.uk.courtdata.repository.FeatureToggleRepository;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeatureToggleService {

  private final FeatureToggleRepository featureToggleRepository;
  private final FeatureToggleMapper featureToggleMapper;

  public List<FeatureToggleDTO> getFeatureTogglesForUser(String username) {
    List<FeatureToggleEntity> allFeatureToggleEntitiesForUser = featureToggleRepository.getAllFeatureTogglesForUser(username);
    List<FeatureToggleEntity> featureToggleEntitiesForUser = allFeatureToggleEntitiesForUser.stream().filter(e -> username.equals(e.getUsername())).toList();

    List<FeatureToggleEntity> nonOverlappingPublicFeatureToggles = this.getNonOverlappingPublicFeatureToggleEntities(allFeatureToggleEntitiesForUser, featureToggleEntitiesForUser);

    return (featureToggleEntitiesForUser.isEmpty() && nonOverlappingPublicFeatureToggles.isEmpty())
        ? null
        : featureToggleMapper.featureToggleToFeatureToggleDto(Stream.concat(featureToggleEntitiesForUser.stream(), nonOverlappingPublicFeatureToggles.stream()).toList());
  }

  private List<FeatureToggleEntity> getNonOverlappingPublicFeatureToggleEntities(List<FeatureToggleEntity> allFeatureToggleEntitiesForUser, List<FeatureToggleEntity> featureToggleEntitiesForUser) {
    List<FeatureToggleEntity> publicFeatureToggleEntities = allFeatureToggleEntitiesForUser.stream().filter(t -> t.getUsername() == null).toList();

    return publicFeatureToggleEntities
        .stream()
        .filter(e -> featureToggleEntitiesForUser.stream().noneMatch(
            userEntity -> userEntity.getFeatureName().equals(e.getFeatureName()) && userEntity.getAction().equals(e.getAction())))
        .toList();
  }
}
