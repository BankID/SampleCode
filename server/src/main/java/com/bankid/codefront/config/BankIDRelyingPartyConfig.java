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

package com.bankid.codefront.config;

import com.bankid.codefront.models.bankid.relyingparty.BankIDRequirements;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Represents the configuration settings for the BankID Relying Party API.
 */
@ConfigurationProperties("app.bankid.relying-party")
public class BankIDRelyingPartyConfig {
    private static final int DEFAULT_CONNECT_TIMEOUT = 5000;
    private static final int DEFAULT_READ_TIMEOUT = 5000;

    private BankIDRequirements authenticationRequirements;
    private BankIDRequirements signingRequirements;

    // TLS settings
    private String clientCertStorePath;
    private String clientCertStorePassword;
    private String clientCertStoreType = "PKCS12";
    private String trustStorePath;
    private String trustStorePassword;
    private String trustStoreType = "JKS";

    // Connection settings
    /**
     * Timeout for connecting in milliseconds.
     */
    private int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
    /**
     * Timeout for reading responses in milliseconds.
     */
    private int readTimeout = DEFAULT_READ_TIMEOUT;
    private String url;

    /**
     * Returns the authentication requirements.
     * @return the authentication requirements.
     */
    public BankIDRequirements getAuthenticationRequirements() {
        return this.authenticationRequirements;
    }

    /**
     * Sets the authentication requirements.
     * @param requirements the authentication requirements.
     */
    public void setAuthenticationRequirements(BankIDRequirements requirements) {
        this.authenticationRequirements = requirements;
    }

    /**
     * Returns the signing requirements.
     * @return the signing requirements.
     */
    public BankIDRequirements getSigningRequirements() {
        return this.signingRequirements;
    }

    /**
     * Sets the signing requirements.
     * @param requirements the signing requirements.
     */
    public void setSigningRequirements(BankIDRequirements requirements) {
        this.signingRequirements = requirements;
    }

    /**
     * Returns the path to the client certificate.
     * @return the path to the client certificate.
     */
    public String getClientCertStorePath() {
        return this.clientCertStorePath;
    }

    /**
     * Sets the path to the client certificate.
     * @param clientCertStorePath the path to the client certificate.
     */
    public void setClientCertStorePath(String clientCertStorePath) {
        this.clientCertStorePath = clientCertStorePath;
    }

    /**
     * Returns the password to the client certificate.
     * @return the password to the client certificate.
     */
    public String getClientCertStorePassword() {
        return this.clientCertStorePassword;
    }

    /**
     * Sets the password to the client certificate.
     * @param clientCertStorePassword the password to the client certificate.
     */
    public void setClientCertStorePassword(String clientCertStorePassword) {
        this.clientCertStorePassword = clientCertStorePassword;
    }

    /**
     * Returns the type of client certificate store.
     * @return the type of client certificate store.
     */
    public String getClientCertStoreType() {
        return this.clientCertStoreType;
    }

    /**
     * Sets the type of client certificate store.
     * @param clientCertStoreType the type of client certificate store.
     */
    public void setClientCertStoreType(String clientCertStoreType) {
        this.clientCertStoreType = clientCertStoreType;
    }

    /**
     * Returns the path to the trust store.
     * @return the path to the trust store.
     */
    public String getTrustStorePath() {
        return this.trustStorePath;
    }

    /**
     * Sets the path to the trust store.
     * @param trustStorePath the path to the trust store.
     */
    public void setTrustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
    }

    /**
     * Returns the password to the trust store.
     * @return the password to the trust store.
     */
    public String getTrustStorePassword() {
        return this.trustStorePassword;
    }

    /**
     * Sets the password to the trust store.
     * @param trustStorePassword the password to the trust store.
     */
    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    /**
     * Returns the type of the trust store.
     * @return the type of the trust store.
     */
    public String getTrustStoreType() {
        return this.trustStoreType;
    }

    /**
     * Sets the type of the trust store.
     * @param trustStoreType the type of the trust store.
     */
    public void setTrustStoreType(String trustStoreType) {
        this.trustStoreType = trustStoreType;
    }

    /**
     * Sets the BankID RP url.
     * @param url the BankID RP url.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Returns the BankID RP url.
     * @return the BankID RP url.
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * Returns the timeout for connecting in milliseconds.
     * @return the timeout for connecting in milliseconds.
     */
    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    /**
     * Sets the timeout for connecting in milliseconds.
     * @param timeout the timeout for connecting in milliseconds.
     */
    public void setConnectTimeout(int timeout) {
        this.connectTimeout = timeout;
    }


    /**
     * Returns the timeout for reading responses in milliseconds.
     * @return the timeout for reading responses in milliseconds.
     */
    public int getReadTimeout() {
        return this.readTimeout;
    }

    /**
     * Sets the timeout for reading responses in milliseconds.
     * @param timeout the timeout for reading responses in milliseconds.
     */
    public void setReadTimeout(int timeout) {
        this.readTimeout = timeout;
    }
}
