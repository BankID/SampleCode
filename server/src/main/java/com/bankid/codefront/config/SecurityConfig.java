/*
BSD 3-Clause License

Copyright (c) 2022, Finansiell ID-Teknik BID AB
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

3. Neither the name of the copyright holder nor the names of its
   contributors may be used to endorse or promote products derived from
   this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/

package com.bankid.codefront.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

/**
 * WebSecurity config.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final int STRICT_TRANSPORT_MAX_AGE = 31536000;
    private static final String CSRF_HEADER_NAME = "X-CSRF-TOKEN";

    private final HeadersConfig headersConfig;

    /**
     * Initialize the Security Config class.
     * @param headersConfig     the headers config.
     */
    public SecurityConfig(HeadersConfig headersConfig) {
        this.headersConfig = headersConfig;
    }

    /**
     * Setup security filter.
     * @param http
     * @return the filter chain.
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CsrfTokenRepository csrfTokenRepository = createCsrfTokenRepository();
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();

        http.csrf(
            csrfConfigurer -> csrfConfigurer
                .csrfTokenRepository(csrfTokenRepository)
                .csrfTokenRequestHandler(requestHandler)
        );
        http.authorizeHttpRequests((requests) -> requests
            .anyRequest().permitAll()
        );
        http.headers(
            headersConfig ->
                headersConfig
                    .httpStrictTransportSecurity(
                        transportSecurity -> transportSecurity
                            .includeSubDomains(true)
                            .maxAgeInSeconds(STRICT_TRANSPORT_MAX_AGE)
                    )
                    .xssProtection(
                        xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
                    )
                    .contentSecurityPolicy(csp -> csp.policyDirectives(this.headersConfig.getCsp()))
            );
        return http.build();
    }

    /**
     * Create Csrf token repository bean.
     * @return the csrfTokenRepository.
     */
    @Bean
    public CsrfTokenRepository createCsrfTokenRepository() {
        HttpSessionCsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();
        csrfTokenRepository.setHeaderName(CSRF_HEADER_NAME);

        return csrfTokenRepository;
    }
}
