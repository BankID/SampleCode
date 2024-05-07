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

package com.bankid.codefront.rest.controller;

import com.bankid.codefront.config.AppConfig;
import com.bankid.codefront.models.service.BankIDTransaction;
import com.bankid.codefront.models.service.CollectResult;
import com.bankid.codefront.models.service.Status;
import com.bankid.codefront.rest.controller.metrics.TransactionControllerMetrics;
import com.bankid.codefront.rest.model.AuthenticationRequest;
import com.bankid.codefront.rest.model.CheckResponse;
import com.bankid.codefront.rest.model.SessionValue;
import com.bankid.codefront.rest.model.SignRequest;
import com.bankid.codefront.rest.model.TransactionResponse;
import com.bankid.codefront.service.BankIDService;
import com.bankid.codefront.utils.CodeFrontWebApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Handle BankID transaction.
 */
@RestController
@RequestMapping("api/")
public class TransactionController {
    private static final String DEVICE_COOKIE_NAME = "__Secure-Device";
    private static final int DEVICE_COOKIE_TTL = 399 * 24 * 60 * 60;

    private final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    private final BankIDService bankIDService;
    private final TransactionControllerMetrics metrics;
    private final SessionValue sessionValue;
    private final AppConfig appConfig;
    private final Clock clock;

    /**
     * Initialize the Transaction Controller class.
     * @param bankIDService         the BankId Service.
     * @param metrics               the metrics helper.
     * @param sessionValue          the session value.
     * @param appConfig             the app configuration.
     * @param clock                 the clock.
     */
    public TransactionController(
        BankIDService bankIDService,
        TransactionControllerMetrics metrics,
        SessionValue sessionValue,
        AppConfig appConfig,
        Clock clock) {
        this.bankIDService = bankIDService;
        this.metrics = metrics;
        this.sessionValue = sessionValue;
        this.appConfig = appConfig;
        this.clock = clock;
    }

