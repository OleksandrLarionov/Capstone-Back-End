package LarionovOleksandrBackEndCapstone.D.DBlog.security;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.User;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.NotFoundException;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.UnauthorizedException;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private UserService userService;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getMethod().equals("state")) {
            String requestPath = request.getServletPath();
            if (requestPath.startsWith("/google/callback")) {
                filterChain.doFilter(request, response);
                return;
            }
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer "))
                throw new UnauthorizedException("Non è presente il token nell'Authorization header.");
            String accesToken = authHeader.substring(11);
            jwtTools.verifyToken(accesToken);
            String email = jwtTools.extractEmailFromToken(accesToken);
            try {
                User user = userService.findByEmail(email);
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);


            } catch (ExpiredJwtException | NotFoundException ex) {
                exceptionResolver.resolveException(request, response, null, ex);
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String servletPath = request.getServletPath();
        return new AntPathMatcher().match("/auth/**", servletPath)
                || servletPath.equals("/google/authorization-url")
                || servletPath.startsWith("/google/callback")
                || servletPath.equals("/favicon.ico")
                || servletPath.equals("/login");
    }
}
