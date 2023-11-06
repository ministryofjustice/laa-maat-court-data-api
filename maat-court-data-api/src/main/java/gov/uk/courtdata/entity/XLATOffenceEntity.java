package gov.uk.courtdata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_XLAT_OFFENCE", schema = "MLA")
public class XLATOffenceEntity {

    @Id
    @Column(name = "OFFENCE_CODE")
    private String offenceCode;
    @Column(name = "PARENT_CODE")
    private String parentCode;
    @Column(name = "CODE_MEANING")
    private String codeMeaning;
    @Column(name = "CODE_START")
    private LocalDate codeStart;
    @Column(name = "APPLICATION_FLAG")
    private Integer applicationFlag;
    @Column(name = "CREATED_USER")
    private String createdUser;
    @Column(name = "CREATED_DATE")
    private LocalDate createdDate;

}
