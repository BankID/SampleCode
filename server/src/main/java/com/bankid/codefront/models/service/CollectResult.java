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

/**
 * Collect result dto.
 */
public class CollectResult {
    private final Status status;
    private final String hintCode;
    private String qrCode;
    private BankIDTransaction transaction;
    private CompletionResult completionResult;

    /**
     * Create the collect result.
     * @param transaction the BankID transaction.
     * @param status the status.
     * @param hintCode the hint code.
     */
    public CollectResult(BankIDTransaction transaction, String status, String hintCode) {
        this.transaction = transaction;
        this.status = Status.fromString(status);
        this.hintCode = hintCode;
    }

    /**
     * Returns the qr code.
     * @return the qr code.
     */
    public String getQrCode() {
        return this.qrCode;
    }

    /**
     * Sets the qr code.
     * @param qrCode the qr code.
     */
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    /**
     * Returns the status.
     * @return the status.
     */
    public Status getStatus() {
        return this.status;
    }

    /**
     * Returns the hint code.
     * @return the hint code.
     */
    public String getHintCode() {
        return this.hintCode;
    }

    /**
     * Returns the BankID transaction.
     * @return the BankID transaction.
     */
    public BankIDTransaction getTransaction() {
        return this.transaction;
    }

    /**
     * Sets the BankID transaction.
     * @param transaction the BankID transaction.
     */
    public void setTransaction(BankIDTransaction transaction) {
        this.transaction = transaction;
    }

    /**
     * Returns the completion data.
     * @return the completion data.
     */
    public CompletionResult getCompletionResult() {
        return this.completionResult;
    }

    /**
     * Sets the completion data.
     * @param completionResult the completion data.
     */
    public void setCompletionResult(CompletionResult completionResult) {
        this.completionResult = completionResult;
    }
}
