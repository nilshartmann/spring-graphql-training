package nh.publy.backend.security;

import nh.publy.backend.domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * JwtAuthenticationFilter
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private AuthenticationService authenticationService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {

    try {
      authenticateIfNeeded(request);
    } catch (AuthenticationException bed) {
      logger.error("Could not authenticate: " + bed, bed);
      SecurityContextHolder.clearContext();
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    filterChain.doFilter(request, response);
  }

  private void authenticateIfNeeded(HttpServletRequest request) {
    getJwtFromRequest(request)
      .map(authenticationService::verify)
      .map(this::createAuthentication)
      .ifPresent(authentication -> SecurityContextHolder.getContext().setAuthentication(authentication));
  }


  private Authentication createAuthentication(User user) {
    return new UsernamePasswordAuthenticationToken(
      user,
      null,
      Arrays.stream(user.getRoles()).map(SimpleGrantedAuthority::new).toList()
    );
  }

  private Optional<String> getJwtFromRequest(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader("Authorization"))
      .map(authHeader -> {
        if (!authHeader.startsWith("Bearer ")) {
          throw new BadCredentialsException(
            "Invalid 'Authorization'-Header ('" + authHeader + "'). Expected format: 'Authorization: Bearer TOKEN'");
        }
        return authHeader.substring(7);
      });
  }

}