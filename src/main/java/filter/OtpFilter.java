package filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class OtpFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        // Check if the request is for the specific servlet where keyId is not required
        if (httpRequest.getServletPath().equals("/handshake")) {
            // Allow the request to proceed without checking the keyId header
            chain.doFilter(request, response);
        } else {
            // Check if the keyId header exists
            String keyId = httpRequest.getHeader("keyId");
            if (keyId != null && !keyId.isEmpty()) {
                // The keyId header exists, allow the request to proceed
                chain.doFilter(request, response);
            } else {
                // The keyId header is missing, return an error response
                httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                httpResponse.getWriter().write("Missing keyId header");
            }
        }
    }

    @Override
    public void destroy() {
        // Clean up any resources used by the filter
    }
}
