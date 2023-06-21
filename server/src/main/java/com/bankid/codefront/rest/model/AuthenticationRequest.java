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

package com.bankid.codefront.rest.model;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Information to start an authentication transaction.
 *
 * <p>
 * OBSERVE: Sent from client only for demonstration purposes,
 * the values should be set by the backend in production code.
 * </p>
 */
public class AuthenticationRequest {

    private static final int AUTH_VISIBLE_MAX_LENGTH = 1500;
    private static final int AUTH_NON_VISIBLE_MAX_LENGTH = 1500;

    private String userVisibleData;
    private UserVisibleDataFormat userVisibleDataFormat;
    private String userNonVisibleData;
    private Boolean allowFingerprint;

    /**
     * Returns the user visible data.
     * @return the user visible data.
     */
    public String getUserVisibleData() {
        if (this.userVisibleData != null) {
            return this.userVisibleData.trim();
        }

        return null;
    }

    /**
     * Sets the user visible data.
     * @param userVisibleData the user visible data.
     */
    public void setUserVisibleData(String userVisibleData) {
        this.userVisibleData = userVisibleData;
    }

    /**
     * Returns the user visible data format.
     * @return the user visible data format.
     */
    public UserVisibleDataFormat getUserVisibleDataFormat() {
        return this.userVisibleDataFormat;
    }

    /**
     * Sets the user visible data format.
     * @param userVisibleDataFormat the user visible data format.
     */
    public void setUserVisibleDataFormat(UserVisibleDataFormat userVisibleDataFormat) {
        this.userVisibleDataFormat = userVisibleDataFormat;
    }

    /**
     * Returns the user non visible data.
     * @return the user non visible data.
     */
    public String getUserNonVisibleData() {
        if (this.userNonVisibleData != null) {
            return this.userNonVisibleData.trim();
        }

        return null;
    }

    /**
     * Sets the user non visible data.
     * @param userNonVisibleData the user non visible data.
     */
    public void setUserNonVisibleData(String userNonVisibleData) {
        this.userNonVisibleData = userNonVisibleData;
    }

    /**
     * Returns if allow fingerprint.
     * @return true if fingerprint is allowed.
     */
    public Boolean getAllowFingerprint() {
        return this.allowFingerprint;
    }

    /**
     * Sets allow fingerprint.
     * @param allowFingerprint true if allowed, false not allowed and null to use BankID default.
     */
    public void setAllowFingerprint(Boolean allowFingerprint) {
        this.allowFingerprint = allowFingerprint;
    }

    /**
     * Validate auth requst.
     *
     * @return true if valid.
     */
    public boolean validate() {
        // Check user visible data
        String userVisibleData = this.getUserVisibleData();
        if (userVisibleData != null && !userVisibleData.isEmpty()) {
            // Max length
            if (base64Encode(userVisibleData).length() > AUTH_VISIBLE_MAX_LENGTH) {
                return false;
            }
        }

        // Check user non visible data
        String userNonVisibleData = this.getUserNonVisibleData();
        if (userNonVisibleData != null && !userNonVisibleData.isEmpty()) {
            // Max length
            if (base64Encode(userNonVisibleData).length() > AUTH_NON_VISIBLE_MAX_LENGTH) {
                return false;
            }
        }

        return true;
    }

    /**
     * Base64 encode data.
     *
     * @param data  data to encode.
     * @return      encoded data.
     */
    protected String base64Encode(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }
}
