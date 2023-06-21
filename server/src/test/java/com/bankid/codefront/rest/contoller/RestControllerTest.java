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
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test rest headers.
 */
public class RestControllerTest extends RestBaseControllerTest {
    /**
     * Validate response headers.
     */
    @Test
    public void checkHeaders() {
        ResponseEntity<StartResponse> startResponse = this.getRestTemplate().getForEntity(
            "/api/start",
            StartResponse.class
        );

        assertEquals("1; mode=block", startResponse.getHeaders().getFirst("X-XSS-Protection"));
        assertEquals("DENY", startResponse.getHeaders().getFirst("X-Frame-Options"));
        assertEquals("nosniff", startResponse.getHeaders().getFirst("X-Content-Type-Options"));
    }

    /**
     * Validate CSRF token.
     */
    @Test
    public void validateCSRF() {
        HttpHeaders headers = getCsrfHeaders();

        ResponseEntity<String> deleteResponse = this.getRestTemplate().exchange(
            "/api/cancel",
            HttpMethod.DELETE,
            new HttpEntity<String>(headers),
            String.class);

        assertEquals(HttpStatus.BAD_REQUEST, deleteResponse.getStatusCode());
    }

    /**
     * Test missing CSRF.
     */
    @Test
    public void missingCSRF() {
        ResponseEntity<String> deleteResponse = this.getRestTemplate().exchange(
            "/api/cancel",
            HttpMethod.DELETE,
            new HttpEntity<String>(new HttpHeaders()),
            String.class);

        assertEquals(HttpStatus.FORBIDDEN, deleteResponse.getStatusCode());
    }
}
