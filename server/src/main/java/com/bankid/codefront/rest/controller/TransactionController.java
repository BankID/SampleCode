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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Handle BankID transaction.
 */
@RestController
@RequestMapping("api/")
public class TransactionController {

    private final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    private final BankIDService bankIDService;
    private final TransactionControllerMetrics metrics;
    private final SessionValue sessionValue;
    private final Clock clock;

    /**
     * Initialize the Transaction Controller class.
     * @param bankIDService         the BankId Service.
     * @param metrics               the metrics helper.
     * @param sessionValue          the session value.
     * @param clock                 the clock.
     */
    public TransactionController(
        BankIDService bankIDService,
        TransactionControllerMetrics metrics,
        SessionValue sessionValue,
        Clock clock) {
        this.bankIDService = bankIDService;
        this.metrics = metrics;
        this.sessionValue = sessionValue;
        this.clock = clock;
    }

    /**
     * Start BankID auth transaction.
     *
     * @param body OBSERVE: Sent from client only for demonstration purposes,
     *             the values should be set by the backend in production code.
     * @param request httpRequest
     * @return Return autoStartToken and OrderRef.
     */
    @PostMapping("/authentication")
    public ResponseEntity<TransactionResponse> startAuthentication(
        HttpServletRequest request,
        @RequestBody AuthenticationRequest body
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

            String clientIp = request.getRemoteAddr();

            // Call service
            BankIDTransaction bankIDTransaction = this.bankIDService.authentication(
                    clientIp,
                    body.getUserVisibleData(),
                    body.getUserVisibleDataFormat(),
                    body.getUserNonVisibleData(),
                    body.getAllowFingerprint()
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

            throw exc;
        } catch (Exception exc) {
            this.logger.error("Failed to start authentication ({}): {}", exc.getClass().toString(), exc.toString());

            throw new CodeFrontWebApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "GENERAL_ERROR");
        }
    }

    /**
     * Start BankID sign transaction.
     *
     * @param body OBSERVE: Sent from client only for demonstration purposes,
     *             the values should be set by the backend in production code.
     * @param request httpRequest
     * @return Return autoStartToken and OrderRef.
     */
    @PostMapping("/sign")
    public ResponseEntity<TransactionResponse> startSign(HttpServletRequest request, @RequestBody SignRequest body) {
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

            // Call service
            BankIDTransaction bankIDTransaction = this.bankIDService.signing(
                    clientIp,
                    body.getUserVisibleData(),
                    body.getUserVisibleDataFormat(),
                    body.getUserNonVisibleData(),
                    body.getAllowFingerprint()
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

            throw exc;
        } catch (Exception exc) {
            this.logger.error("Failed to start sign ({}): {}", exc.getClass().toString(), exc.toString());

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
            this.logger.error("Failed to check transaction ({}): {}", exc.getClass().toString(), exc.toString());

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
            this.logger.error("Failed to cancel transaction ({}): {}", exc.getClass().toString(), exc.toString());

            throw new CodeFrontWebApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "GENERAL_ERROR");
        }
    }
}
