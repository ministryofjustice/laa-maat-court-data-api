package gov.uk.courtdata.filter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.enums.LoggingData;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

@ExtendWith(MockitoExtension.class)
class AfterResponseClearDownTest {

  @Mock
  private HttpServletRequest mockHttpRequest;

  @Mock
  private HttpServletResponse mockHttpResponse;

  @Mock
  private FilterChain mockFilterChain;

  private AfterResponseClearDown filter;

  @BeforeEach
  void beforeEach() {
    MDC.clear();
    filter = new AfterResponseClearDown();
  }

  @AfterEach
  void afterEach() {
    MDC.clear();
  }

  @Test
  void should_ClearTheMappingDiagnosticsContext() throws ServletException, IOException {
    MDC.put(String.valueOf(LoggingData.MAATID), "123456");
    MDC.put(String.valueOf(LoggingData.LAA_TRANSACTION_ID), "230di29ied-=3di32-d0");

    filter.doFilter(mockHttpRequest, mockHttpResponse, mockFilterChain);

    assertAll(
        () -> verify(mockFilterChain).doFilter(mockHttpRequest, mockHttpResponse),
        () -> assertNull(MDC.get(String.valueOf(LoggingData.MAATID))),
        () -> assertNull(MDC.get(String.valueOf(LoggingData.LAA_TRANSACTION_ID)))
    );
  }
}