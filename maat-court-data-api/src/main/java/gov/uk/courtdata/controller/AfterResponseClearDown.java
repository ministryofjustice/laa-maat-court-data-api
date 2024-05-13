package gov.uk.courtdata.controller;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@WebFilter("/*")
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class AfterResponseClearDown implements Filter {
    
    @Override
    public void doFilter(ServletRequest servletRequest,
        ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.clear();
            log.debug("Cleared down the MDC");
        }
    }
}
