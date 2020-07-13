package gov.uk.courtdata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_QUEUE_MESSAGE_LOG", schema = "MLA")
public class QueueMessageLogEntity {

    @Id
    @Column(name = "TRANSACTION_UUID")
    private String transactionUUID;
    @Column(name = "MAAT_ID")
    private Integer maatId;
    @Column(name = "TYPE")
    private String type;
    @Lob
    @Column(name = "MESSAGE")
    private byte[] message;
    @Column(name = "CREATED_TIME")
    private LocalDateTime createdTime;
}
