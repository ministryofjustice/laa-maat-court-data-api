package gov.uk.courtdata.job;

import gov.uk.courtdata.repository.QueueMessageLogRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QueueMessageMaintenanceJobTest {


    @InjectMocks
    private QueueMessageMaintenanceJob job;

    @Mock
    private Integer expiryInDays;


    @Mock
    private QueueMessageLogRepository queueMessageLogRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

}
