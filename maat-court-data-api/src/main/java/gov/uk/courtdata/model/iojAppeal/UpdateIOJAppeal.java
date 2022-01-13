package gov.uk.courtdata.model.iojAppeal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateIOJAppeal extends IOJAppeal{
    @NotNull
    private Integer id;

    private LocalDateTime dateModified;
    @NotNull
    private String userModified;
    }
