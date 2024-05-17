package gov.uk.courtdata.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.enums.LoggingData;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AddLaaTransactionIdToMDCTest {

  private static final String LAA_TRANSACTION_ID = "Laa-Transaction-Id";

  @Mock
  private HttpServletRequest httpServletRequest;

  @Mock
  private ServletResponse servletResponse;

  @Mock
  private FilterChain filterChain;

  private AddLaaTransactionIdToMDC filter;

  @BeforeEach
  public void setUp() {
    MDC.clear();
    filter = new AddLaaTransactionIdToMDC();
  }

  @AfterEach
  public void tearDown() {
    MDC.clear();
  }

  @Test
  public void doFilter_addsLaaTransactionIdToMDC_whenHeaderIsPresent()
      throws IOException, ServletException {
    final String laaTransactionId = "7c49ebfe-fe3a-4f2f-8dad-f7b8f03b8327";
    when(httpServletRequest.getHeader(LAA_TRANSACTION_ID)).thenReturn(laaTransactionId);

    filter.doFilter(httpServletRequest, servletResponse, filterChain);

    assertEquals(laaTransactionId, MDC.get(LoggingData.LAA_TRANSACTION_ID.getKey()));
    verify(filterChain)
        .doFilter(httpServletRequest, servletResponse);
  }

  @Test
  public void doFilter_doesNotAddLaaTransactionIdToMDC_whenHeaderIsAbsent()
      throws IOException, ServletException {
    when(httpServletRequest.getHeader(LAA_TRANSACTION_ID)).thenReturn(null);

    filter.doFilter(httpServletRequest, servletResponse, filterChain);

    assertNull(MDC.get(LoggingData.LAA_TRANSACTION_ID.getKey()));
    verify(filterChain)
        .doFilter(httpServletRequest, servletResponse);
  }

  @Test
  public void doFilter_handlesNonHttpServletRequest() throws IOException, ServletException {
    ServletRequest servletRequest = Mockito.mock(ServletRequest.class);
    filter.doFilter(servletRequest, servletResponse, filterChain);

    assertNull(MDC.get(LoggingData.LAA_TRANSACTION_ID.getKey()));
    verify(filterChain)
        .doFilter(servletRequest, servletResponse);
  }
}
