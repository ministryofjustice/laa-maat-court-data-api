package gov.uk.courtdata.model;

import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import lombok.Setter;

import java.util.List;


@Builder
@Generated
@Data
public class MessageCollection {

    private List<String> messages;
}
