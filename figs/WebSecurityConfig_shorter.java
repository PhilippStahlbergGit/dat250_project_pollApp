
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        security
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // A) Allow anyone to register a new user
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                //B)  Allow authenticated users to vote
                .requestMatchers(HttpMethod.POST, "/vote/**").authenticated()
                // C) secure poll creation for ADMIN's only
                .requestMatchers(new AntPathRequestMatcher("/polls/**", HttpMethod.POST.name())).hasRole("ADMIN")
		// D) secure poll deletion for ADMIN's only
		.requestMatchers(new AntPathRequestMatcher("/polls/**", HttpMethod.DELETE.name())).hasRole("ADMIN")
                // All other requests need authentication (for safety purposes)
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        return security.build();
    }    