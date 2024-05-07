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

package com.bankid.codefront.bankid.relyingparty;

import com.bankid.codefront.bankid.relyingparty.metrics.RpApiMetrics;
import com.bankid.codefront.config.BankIDRelyingPartyConfig;
import com.bankid.codefront.models.bankid.relyingparty.CancelRequest;
import com.bankid.codefront.models.bankid.relyingparty.CollectRequest;
import com.bankid.codefront.models.bankid.relyingparty.CollectResponse;
import com.bankid.codefront.models.bankid.relyingparty.StartAuthenticationRequest;
import com.bankid.codefront.models.bankid.relyingparty.StartSignatureRequest;
import com.bankid.codefront.models.bankid.relyingparty.StartTransactionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyStore;
import java.time.Duration;

/**
 * This class represents the BankID Relying Party API endpoint.
 */
@Component
public class RpApi {
    private final Logger logger = LoggerFactory.getLogger(RpApi.class);

    private final BankIDRelyingPartyConfig settings;
    private final RpApiMetrics metrics;
    private final SSLContext sslContext;
    private final HttpClient httpClient;

    /**
     * Initialize the Relying Party API class.
     * @param settings the configuration.
     * @param metrics  the metrics helper.
     */
    public RpApi(BankIDRelyingPartyConfig settings, RpApiMetrics metrics) {
        this.settings = settings;
        this.metrics = metrics;

        this.sslContext = newSslContext();
        this.httpClient = getClient();
    }

    /**
     * Check that communication with BankID RP works.
     * @return true if communication works.
     */
    public boolean checkConnectionWorks() {
        try {
            long startTime = System.currentTimeMillis();

            HttpRequest request = this.getRequestBuilder("auth")
                .GET()
                .build();

            // Send request
            HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int responseCode = response.statusCode();

            if (responseCode != HttpStatus.METHOD_NOT_ALLOWED.value()) {
                this.logger.warn("Got HTTP error from BankID RP: {}", responseCode);

                this.metrics.failedConnectionCheck(System.currentTimeMillis() - startTime);

                return false;
            }
            this.metrics.successConnectionCheck(System.currentTimeMillis() - startTime);
            return true;
        } catch (Exception exc) {
            this.logger.warn(
                    "Got exception while checking connection with BankID RP: {}",
                    exc.getMessage());
            return false;
        }
    }

    private HttpClient getClient() {
        return HttpClient
            .newBuilder()
            .connectTimeout(Duration.ofMillis(this.settings.getConnectTimeout()))
            .sslContext(this.sslContext)
            .build();
    }

    private HttpRequest.Builder getRequestBuilder(String path) throws URISyntaxException {
        String fullPath = (this.settings.getUrl() + "/" + path)
            .replace("//", "/")
            .replace(":/", "://");

        return
            HttpRequest
                .newBuilder()
                .timeout(Duration.ofMillis(this.settings.getReadTimeout()))
                .uri(new URI(fullPath))
                .header("Content-Type", "application/json");
    }

    private SSLContext newSslContext() {
        if (this.settings.getTrustStorePath() == null
            || this.settings.getTrustStorePath().isBlank()) {
            throw new RuntimeException("Trust store path is not configured");
        }

        if (this.settings.getClientCertStorePath() == null
            || this.settings.getClientCertStorePath().isBlank()) {
            throw new RuntimeException("Client cert store path is null");
        }

        if (this.settings.getTrustStorePassword() == null
            || this.settings.getTrustStorePassword().isBlank()) {
            throw new NullPointerException("Trust store password is null");
        }

        if (this.settings.getClientCertStorePassword() == null
            || this.settings.getClientCertStorePassword().isBlank()) {
            throw new NullPointerException("Client cert store password is null");
        }

        File clientCertFile = new File(this.settings.getClientCertStorePath());
        File trustStore = new File(this.settings.getTrustStorePath());

        try (InputStream clientCertStream = new FileInputStream(clientCertFile);
             InputStream trustStoreStream = new FileInputStream(trustStore)) {
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(clientCertStream, this.settings.getTrustStorePassword().toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory
                .getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keystore, this.settings.getClientCertStorePassword().toCharArray());

            KeyStore trustKeystore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustKeystore.load(trustStoreStream, this.settings.getTrustStorePassword().toCharArray());

            SSLContext context = SSLContext.getInstance("TLSv1.2");
            TrustManagerFactory factory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
            factory.init(trustKeystore);
            context.init(keyManagerFactory.getKeyManagers(), factory.getTrustManagers(), null);

            return context;
        } catch (java.security.UnrecoverableKeyException
                 | java.security.KeyStoreException
                 | java.security.cert.CertificateException
                 | java.security.NoSuchAlgorithmException
                 | java.security.KeyManagementException
                 | IOException exception) {

            throw new RuntimeException("Failed to open store: " + exception, exception);
        }
    }

    /**
     * Starts an authentication with BankID RP API.
     * @param clientIp the client IP address.
     * @return the information about the created transaction or null if the transaction failed.
     */
    public StartTransactionResponse startAuthentication(String clientIp) {
        StartAuthenticationRequest request = new StartAuthenticationRequest(clientIp);
        request.setRequirement(this.settings.getAuthenticationRequirements());
        return startAuthentication(request);
    }

