package gov.uk.courtdata.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "FIN_ASS_STATUSES")
public class FinAssStatus {
    @Id
    @Column(name = "STATUS", nullable = false, length = 20)
    private String id;

    //TODO Reverse Engineering! Migrate other columns to the entity
}