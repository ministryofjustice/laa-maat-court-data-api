package gov.uk.courtdata.address.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ADDRESSES", schema = "TOGDATA")
public class Address {
    @Id
    @SequenceGenerator(name = "addresses_gen_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addresses_gen_seq")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "LINE1", length = 100)
    private String line1;

    @Column(name = "LINE2", length = 100)
    private String line2;

    @Column(name = "LINE3", length = 100)
    private String line3;

    @Column(name = "CITY", length = 100)
    private String city;

    @Column(name = "POSTCODE", length = 10)
    private String postcode;

    @Column(name = "COUNTY", length = 150)
    private String county;

    @Column(name = "COUNTRY", length = 25)
    private String country;

    @CreationTimestamp
    @Column(name = "DATE_CREATED", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "USER_CREATED", nullable = false, length = 100)
    private String userCreated;

    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Column(name = "USER_MODIFIED", length = 100)
    private String userModified;

}