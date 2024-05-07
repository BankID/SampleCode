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

import com.bankid.codefront.models.Base64String;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Represents a request to start a BankID RP authentication transaction.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StartAuthenticationRequest {
    private final String endUserIp;
    private String userVisibleData;
    private String userVisibleDataFormat;
    private String userNonVisibleData;
    /**
     * If this is set to true a risk indicator will be included in the collect response when the order completes.
     *
     * <p>Risk indicator will be more effective if endUserIp and additional data is sent correctly.
     * Your service will get information about risks regarding channel binding in the response
     * among other risk indicators.
     *
     * <p>Boolean. Default is false
     */
    private Boolean returnRisk;
    private BankIDRequirements requirement;
    private AdditionalWebData web;

    /**
     * Initialize the object.
     * @param endUserIp the information channel client IP address.
     */
    public StartAuthenticationRequest(String endUserIp) {
        this.endUserIp = endUserIp;

        // Return risk information in response
        this.returnRisk = true;
    }

    /**
     * Returns the information channel client IP address.
     * @return the information channel client IP address.
     */
    public String getEndUserIp() {
        return this.endUserIp;
    }

    /**
     * Returns the user visible data.
     * @return the user visible data.
     */
    public String getUserVisibleData() {
        return this.userVisibleData;
    }

    /**
     * Sets the user visible data.
     * @param userVisibleData the user visible data.
     */
    public void setUserVisibleData(Base64String userVisibleData) {
        this.userVisibleData = userVisibleData.getValue();
    }

    /**
     * Returns the user visible data format.
     * @return the user visible data format.
     */
    public String getUserVisibleDataFormat() {
        return this.userVisibleDataFormat;
    }

    /**
     * Sets the user visible data format.
     * @param userVisibleDataFormat the user visible data format.
     */
    public void setUserVisibleDataFormat(String userVisibleDataFormat) {
        if (userVisibleDataFormat != null
                && !userVisibleDataFormat.equals("simpleMarkdownV1")) {
            throw new IllegalArgumentException("Only null or simpleMarkdownV1 accepted.");
        }

        this.userVisibleDataFormat = userVisibleDataFormat;
    }

    /**
     * Returns the non visible data.
     * @return the non visible data.
     */
    public String getUserNonVisibleData() {
        return this.userNonVisibleData;
    }

    /**
     * Sets the non visible data.
     * @param userNonVisibleData the non visible data.
     */
    public void setUserNonVisibleData(Base64String userNonVisibleData) {
        this.userNonVisibleData = userNonVisibleData.getValue();
    }

    /**
     * Returns returnRisk.
     * @return true if risk information should be returned in collect.
     */
    public Boolean getReturnRisk() {
        return this.returnRisk;
    }

    /**
     * Sets returnRisk.
     * @param returnRisk should return risk information in collect.
     */
    public void setReturnRisk(Boolean returnRisk) {
        this.returnRisk = returnRisk;
    }

    /**
     * Returns the requirement.
     * @return the requirement.
     */
    public BankIDRequirements getRequirement() {
        return this.requirement;
    }

    /**
     * Sets the requirement.
     * @param requirement the requirement.
     */
    public void setRequirement(BankIDRequirements requirement) {
        this.requirement = requirement;
    }

    /**
     * Returns the additional web data.
     * @return the additional web data.
     */
    public AdditionalWebData getWeb() {
        return this.web;
    }

    /**
     * Sets the additional web data.
     * @param web the additional web data.
     */
    public void setWeb(AdditionalWebData web) {
        this.web = web;
    }
}
