package gov.uk.courtdata.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "FEATURE_TOGGLE", schema = "TOGDATA")
public class FeatureToggleEntity {

  @Id
  @SequenceGenerator(name = "feature_toggle_seq", sequenceName = "ISEQ$$_316195", allocationSize = 1, schema = "TOGDATA")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feature_toggle_seq")
  @Column(name = "ID", nullable = false)
  private Integer id;

  @Column(name = "USER_NAME")
  private String username;

  @Column(name = "FEATURE")
  private String featureName;

  @Column(name = "ACTION")
  private String action;
}
