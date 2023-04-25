package gov.uk.courtdata.filters.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.SyncFailedException;

@Slf4j
public class ValidationFilter implements Filter {

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
        if (true) {
            chain.doFilter(request, response);
            log.info("Committing Transaction for req :{}", req.getRequestURI());
        } else {
            throw new SyncFailedException("");
        }


    }

    @Override
    public void destroy() {
        log.warn("Destructing filter :{}", this);
    }
}