    /**
     * Start BankID auth transaction.
     *
     * @param body OBSERVE: Sent from client only for demonstration purposes,
     *             the values should be set by the backend in production code.
     * @param request httpRequest
     * @param response httpResponse
     * @param deviceIdentifier the identifier of the used device.
     * @return Return autoStartToken and OrderRef.
     */
    @PostMapping("/authentication")
    public ResponseEntity<TransactionResponse> startAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody AuthenticationRequest body,
        @CookieValue(value = DEVICE_COOKIE_NAME, defaultValue = "", required = false) String deviceIdentifier
    ) {
        long startTime = System.currentTimeMillis();

        try {
            // If session exists, change session id to protect against session fixation
            if (request.getSession(false) != null) {
                request.changeSessionId();
            }

            // If an ongoing BankId transaction exists cancel it.
            if (
                this.sessionValue.getBankIDTransaction() != null
                    && this.sessionValue.getBankIDTransaction().getStatus() == Status.PENDING
            ) {
                this.cancelOngoingTransaction();
            }

            // Validate input
            if (body == null || !body.validate()) {
                throw new CodeFrontWebApplicationException(HttpStatus.BAD_REQUEST, "Invalid input");
            }

            checkDomain(request.getHeader(HttpHeaders.HOST));

            String clientIp = request.getRemoteAddr();

            String deviceId = getOrCreateDeviceIdentifier(response, deviceIdentifier);

            // User agent
            String userAgent = request.getHeader("User-Agent");

            // Call service
            BankIDTransaction bankIDTransaction = this.bankIDService.authentication(
                clientIp,
                body.getUserVisibleData(),
                body.getUserVisibleDataFormat(),
                body.getUserNonVisibleData(),
                body.getAllowFingerprint(),
                userAgent,
                deviceId
            );

            if (bankIDTransaction == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            // Store transaction in session
            this.sessionValue.setAuthTransaction(true);
            this.sessionValue.setBankIDTransaction(bankIDTransaction);

            // Add metrics
            this.metrics.successStartAuthentication(System.currentTimeMillis() - startTime);

            // Return values
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new TransactionResponse(
                        bankIDTransaction.getTransactionId(),
                        bankIDTransaction.getAutoStartToken()
                    ));
        } catch (CodeFrontWebApplicationException exc) {
            this.logger.info("CodeFrontWebApplicationException while authenticating the user: {} ", exc.toString());
            this.metrics.failedStartAuthentication((System.currentTimeMillis() - startTime));

            throw exc;
        } catch (Exception exc) {
            this.logger.error("Failed to start authentication ({}): {}", exc.getClass(), exc.toString());
            this.metrics.failedStartAuthentication((System.currentTimeMillis() - startTime));

            throw new CodeFrontWebApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "GENERAL_ERROR");
        }
    }

    /**
     * Start BankID sign transaction.
     *
     * @param body OBSERVE: Sent from client only for demonstration purposes,
     *             the values should be set by the backend in production code.
     * @param request httpRequest
     * @param response httpResponse
     * @param deviceIdentifier the identifier of the used device.
     * @return Return autoStartToken and OrderRef.
     */
    @PostMapping("/sign")
    public ResponseEntity<TransactionResponse> startSign(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody SignRequest body,
        @CookieValue(value = DEVICE_COOKIE_NAME, defaultValue = "", required = false) String deviceIdentifier
    ) {
        long startTime = System.currentTimeMillis();
        String clientIp = request.getRemoteAddr();

        try {
            // If session exists, change session id to protect against session fixation
            if (request.getSession(false) != null) {
                request.changeSessionId();
            }

            // If an ongoing BankId transaction exists cancel it.
            if (
                this.sessionValue.getBankIDTransaction() != null
                && this.sessionValue.getBankIDTransaction().getStatus() == Status.PENDING
            ) {
                this.cancelOngoingTransaction();
            }

            // Validate input
            if (body == null || !body.validate()) {
                throw new CodeFrontWebApplicationException(HttpStatus.BAD_REQUEST, "Invalid input");
            }

            checkDomain(request.getHeader(HttpHeaders.HOST));

            String deviceId = getOrCreateDeviceIdentifier(response, deviceIdentifier);

            // User agent
            String userAgent = request.getHeader("User-Agent");

            // Call service
            BankIDTransaction bankIDTransaction = this.bankIDService.signing(
                clientIp,
                body.getUserVisibleData(),
                body.getUserVisibleDataFormat(),
                body.getUserNonVisibleData(),
                body.getAllowFingerprint(),
                userAgent,
                deviceId
            );

            if (bankIDTransaction == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            // Store transaction in session
            this.sessionValue.setAuthTransaction(false);
            this.sessionValue.setBankIDTransaction(bankIDTransaction);

            // Add metrics
            this.metrics.successStartSign((System.currentTimeMillis() - startTime));

            // Return values
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new TransactionResponse(
                        bankIDTransaction.getTransactionId(),
                        bankIDTransaction.getAutoStartToken()
                    ));
        } catch (CodeFrontWebApplicationException exc) {
            this.logger.info("CodeFrontWebApplicationException while signing: {}", exc.toString());
            this.metrics.failedStartSign((System.currentTimeMillis() - startTime));

            throw exc;
        } catch (Exception exc) {
            this.logger.error("Failed to start sign ({}): {}", exc.getClass(), exc.toString());
            this.metrics.failedStartSign((System.currentTimeMillis() - startTime));

            throw new CodeFrontWebApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "GENERAL_ERROR");
        }
    }

    /**
     * Check transaction status.
     * @return Return status of the transaction.
     */
    @PostMapping("/check")
    public ResponseEntity<CheckResponse> checkTransaction() {
        long startTime = System.currentTimeMillis();

        try {
            // Validate input
            if (this.sessionValue == null || this.sessionValue.getBankIDTransaction() == null) {
                this.logger.trace("Failed to check transaction: Session not found");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            BankIDTransaction sessionTransaction = this.sessionValue.getBankIDTransaction();

            // Call service
            CollectResult collectResult = this.bankIDService.collect(this.sessionValue.getBankIDTransaction());
            if (collectResult == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            this.sessionValue.setBankIDTransaction(collectResult.getTransaction());

            // Add metrics
            this.metrics.successCollect(System.currentTimeMillis() - startTime);
            if (collectResult.getStatus() == Status.COMPLETE) {
                this.metrics.finalizedTransaction(sessionTransaction.getStartTime()
                        .until(Instant.now(this.clock), ChronoUnit.SECONDS));
            }
            if (collectResult.getStatus() == Status.FAILED) {
                this.metrics.failedTransaction(sessionTransaction.getStartTime()
                        .until(Instant.now(this.clock), ChronoUnit.SECONDS));
            }

            return ResponseEntity.status(HttpStatus.OK).body(new CheckResponse(
                collectResult.getTransaction().getTransactionId(),
                collectResult
            ));
        } catch (CodeFrontWebApplicationException exc) {
            this.logger.info("CodeFrontWebApplicationException while checking transaction: {}", exc.toString());

            throw exc;
        } catch (Exception exc) {
            this.logger.error("Failed to check transaction ({}): {}", exc.getClass(), exc.toString());

            throw new CodeFrontWebApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "GENERAL_ERROR");
        }
    }

    /**
     * Cancel ongoing BankID transaction.
     * @return Return status
     */
    @DeleteMapping("/cancel")
    public ResponseEntity<Void> cancelTransaction() {
        if (this.cancelOngoingTransaction()) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private boolean cancelOngoingTransaction() {
        // Validate input
        if (this.sessionValue == null || this.sessionValue.getBankIDTransaction() == null) {
            this.logger.trace("Failed to cancel transaction: Session not found");

            return false;
        }

        try {
            // Always return OK to user.
            this.bankIDService.cancelTransaction(this.sessionValue.getBankIDTransaction().getOrderRef());

            // Remove transaction
            this.sessionValue.setBankIDTransaction(null);

            return true;
        } catch (CodeFrontWebApplicationException exc) {
            this.logger.info("CodeFrontWebApplicationException while cancel transaction: {}", exc.toString());

            throw exc;
        } catch (Exception exc) {
            this.logger.error("Failed to cancel transaction ({}): {}", exc.getClass(), exc.toString());

            throw new CodeFrontWebApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "GENERAL_ERROR");
        }
    }

    /**
     * Check that the user is accessing the site with same domain as the configuration.
     * @param hostHeader host header in request.
     */
    private void checkDomain(String hostHeader) {
        // Check if host header and configured domain match
        try {
            URI hostUri = URI.create(hostHeader);
            if (!this.appConfig.getDomain().equalsIgnoreCase(hostUri.getHost())) {
                this.logger.warn("Host header doesn't match configured host. Header: {}", hostHeader);
                this.metrics.domainMismatch();
            }
        } catch (Exception e) {
            this.logger.error("Failed to parse host header: {}", e.getMessage());
        }
    }

    /**
     * Get or create a device identifier and create or update the cookie.
     * @param response httpResponse
     * @param deviceIdentifier the identifier of the used device.
     *
     * @return the device identifier.
     */
    private String getOrCreateDeviceIdentifier(HttpServletResponse response, String deviceIdentifier) {
        // Check if valid uuid or create a new.
        try {
            deviceIdentifier = UUID.fromString(deviceIdentifier).toString();
        } catch (IllegalArgumentException e) {
            deviceIdentifier = UUID.randomUUID().toString();
        }

        ResponseCookie responseCookie = ResponseCookie
            .from(DEVICE_COOKIE_NAME, deviceIdentifier)
            .secure(true)
            .httpOnly(true)
            .path("/")
            .maxAge(DEVICE_COOKIE_TTL)
            .sameSite("Strict")
            .build();

        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        return deviceIdentifier;
    }
}
