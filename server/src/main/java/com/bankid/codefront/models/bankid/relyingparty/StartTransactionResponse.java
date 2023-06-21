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

package com.bankid.codefront.models.bankid.relyingparty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents the response from the BankID RP API when creating an authentication or signature transaction.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StartTransactionResponse {

    private String autoStartToken;
    private String orderRef;
    private String qrStartToken;
    private String qrStartSecret;

    /**
     * Returns the auto start token.
     * @return the auto start token.
     */
    public String getAutoStartToken() {
        return this.autoStartToken;
    }

    /**
     * Sets the auto start token.
     * @param autoStartToken the auto start token.
     */
    public void setAutoStartToken(String autoStartToken) {
        this.autoStartToken = autoStartToken;
    }

    /**
     * Returns the order reference.
     * @return the order reference.
     */
    public String getOrderRef() {
        return this.orderRef;
    }

    /**
     * Sets the order reference.
     * @param orderRef the order reference.
     */
    public void setOrderRef(String orderRef) {
        this.orderRef = orderRef;
    }

    /**
     * Returns the QR start secret.
     * @return the QR start secret.
     */
    public String getQrStartSecret() {
        return this.qrStartSecret;
    }

    /**
     * Sets the QR start secret.
     * @param qrStartSecret the QR start secret.
     */
    public void setQrStartSecret(String qrStartSecret) {
        this.qrStartSecret = qrStartSecret;
    }

    /**
     * Returns the QR start token.
     * @return the QR start token.
     */
    public String getQrStartToken() {
        return this.qrStartToken;
    }

    /**
     * Sets the QR start token.
     * @param qrStartToken the QR start token.
     */
    public void setQrStartToken(String qrStartToken) {
        this.qrStartToken = qrStartToken;
    }
}
