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

import java.io.Serializable;

/**
 * This is a final state. The order was successful. The user has provided the security code and completed the order.
 * The completionData includes the signature, user information and the OCSP response.
 * RP should control the user information and continue their process.
 * RP should keep the completion data for future references/compliance/audit.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompletionData implements Serializable {

    private UserData user;
    private DeviceData device;
    private SetUpData setUp;
    private String bankIdIssueDate;
    private String signature;
    private String ocspResponse;
    private RiskLevel risk;

    /**
     * Returns the user data.
     * @return the user data.
     */
    public UserData getUser() {
        return this.user;
    }

    /**
     * Sets the user data.
     * @param user the user data.
     */
    public void setUser(UserData user) {
        this.user = user;
    }

    /**
     * Returns the device data.
     * @return the device data.
     */
    public DeviceData getDevice() {
        return this.device;
    }

    /**
     * Sets the device data.
     * @param device the device data.
     */
    public void setDevice(DeviceData device) {
        this.device = device;
    }

    /**
     * Returns the setup data.
     * @return the setup data.
     */
    public SetUpData getSetUp() {
        return this.setUp;
    }

    /**
     * Sets the setup data.
     * @param setUp - the setup data.
     */
    public void setSetUp(SetUpData setUp) {
        this.setUp = setUp;
    }

    /**
     * The date the BankID was issued to the user.
     * @return The date the BankID was issued to the user.
     */
    public String getBankIdIssueDate() {
        return this.bankIdIssueDate;
    }

    /**
     * Sets the date the BankID was issued to the user.
     * @param bankIdIssueDate - the date the BankID was issued to the user.
     */
    public void setBankIdIssueDate(String bankIdIssueDate) {
        this.bankIdIssueDate = bankIdIssueDate;
    }

    /**
     * Returns the signature.
     * @return the signature.
     */
    public String getSignature() {
        return this.signature;
    }

    /**
     * Sets the signature.
     * @param signature the signature.
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * Returns the OCSP response.
     * @return the OCSP response.
     */
    public String getOcspResponse() {
        return this.ocspResponse;
    }

    /**
     * Sets the OCSP response.
     * @param ocspResponse the OCSP response.
     */
    public void setOcspResponse(String ocspResponse) {
        this.ocspResponse = ocspResponse;
    }

    /**
     * Returns the risk level of the transaction.
     * @return the risk level.
     */
    public RiskLevel getRisk() {
        return this.risk;
    }
}
