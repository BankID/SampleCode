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

package com.bankid.codefront.rest.contoller;

import com.bankid.codefront.rest.model.StartResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Base class for rest tests.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"dev", "tlsdisabled"})
public abstract class RestBaseControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Get the rest template.
     *
     * @return the rest template.
     */
    protected TestRestTemplate getRestTemplate() {
        return this.restTemplate;
    }

    /**
     * Get CSRF-token from response headers and create new headers with csrf-token.
     * @return  headers with csrf-token.
     */
    protected HttpHeaders getCsrfHeaders() {
        ResponseEntity<StartResponse> startResponse = this.getRestTemplate().getForEntity(
            "/api/start",
            StartResponse.class
        );
        assertEquals(HttpStatus.OK, startResponse.getStatusCode());

        String cookieHeaders = startResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

        // Extract CSRF
        assertNotNull(startResponse.getBody());

        String csrf = startResponse.getBody().getCsrfToken();
        assertFalse(csrf.isEmpty());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookieHeaders);
        headers.add("X-CSRF-TOKEN", csrf);

        return headers;
    }
}
