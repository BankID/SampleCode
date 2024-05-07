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

package com.bankid.codefront.service;

import com.bankid.codefront.bankid.relyingparty.RpApi;
import com.bankid.codefront.bankid.relyingparty.signature.DigitalSignature;
import com.bankid.codefront.bankid.relyingparty.signature.SignatureParseException;
import com.bankid.codefront.config.AppConfig;
import com.bankid.codefront.config.BankIDRelyingPartyConfig;
import com.bankid.codefront.models.Base64String;
import com.bankid.codefront.models.bankid.relyingparty.AdditionalWebData;
import com.bankid.codefront.models.bankid.relyingparty.BankIDRequirements;
import com.bankid.codefront.models.bankid.relyingparty.CollectResponse;
import com.bankid.codefront.models.bankid.relyingparty.RiskRequirement;
import com.bankid.codefront.models.bankid.relyingparty.StartAuthenticationRequest;
import com.bankid.codefront.models.bankid.relyingparty.StartSignatureRequest;
import com.bankid.codefront.models.bankid.relyingparty.StartTransactionResponse;
import com.bankid.codefront.models.service.BankIDTransaction;
import com.bankid.codefront.models.service.CollectResult;
import com.bankid.codefront.models.service.CompletionResult;
import com.bankid.codefront.models.service.Status;
import com.bankid.codefront.rest.model.UserVisibleDataFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

/**
 * Business logic for BankID.
 */
@Service
public class BankIDService {

    private static final String ALGORITHM = "HmacSHA256";

    private final Logger logger = LoggerFactory.getLogger(BankIDService.class);
    private final RpApi rpApi;
    private final BankIDRelyingPartyConfig bankIDRelyingPartyConfig;
    private final AppConfig appConfig;
    private final AuditService auditService;
    private final Clock clock;

    /**
     * Initialize the BankID Service class.
     * @param rpApi         the bankId api.
     * @param bankIDRelyingPartyConfig the bankId config.
     * @param appConfig     the app config.
     * @param auditService  the auditService.
     * @param clock         the clock used for easier testing.
     */
    public BankIDService(
        RpApi rpApi,
        BankIDRelyingPartyConfig bankIDRelyingPartyConfig,
        AppConfig appConfig,
        AuditService auditService,
        Clock clock
    ) {
        this.rpApi = rpApi;
        this.bankIDRelyingPartyConfig = bankIDRelyingPartyConfig;
        this.appConfig = appConfig;
        this.auditService = auditService;
        this.clock = clock;

        // Check that HmacSHA256 exists
        try {
            Mac.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            this.logger.error("{} algorithm missing.", ALGORITHM);

            throw new RuntimeException(ALGORITHM + " algorithm missing.", e);
        }
    }

    /**
     * Start BankID authentication.
     * @param clientIp client ip.
     * @param userVisibleData optional text visible to user,
     * @param userVisibleDataFormat format of text visible to user.
     * @param userNonVisibleData optional non visible data.
     * @param pinCode require pinCode for authentication.
     * @param userAgent the user agent of the web page.
     * @param deviceIdentifier deviceIdentifier.
     * @return the transaction.
     */
    public BankIDTransaction authentication(
        String clientIp,
        String userVisibleData,
        UserVisibleDataFormat userVisibleDataFormat,
        String userNonVisibleData,
        Boolean pinCode,
        String userAgent,
        String deviceIdentifier
    ) {
        StartAuthenticationRequest
            authenticationRequest = new StartAuthenticationRequest(clientIp);

        // Include additional web data
        AdditionalWebData webData = new AdditionalWebData(this.appConfig.getDomain(), userAgent, deviceIdentifier);
        authenticationRequest.setWeb(webData);

        BankIDRequirements bankIDRequirements = this.bankIDRelyingPartyConfig.getAuthenticationRequirements();
        if (bankIDRequirements == null) {
            bankIDRequirements = new BankIDRequirements();
        }

        // Only allow transactions with classified with risk low
        bankIDRequirements.setRisk(RiskRequirement.LOW);

        bankIDRequirements.setPinCode(pinCode);
        authenticationRequest.setRequirement(bankIDRequirements);

        // Check if userVisibleData should be included.
        if (userVisibleData != null && !userVisibleData.isEmpty()) {
            authenticationRequest.setUserVisibleData(
                    new Base64String(
                            base64Encode(userVisibleData)));

            if (userVisibleDataFormat != null) {
                authenticationRequest.setUserVisibleDataFormat(userVisibleDataFormat.getValue());
            }
        }

        if (userNonVisibleData != null && !userNonVisibleData.isEmpty()) {
            authenticationRequest.setUserNonVisibleData(
                    new Base64String(
                            base64Encode(userNonVisibleData)));
        }

        StartTransactionResponse startTransaction = this.rpApi.startAuthentication(authenticationRequest);

        if (startTransaction == null) {
            this.logger.info("Failed to start authentication.");
            return null;
        }

        return new BankIDTransaction(
            startTransaction.getOrderRef(),
            startTransaction.getQrStartToken(),
            startTransaction.getQrStartSecret(),
            startTransaction.getAutoStartToken(),
            Instant.now(this.clock)
        );
    }

