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


import com.bankid.codefront.TestUtils;
import com.bankid.codefront.models.service.Status;
import com.bankid.codefront.rest.model.AuthenticationRequest;
import com.bankid.codefront.rest.model.SignRequest;
import com.bankid.codefront.rest.model.UserVisibleDataFormat;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test of TransactionController.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class TransactionControllerTest  extends RestBaseControllerTest {

    /**
     * Call auth with invalid data.
     */
    @Test
    public void authInvalidVisibleLength() {
        // Get CSRF-token
        HttpHeaders headers = getCsrfHeaders();

        AuthenticationRequest request = new AuthenticationRequest();
        request.setUserVisibleData(TestUtils.generateString(1501));

        // Start authentication
        ResponseEntity<String> response = this.getRestTemplate().exchange(
            "/api/authentication",
            HttpMethod.POST,
            new HttpEntity<>(request, headers),
            String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Call sign with invalid data.
     */
    @Test
    public void signMissingVisibleData() {
        // Get CSRF-token
        HttpHeaders headers = getCsrfHeaders();

        SignRequest request = new SignRequest();

        // Start authentication
        ResponseEntity<String> response = this.getRestTemplate().exchange(
            "/api/sign",
            HttpMethod.POST,
            new HttpEntity<>(request, headers),
            String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test authentication, start, check and cancel transaction.
     * Disabled by default. Only for manual testing.
     */
    // @Test
    public void authStartCheckAndCancelTransaction() {
        // Get CSRF-token
        HttpHeaders headers = getCsrfHeaders();

        // Start authentication
        ResponseEntity<AuthenticationResponse> response = this.getRestTemplate().exchange(
            "/api/authentication",
            HttpMethod.POST,
            new HttpEntity<>(new AuthenticationRequest(), headers),
            AuthenticationResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getAutoStartToken());

        List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        if (cookies != null) {
            cookies.forEach((cookie) -> {
                headers.add("Cookie", cookie);
            });
        }

        // Check status
        ResponseEntity<CheckResponse> collectResponse = this.getRestTemplate().exchange(
            "/api/check",
            HttpMethod.POST,
            new HttpEntity<String>(headers),
            CheckResponse.class);

        assertEquals(HttpStatus.OK, collectResponse.getStatusCode());
        assertNotNull(collectResponse.getBody());
        assertEquals(Status.PENDING, collectResponse.getBody().getStatus());
        assertNotNull(collectResponse.getBody().getQrCode());

        // Cancel transaction
        ResponseEntity<String> deleteResponse = this.getRestTemplate().exchange(
            "/api/cancel",
            HttpMethod.DELETE,
            new HttpEntity<String>(headers),
            String.class);

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
    }

    /**
     * Test sign, start, check and cancel transaction.
     * Disabled by default. Only for manual testing.
     */
    // @Test
    public void signStartCheckAndCancelTransaction() {
        // Get CSRF-token
        HttpHeaders headers = getCsrfHeaders();

        // Start signing
        SignRequest request = new SignRequest();
        request.setUserVisibleData("text to sign.");
        request.setUserVisibleDataFormat(UserVisibleDataFormat.SIMPLE_MARKDOWN_V1);
        request.setUserNonVisibleData("non visible data to sign.");
        ResponseEntity<AuthenticationResponse> response = this.getRestTemplate().exchange(
            "/api/sign",
            HttpMethod.POST,
            new HttpEntity<>(request, headers),
            AuthenticationResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getAutoStartToken());

        List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        if (cookies != null) {
            cookies.forEach((cookie) -> {
                headers.add("Cookie", cookie);
            });
        }

        // Check status
        ResponseEntity<CheckResponse> collectResponse = this.getRestTemplate().exchange(
            "/api/check",
            HttpMethod.POST,
            new HttpEntity<String>(headers),
            CheckResponse.class);

        assertEquals(HttpStatus.OK, collectResponse.getStatusCode());
        assertNotNull(collectResponse.getBody());
        assertEquals(Status.PENDING, collectResponse.getBody().getStatus());
        assertNotNull(collectResponse.getBody().getQrCode());

        // Cancel transaction
        ResponseEntity<String> deleteResponse = this.getRestTemplate().exchange(
            "/api/cancel",
            HttpMethod.DELETE,
            new HttpEntity<String>(headers),
            String.class);

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
    }
}
