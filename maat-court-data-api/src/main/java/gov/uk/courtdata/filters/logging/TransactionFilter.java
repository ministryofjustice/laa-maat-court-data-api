package gov.uk.courtdata.filters.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * A filter to create transaction before and commit it once request completes
 */
@Component
@Order(1)
@Slf4j
public class TransactionFilter implements Filter {

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        log.info("Initializing filter :{}", this);
    }

    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        log.info("Starting Transaction for req :{}", req.getRequestURI());
        chain.doFilter(request, response);
        log.info("Committing Transaction for req :{}", req.getRequestURI());
    }

    @Override
    public void destroy() {
        log.warn("Destructing filter :{}", this);
    }
}