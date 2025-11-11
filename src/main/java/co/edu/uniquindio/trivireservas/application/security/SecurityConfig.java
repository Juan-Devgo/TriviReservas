package co.edu.uniquindio.trivireservas.application.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sesion ->
                        sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request ->
                        request
                                // Endpoints públicos
                                .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/lodgings/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll()

                                // Endpoints restringidos

                                // Usuarios

                                .requestMatchers(HttpMethod.GET, "/api/users").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/users/**/favorites").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/users/**/recommendations").authenticated()
                                .requestMatchers(HttpMethod.PATCH, "/api/users/**").authenticated()
                                .requestMatchers(HttpMethod.PATCH, "/api/users/{userUUID}/password").authenticated()

                                // Alojamientos
                                .requestMatchers(HttpMethod.POST, "/api/lodgings").hasRole("HOST")
                                .requestMatchers(HttpMethod.PUT, "/api/lodgings/**").hasRole("HOST")
                                .requestMatchers(HttpMethod.DELETE, "/api/lodgings/**").hasRole("HOST")
                                .requestMatchers(HttpMethod.PATCH, "/api/lodgings/**/comments").authenticated()
                                .requestMatchers(HttpMethod.PATCH, "/api/lodgings/**/comments/**/host_response").hasRole("HOST")

                                // Reservas
                                .requestMatchers(HttpMethod.GET, "/api/reservations/**").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/reservations/**").hasRole("USER")
                                .requestMatchers(HttpMethod.PATCH, "/api/reservations/**").authenticated()

                                // Resto de Endpoints
                                .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
