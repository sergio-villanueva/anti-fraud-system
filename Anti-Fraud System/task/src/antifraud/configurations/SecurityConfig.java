package antifraud.configurations;

import antifraud.database.entities.UserEntity;
import antifraud.database.repositories.UserRepository;
import antifraud.utilities.LockToggle;
import antifraud.utilities.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

import static antifraud.utilities.Role.*;


/**
 * Contains authentication and authorization controls and other
 * security related configurations
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .httpBasic(Customizer.withDefaults()) // enable default basic authentication
                .csrf(CsrfConfigurer::disable) // allows for post requests via Postman
                .headers(headers -> headers.frameOptions().disable())
                .exceptionHandling(handling -> {
                    handling.authenticationEntryPoint(new RestAuthenticationEntryPoint());
                    handling.accessDeniedHandler(new RestAccessDeniedHandler());
                })
                .userDetailsService(new UserDetailsServiceImpl())
                .authorizeHttpRequests(auth -> { // define authorization controls
                    // note: do not include the base path (/api) in the matcher; Spring Security assumes it by default
                    auth
                            // authorization required
                            .requestMatchers(HttpMethod.GET, "/api/auth/list/**").hasAnyAuthority(ADMINISTRATOR.getAuthority(), SUPPORT.getAuthority())
                            .requestMatchers(HttpMethod.DELETE, "/api/auth/user/{username}/**").hasAuthority(ADMINISTRATOR.getAuthority())
                            .requestMatchers(HttpMethod.PUT, "/api/auth/access/**").hasAuthority(ADMINISTRATOR.getAuthority())
                            .requestMatchers(HttpMethod.PUT, "/api/auth/role/**").hasAuthority(ADMINISTRATOR.getAuthority())
                            .requestMatchers(HttpMethod.GET, "/api/antifraud/history/**").hasAuthority(SUPPORT.getAuthority())
                            .requestMatchers(HttpMethod.POST, "/api/antifraud/transaction/**").hasAuthority(MERCHANT.getAuthority())
                            .requestMatchers(HttpMethod.PUT, "/api/antifraud/transaction/**").hasAuthority(SUPPORT.getAuthority())
                            .requestMatchers(HttpMethod.POST, "/api/antifraud/suspicious-ip").hasAuthority(SUPPORT.getAuthority())
                            .requestMatchers(HttpMethod.GET,  "/api/antifraud/suspicious-ip").hasAuthority(SUPPORT.getAuthority())
                            .requestMatchers(HttpMethod.DELETE,  "/api/antifraud/suspicious-ip/{ip}").hasAuthority(SUPPORT.getAuthority())
                            .requestMatchers(HttpMethod.POST, "/api/antifraud/stolencard").hasAuthority(SUPPORT.getAuthority())
                            .requestMatchers(HttpMethod.GET, "/api/antifraud/stolencard").hasAuthority(SUPPORT.getAuthority())
                            .requestMatchers(HttpMethod.DELETE, "/api/antifraud/stolencard/{number}").hasAuthority(SUPPORT.getAuthority())
                            // available to all users
                            .requestMatchers(HttpMethod.POST, "/api/auth/user/**").permitAll()
                            .requestMatchers("/error/**").permitAll()
                            .requestMatchers("/actuator/**").permitAll() // enable for testing purposes
                            .requestMatchers("/h2-console/**").permitAll() // enable h2 console to inspect db
                            // deny all other requests by default
                            .anyRequest().denyAll();
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // no session
                .build();
    }

    /**This class is used by the framework for retrieving user details during the authentication process.
     * */
    private class UserDetailsServiceImpl implements UserDetailsService {
        private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            logger.info("");
            return userRepository.findByUsername(username.toLowerCase())
                    .map(this::toUserDetails)
                    .orElseThrow(() -> {
                        logger.error("The following username does not exist: " + username);
                        return new UsernameNotFoundException("The following username does not exist: " + username);
                    });
        }

        private UserDetails toUserDetails(UserEntity entity) {

            return User.builder()
                    .username(entity.getUsername())
                    .password(entity.getPassword())
                    .authorities(Role.fetchStringAuthorityNullable(entity.getRole()))
                    .accountLocked(LockToggle.fetchLogicToggleNullable(entity.getAccountLockedInd()))
                    .accountExpired(false)
                    .credentialsExpired(false)
                    .disabled(false)
                    .build();
        }
    }

    /**This class is used for commencing the authentication process
     * when an unauthenticated client tries to access a secured resource.
     * */
    private class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        }
    }

    /**This class is used for handling the response when an authenticated client
     * attempts to access an unauthorized resource.
     * */
    private class RestAccessDeniedHandler implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
        }
    }

}