    /**
     * Start BankID signing.
     * @param clientIp client ip.
     * @param userVisibleData text visible to user,
     * @param userVisibleDataFormat format of text visible to user.
     * @param userNonVisibleData optional non visible data.
     * @param pinCode require pinCode for signing.
     * @param userAgent the user agent of the web page.
     * @param deviceIdentifier deviceIdentifier.
     * @return the transaction.
     */
    public BankIDTransaction signing(
        String clientIp,
        String userVisibleData,
        UserVisibleDataFormat userVisibleDataFormat,
        String userNonVisibleData,
        Boolean pinCode,
        String userAgent,
        String deviceIdentifier
    ) {
        StartSignatureRequest signatureRequest =
            new StartSignatureRequest(clientIp, new Base64String(base64Encode(userVisibleData)));

        // Include additional web data
        AdditionalWebData webData = new AdditionalWebData(this.appConfig.getDomain(), userAgent, deviceIdentifier);
        signatureRequest.setWeb(webData);

        BankIDRequirements bankIDRequirements = this.bankIDRelyingPartyConfig.getSigningRequirements();
        if (bankIDRequirements == null) {
            bankIDRequirements = new BankIDRequirements();
        }

        // Only allow transactions with classified with risk low
        bankIDRequirements.setRisk(RiskRequirement.LOW);

        bankIDRequirements.setPinCode(pinCode);

        signatureRequest.setRequirement(bankIDRequirements);

        if (userVisibleDataFormat != null) {
            signatureRequest.setUserVisibleDataFormat(userVisibleDataFormat.getValue());
        }

        if (userNonVisibleData != null && !userNonVisibleData.isEmpty()) {
            signatureRequest.setUserNonVisibleData(new Base64String(base64Encode(userNonVisibleData)));
        }

        StartTransactionResponse startTransaction = this.rpApi.startSignature(signatureRequest);

        if (startTransaction == null) {
            this.logger.info("Failed to start signing.");
            return null;
        }

        return new BankIDTransaction(
            startTransaction.getOrderRef(),
            startTransaction.getQrStartToken(),
            startTransaction.getQrStartSecret(),
            startTransaction.getAutoStartToken(),
            Instant.now(this.clock)
        );
    }

