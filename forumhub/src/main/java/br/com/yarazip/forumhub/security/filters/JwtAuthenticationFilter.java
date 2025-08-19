package br.com.yarazip.forumhub.security.filters;

import br.com.yarazip.forumhub.security.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	private final UserDetailsService userDetailsService;
	private final JwtUtil jwtUtil;

	public JwtAuthenticationFilter(UserDetailsService userDetailsService, JwtUtil jwtUtil) {
		this.userDetailsService = userDetailsService;
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		logger.info("Processing authentication for request: {}", request.getRequestURI());

		String jwt = extractJwtFromCookies(request);
		if (jwt == null || jwt.trim().isEmpty()) {
			logger.warn("JWT token not found in request");
			chain.doFilter(request, response);
			return;
		}

		try {
			String userId = jwtUtil.extractUserId(jwt);

			if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				if (!jwtUtil.isTokenExpired(jwt)) {
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							UUID.fromString(userId), null, Collections.emptyList());
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
		} catch (Exception e) {
			logger.error("JWT authentication failed: {}", e.getMessage());
		}

		chain.doFilter(request, response);
	}

	private String extractJwtFromCookies(HttpServletRequest request) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("JWT_TOKEN".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}
