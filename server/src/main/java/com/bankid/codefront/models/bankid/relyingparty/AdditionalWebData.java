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

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Represents additional web page data from relying party.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdditionalWebData {
    private static final int REFERRING_DOMAIN_MIN_LENGTH = 3;
    private static final int USER_AGENT_MAX_LENGTH = 256;
    private static final int DEVICE_IDENTIFIER_MAX_LENGTH = 64;

    /**
     * The domain that start the BankID app.
     *
     * <p>Example: www.testbank.bankid.com</p>
     *
     * <p>String. 3 - 253 characters</p>
     */
    private final String referringDomain;
    /**
     * The user agent of the RP web page.
     * String. 1 - 256 characters
     */
    private final String userAgent;
    /**
     * The identifier of the device running the RP client. Use a web cookie or a hash of it.
     * String. 1 - 64 characters
     */
    private final String deviceIdentifier;

    /**
     * Initialize the object.
     *
     * @param referringDomain - The domain that start the BankID app.
     * @param userAgent - The user agent of the RP web page.
     * @param deviceIdentifier - The identifier of the device running the RP client.
     */
    public AdditionalWebData(String referringDomain, String userAgent, String deviceIdentifier) {
        if (referringDomain == null || referringDomain.length() < REFERRING_DOMAIN_MIN_LENGTH) {
            throw new IllegalArgumentException(
                "referringDomain cannot be null or smaller than " + REFERRING_DOMAIN_MIN_LENGTH
            );
        }
        if (userAgent == null || userAgent.isBlank()) {
            throw new IllegalArgumentException("userAgent cannot be null or empty");
        }
        if (deviceIdentifier == null || deviceIdentifier.isBlank()) {
            throw new IllegalArgumentException("deviceIdentifier cannot be null or empty");
        }

        this.referringDomain = referringDomain;

        // Trim user agent
        if (userAgent.length() > USER_AGENT_MAX_LENGTH) {
            userAgent = userAgent.substring(0, USER_AGENT_MAX_LENGTH);
        }

        this.userAgent = userAgent;

        // Trim device identifier
        if (deviceIdentifier.length() > DEVICE_IDENTIFIER_MAX_LENGTH) {
            deviceIdentifier = deviceIdentifier.substring(0, DEVICE_IDENTIFIER_MAX_LENGTH);
        }

        this.deviceIdentifier = deviceIdentifier;
    }

    /**
     * Returns the referring domain.
     * @return the referring domain.
     */
    public String getReferringDomain() {
        return this.referringDomain;
    }

    /**
     * Returns the user agent.
     * @return the user agent.
     */
    public String getUserAgent() {
        return this.userAgent;
    }

    /**
     * Returns the device identifier.
     * @return the device identifier.
     */
    public String getDeviceIdentifier() {
        return this.deviceIdentifier;
    }
}