    /**
     * Starts an authentication with BankID RP API.
     * @param request the authentication request information.
     * @return the information about the created transaction.
     */
    public StartTransactionResponse startAuthentication(StartAuthenticationRequest request) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonRequest = mapper.writeValueAsString(request);

            return this.startTransactionResponse("auth", jsonRequest);
        } catch (Exception e) {
            this.logger.warn("IOException while starting authentication: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Starts a signature transaction with BankID RP API.
     * @param request the signature request information.
     * @return the information about the created transaction.
     */
    public StartTransactionResponse startSignature(StartSignatureRequest request) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonRequest = mapper.writeValueAsString(request);

            return this.startTransactionResponse("sign", jsonRequest);
        } catch (Exception e) {
            this.logger.warn("IOException while starting authentication: {}", e.getMessage());
            return null;
        }
    }

    private StartTransactionResponse startTransactionResponse(String endpoint, String jsonRequest)
        throws JsonProcessingException {

        long startTime = System.currentTimeMillis();
        String result = this.post(endpoint, jsonRequest);
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (result == null) {
            this.logger.warn(
                    "Query to start {} transaction failed after {} ms.",
                    endpoint,
                    elapsedTime);
            return null;
        }

        this.logger.info("Received json string from BankID RP after {} ms.", elapsedTime);
        this.logger.trace("Try to parse json string: {}", Encode.forJava(result));
        ObjectMapper mapper = new ObjectMapper();
        StartTransactionResponse response = mapper.readValue(
                result, StartTransactionResponse.class);
        if (response == null || response.getOrderRef() == null || response.getAutoStartToken() == null) {
            this.logger.warn(
                    "Response to start auth transaction could not be read after {} ms: {}",
                    elapsedTime,
                    Encode.forJava(result));
            return null;
        }

        return response;
    }

    /**
     * Collects the result of a sign or auth order using the orderRef as reference.
     * RP should keep on calling collect every two seconds as long as status indicates pending.
     * RP must abort if status indicates failed. The user identity is returned when complete.
     * @param orderRef The orderRef returned from auth or sign.
     * @return CollectResponse
     */
    public CollectResponse collect(String orderRef) {

        try {
            CollectRequest request = new CollectRequest(orderRef);
            ObjectMapper mapper = JsonMapper.builder()
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .build();
            String jsonRequest = mapper.writeValueAsString(request);

            long startTime = System.currentTimeMillis();
            String result = this.post("collect", jsonRequest);
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (result == null) {
                this.logger.warn(
                        "Query to collect transaction failed after {} ms.",
                        elapsedTime);
                return null;
            }

            this.logger.info("Received json string from BankID RP after {} ms.", elapsedTime);
            this.logger.trace("Try to parse json string: {}", Encode.forJava(result));
            CollectResponse response = mapper.readValue(
                    result, CollectResponse.class);
            if (response == null || response.getOrderRef() == null) {
                this.logger.warn(
                        "Response to collect transaction could not be read after {} ms: {}",
                        elapsedTime,
                        Encode.forJava(result));
                return null;
            }

            return response;
        } catch (Exception e) {
            this.logger.warn("IOException while starting authentication: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Cancels an ongoing sign or auth order.
     * This is typically used if the user cancels the order in your service or app.
     * @param orderRef The orderRef from the response from auth or sign. String.
     * @return True if cancelling succeeded.
     */
    public boolean cancel(String orderRef) {
        try {
            CancelRequest request = new CancelRequest(orderRef);
            ObjectMapper mapper = new ObjectMapper();
            String jsonRequest = mapper.writeValueAsString(request);

            long startTime = System.currentTimeMillis();
            String result = this.post("cancel", jsonRequest);
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (result == null) {
                this.logger.warn("Query to cancel transaction "
                                + "failed after {} ms.",
                        elapsedTime);
                return false;
            }

            this.logger.info("Received json string from BankID RP after {} ms.", elapsedTime);
            return result.trim().equals("{}");
        } catch (Exception e) {
            this.logger.warn("IOException while starting authentication: {}", e.getMessage());
            return false;
        }
    }

    private String post(String path, String content) {

        String verb = "POST";
        try {
            HttpRequest request = getRequestBuilder(path)
                .POST(HttpRequest.BodyPublishers.ofString(content))
                .build();

            this.logger.trace("Starting to send payload to BankID RP.");
            HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            this.logger.trace("Sent request to RP:/{} with content: {}", path, content);

            int expectedResult = HttpStatus.OK.value();
            int responseCode = response.statusCode();
            if (responseCode != expectedResult) {
                this.logger.warn("Failed to {} to BankID RP ({}). Expected {}"
                        + " but got {} with message \"{}\".",
                        verb,
                        path,
                        expectedResult,
                        responseCode,
                    response.body());
                return null;
            }

            this.logger.trace("Starting to read response from BankID RP.");
            return response.body();
        } catch (IOException | InterruptedException | URISyntaxException e) {

            e.printStackTrace();

            this.logger.warn(
                    "Could not {} to RP:/{} ({}): \"{}\"",
                    verb,
                    path,
                    e.getClass().toString(),
                    e.getMessage());
        }

        return null;
    }
}
