package hoan.com.springboot.security.jwt;

import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hoan.com.springboot.common.UserAuthentication;
import hoan.com.springboot.common.UserAuthority;
import hoan.com.springboot.common.error.AuthenticationError;
import hoan.com.springboot.common.exception.ResponseException;
import hoan.com.springboot.security.services.AuthorityService;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils helper;

    @Autowired
    private UserDetailsService service;

    @Autowired
    private AuthorityService authorityService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = helper.getToken(request);

        if (helper.validate(token)) {
            Boolean isRoot = Boolean.FALSE;
            Boolean isClient = Boolean.FALSE;
            String userId = null;
            Instant issuedAt = helper.getIssuedAt(token).toInstant();
            Optional<UserAuthority> optionalUserAuthority = enrichAuthority(token);

            Set<SimpleGrantedAuthority> grantedPermissions = optionalUserAuthority
                    .map(auth -> CollectionUtils.isEmpty(auth.getGrantedPermissions()) ?
                            new HashSet<SimpleGrantedAuthority>() :
                            auth.getGrantedPermissions().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet()))
                    .orElse(new HashSet<>());

            if (optionalUserAuthority.isPresent()) {
                UserAuthority userAuthority = optionalUserAuthority.get();
                isRoot = userAuthority.getIsRoot();
                userId = userAuthority.getUserId();
            } else {
                userId = helper.getUserId(token);
            }

            User principal = new User(helper.getUsername(token), "", grantedPermissions);
            AbstractAuthenticationToken auth = new UserAuthentication(principal,
                    token,
                    grantedPermissions,
                    isRoot,
                    userId,
                    token);

            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

//    @Override
//    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        Authentication authentication = securityContext.getAuthentication();
//
//        return !(authentication instanceof JwtAuthenticationToken);
//    }

    private Optional<UserAuthority> enrichAuthority(String token) {

        String userId = helper.getUserId(token);

        try {
                if (!StringUtils.hasLength(userId)) {
                    return Optional.empty();
                }

                UserAuthority userAuthority = authorityService.getUserAuthority(userId);
                if (userAuthority != null) {

                    userAuthority.setUserId(userId);
                    return Optional.of(userAuthority);
                }

        } catch (ResponseException e) {
            if (e.getError().equals(AuthenticationError.FORBIDDEN_ACCESS_TOKEN)) {
                throw new ResponseException(AuthenticationError.UNAUTHORISED);
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new ResponseException(AuthenticationError.UNAUTHORISED);
        } catch (Exception e) {
        }
        return Optional.empty();
    }
}
