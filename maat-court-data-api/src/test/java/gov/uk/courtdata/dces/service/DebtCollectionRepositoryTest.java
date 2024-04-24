package gov.uk.courtdata.dces.service;

import org.junit.jupiter.api.Assertions;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtCollectionRepositoryTest {

    @InjectMocks
    private DebtCollectionRepository debtCollectionRepository;

    @Mock
    JdbcTemplate jdbcTemplate;
    private static final byte[] fdcMerge1StatementHash = new byte[]{-47, -40, -68, -99, 85, -77, -119, -64, 1, 3, 83, -92, -76, 27, -114, 107, -83, -31, -66, 60, -122, -24, 127, 55, 120, -112, 96, -51, -105, 62, 1, -18};
    private static final byte[] fdcMerge2StatementHash = new byte[]{-17, -92, -125, -62, -12, 18, 39, -68, -70, 54, 77, -42, 16, 113, 24, -106, -124, 116, 44, -63, -103, 78, 66, -78, 15, -41, -42, -65, -66, -45, -74, -48};

    @Captor
    ArgumentCaptor<String> mergeSQLCaptor;

    @Test
    void verifyMergeStatement1_IsExpected() {
        when(jdbcTemplate.update(mergeSQLCaptor.capture(),anyString()))
                .thenReturn(1);
        debtCollectionRepository.globalUpdatePart1("?");

        assertNotNull(mergeSQLCaptor);
        Assertions.assertArrayEquals(fdcMerge1StatementHash, getCaptorHash(), "A change has been detected in the debtCollectionRepository.globalUpdatePart1() please verify changes are correct. And update this test.");
    }

    @Test
    void verifyMergeStatement2_IsExpected() {
        when(jdbcTemplate.update(mergeSQLCaptor.capture(),anyString()))
                .thenReturn(1);
        debtCollectionRepository.globalUpdatePart2("?");


        assertNotNull(mergeSQLCaptor);
        Assertions.assertArrayEquals(fdcMerge2StatementHash,getCaptorHash(),"A change has been detected in the debtCollectionRepository.globalUpdatePart1() please verify changes are correct. And update this test.");
    }

    byte[] getCaptorHash(){
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return messageDigest.digest(mergeSQLCaptor.getValue().getBytes(StandardCharsets.UTF_8));
    }
}