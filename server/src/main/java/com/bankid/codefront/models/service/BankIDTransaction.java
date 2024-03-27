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
package com.bankid.codefront.models.service;

import com.bankid.codefront.models.bankid.relyingparty.CollectResponse;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * The BankID transaction stored in session.
 */
@JsonSerialize
public class BankIDTransaction implements Serializable {

    private final String transactionId;
    private final String orderRef;
    private final String qrStartToken;
    private final String qrStartSecret;
    private final Instant startTime;
    private final String autoStartToken;
    private Status status;
    private Instant lastCollect;
    private CollectResponse lastCollectResponse;

    /**
     * Create a bankID transaction object.
     * @param orderRef the order ref.
     * @param qrStartToken the qr start token.
     * @param qrStartSecret the qr start secret.
     * @param autoStartToken the auto start token.
     * @param startTime the start time.
     */
    public BankIDTransaction(
        String orderRef,
        String qrStartToken,
        String qrStartSecret,
        String autoStartToken,
        Instant startTime) {
        this.transactionId = UUID.randomUUID().toString();
        this.orderRef = orderRef;
        this.qrStartToken = qrStartToken;
        this.qrStartSecret = qrStartSecret;
        this.startTime = startTime;
        this.autoStartToken = autoStartToken;
        this.status = Status.PENDING;
    }

    /**
     * Returns the transaction id.
     * @return the transaction id.
     */
    public String getTransactionId() {
        return this.transactionId;
    }

    /**
     * Returns the order ref.
     * @return the order ref.
     */
    public String getOrderRef() {
        return this.orderRef;
    }

    /**
     * Returns the QR start token.
     * @return the QR start token.
     */
    public String getQrStartToken() {
        return this.qrStartToken;
    }

    /**
     * Returns the QR start secret.
     * @return the QR start secret.
     */
    public String getQrStartSecret() {
        return this.qrStartSecret;
    }

    /**
     * Returns the start time of the transaction.
     * @return the start time of the transaction.
     */
    public Instant getStartTime() {
        return this.startTime;
    }

    /**
     * Returns the auto start token.
     * @return the auto start token.
     */
    public String getAutoStartToken() {
        return this.autoStartToken;
    }

    /**
     * Returns the status.
     * @return the status.
     */
    public Status getStatus() {
        return this.status;
    }

    /**
     * Sets status.
     * @param status last collect status.
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Returns last collect time.
     * @return last collect time.
     */
    public Instant getLastCollect() {
        return this.lastCollect;
    }

    /**
     * Sets last collect time.
     * @param lastCollect time for last collect.
     */
    public void setLastCollect(Instant lastCollect) {
        this.lastCollect = lastCollect;
    }

    /**
     * Returns last collect response.
     * @return last collect response.
     */
    public CollectResponse getLastCollectResponse() {
        return this.lastCollectResponse;
    }

    /**
     * Sets last collect response.
     * @param lastCollectResponse last collect response.
     */
    public void setLastCollectResponse(CollectResponse lastCollectResponse) {
        this.lastCollectResponse = lastCollectResponse;
    }
}
