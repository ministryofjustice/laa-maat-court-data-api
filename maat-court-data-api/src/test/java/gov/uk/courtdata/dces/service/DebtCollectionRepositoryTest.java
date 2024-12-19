package gov.uk.courtdata.dces.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtCollectionRepositoryTest {
    @InjectMocks
    private DebtCollectionRepository debtCollectionRepository;

    @Mock
    JdbcTemplate jdbcTemplate;

    private static final byte[] fdcDelayedPickupStatementHash = new byte[] { 99, 42, 80, -85, 61, 50, -120, 64, -112, -102, -79, 107, -47, 77, 20, -25, -109, 45, -26, -60, -99, 71, 81, 62, -87, -124, 92, 84, 108, -40, -79, -27 };
    private static final byte[] fdcFastTrackingStatementHash = new byte[] { 84, -104, 124, 60, -127, -106, 99, 90, -70, 90, 106, -27, 35, -14, -59, -54, -115, -98, 31, -104, 9, 23, 44, 117, 102, -41, 95, -37, 78, -119, 95, 111 };

    @Captor
    ArgumentCaptor<String> mergeSQLCaptor;

    @Test
    void givenValidInput_whenSetEligibleForFdcDelayedPickup_thenVerifySQLStatement() {
        when(jdbcTemplate.update(mergeSQLCaptor.capture(), anyString()))
                .thenReturn(1);
        debtCollectionRepository.setEligibleForFdcDelayedPickup("?");

        assertThat(mergeSQLCaptor).isNotNull();
        assertArrayEquals(fdcDelayedPickupStatementHash, getCaptorHash(), "A change has been detected in the DebtCollectionRepository.setEligibleForFdcDelayedPickup() please verify changes are correct. And update this test.");
    }

    @Test
    void givenValidInput_whenSetEligibleForFdcFastTracking_thenVerifySQLStatement() {
        when(jdbcTemplate.update(mergeSQLCaptor.capture(), anyString()))
                .thenReturn(1);
        debtCollectionRepository.setEligibleForFdcFastTracking("?");

        assertThat(mergeSQLCaptor).isNotNull();
        assertArrayEquals(fdcFastTrackingStatementHash, getCaptorHash(), "A change has been detected in the DebtCollectionRepository.setEligibleForFdcFastTracking() please verify changes are correct. And update this test.");
    }

    byte[] getCaptorHash() {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return messageDigest.digest(mergeSQLCaptor.getValue().getBytes(StandardCharsets.UTF_8));
    }
}
