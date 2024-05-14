package gov.uk.courtdata.filter;

import gov.uk.courtdata.enums.LoggingData;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@WebFilter("/*")
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AddLaaTransactionIdToMDC implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {

    if (request instanceof HttpServletRequest) {
      String laaTransactionIdHeaderValue = ((HttpServletRequest) request).getHeader(
          "Laa-Transaction-Id");

      log.debug("Adding Laa-Transaction-Id {} to the MDC", laaTransactionIdHeaderValue);
      LoggingData.LAA_TRANSACTION_ID.putInMDC(laaTransactionIdHeaderValue);
    }
    chain.doFilter(request, response);
  }
}