    /**
     * Collect BankID transaction status.
     * @param transaction BankID orderRef.
     * @return the status
     */
    public CollectResult collect(BankIDTransaction transaction) throws SignatureParseException {
        CollectResponse collectResponse;

        if (shouldCallBankIDCollect(transaction)) {
             collectResponse = this.rpApi.collect(transaction.getOrderRef());
            // Failed to collect
            if (collectResponse == null) {
                return null;
            }

            Status status = Status.fromString(collectResponse.getStatus());

            // Store collectResponse if status is complete future references/compliance/audit.
            if (status == Status.COMPLETE) {
                this.auditService.logCollectResponse(collectResponse);
            }

             transaction.setLastCollectResponse(collectResponse);
             transaction.setLastCollect(Instant.now(this.clock));
             transaction.setStatus(status);
        } else {
            collectResponse = transaction.getLastCollectResponse();
        }

        CollectResult collectModel = new CollectResult(
            transaction,
            collectResponse.getStatus(),
            collectResponse.getHintCode()
        );

        // Only generate qr-code for pending transaction with hintCode outstandingTransaction
        if (collectModel.getStatus() == Status.PENDING
            && "outstandingTransaction".equals(collectModel.getHintCode())) {
            collectModel.setQrCode(createQRData(transaction));
        }

        if (collectModel.getStatus() == Status.COMPLETE) {
            String name = collectResponse.getCompletionData().getUser().getName();
            String personalNumber = collectResponse.getCompletionData().getUser().getPersonalNumber();

            Base64String xmlSigB64 = new Base64String(collectResponse.getCompletionData().getSignature());

            // Parse the digital signature to retrieve more information.
            DigitalSignature digSig = new DigitalSignature(xmlSigB64);

            String visibleData = null;

            // Visible data may be empty
            if (digSig.getUserVisibleData() != null && !digSig.getUserVisibleData().isEmpty()) {
                visibleData = new String(Base64.getDecoder().decode(digSig.getUserVisibleData()),
                    StandardCharsets.UTF_8);
            }

            // Return a completion result
            collectModel.setCompletionResult(
                new CompletionResult(name, personalNumber, visibleData)
            );
        }

        return collectModel;
    }

    /**
     * Cancel a BankID transaction.
     * @param orderRef BankID orderRef.
     * @return true if cancel was successful.
     */
    public boolean cancelTransaction(String orderRef) {
        return this.rpApi.cancel(orderRef);
    }

    /**
     * Check if it's time to call collect in the RP api.
     * @param transaction BankIDTransaction.
     * @return true if time to call api.
     */
    private boolean shouldCallBankIDCollect(BankIDTransaction transaction) {
        // First collect
        if (transaction.getLastCollectResponse() == null || transaction.getLastCollect() == null) {
            return true;
        }

        // Time since last collect
        long secondsSinceLastCheck = transaction.getLastCollect().until(Instant.now(this.clock), ChronoUnit.SECONDS);

        // Only call RP api every two seconds and if last result was pending.
        return secondsSinceLastCheck > 1
            && transaction.getLastCollectResponse().getStatus().equalsIgnoreCase("pending");
    }

    /**
     * Create the QR data.
     * @param transaction BankIDTransaction.
     * @return the generated qr data.
     */
    private String createQRData(BankIDTransaction transaction) {
        try {
            String qrTime = Long.toString(
                transaction.getStartTime().until(Instant.now(this.clock), ChronoUnit.SECONDS)
            );

            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(new SecretKeySpec(transaction.getQrStartSecret().getBytes(StandardCharsets.US_ASCII), ALGORITHM));
            mac.update(qrTime.getBytes(StandardCharsets.US_ASCII));

            // Example of generated auth code from the RP guideline
            // "dc69358e712458a66a7525beef148ae8526b1c71610eff2c16cdffb4cdac9bf8" (qr_time="0")
            // "949d559bf23403952a94d103e67743126381eda00f0b3cbddbf7c96b1adcbce2" (qr_time="1")
            // "a9e5ec59cb4eee4ef4117150abc58fad7a85439a6a96ccbecc3668b41795b3f3" (qr_time="2")
            String qrAuthCode = String.format("%064x", new BigInteger(1, mac.doFinal()));

            // Example of generated qr-data from the RP guideline
            // "bankid.67df3917-fa0d-44e5-b327-edcc928297f8.0.
            // dc69358e712458a66a7525beef148ae8526b1c71610eff2c16cdffb4cdac9bf8" (qr_time="0")
            // "bankid.67df3917-fa0d-44e5-b327-edcc928297f8.1.
            // 949d559bf23403952a94d103e67743126381eda00f0b3cbddbf7c96b1adcbce2" (qr_time="1")
            // "bankid.67df3917-fa0d-44e5-b327-edcc928297f8.2.
            // a9e5ec59cb4eee4ef4117150abc58fad7a85439a6a96ccbecc3668b41795b3f3" (qr_time="2")
            return String.join(".", "bankid", transaction.getQrStartToken(), qrTime, qrAuthCode);
        } catch (NoSuchAlgorithmException e) {
            this.logger.error("{} algorithm is missing.", ALGORITHM);
        } catch (InvalidKeyException e) {
            this.logger.error("Invalid qr start secret.");
        }

        return null;
    }

    private String base64Encode(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }
}
