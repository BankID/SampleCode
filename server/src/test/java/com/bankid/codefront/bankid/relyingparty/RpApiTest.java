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
import com.bankid.codefront.models.Base64String;
import com.bankid.codefront.models.bankid.relyingparty.CollectResponse;
import com.bankid.codefront.models.bankid.relyingparty.StartSignatureRequest;
import com.bankid.codefront.models.bankid.relyingparty.StartTransactionResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

/**
 * Test RP api.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RpApiTest {

    private static final String CLIENT_IP = "127.0.0.1";
    private final String visibleData = Base64.getEncoder().encodeToString("visible".getBytes(StandardCharsets.UTF_8));

    @Mock
    private BankIDRelyingPartyConfig settings;

    @Mock
    private RpApiMetrics metrics;

    private RpApi rpApi;

    /**
     * Common set-up code.
     */
    @BeforeAll
    public void setup() {
        MockitoAnnotations.initMocks(this);

        Assertions.assertNotNull(this.settings);
        when(this.settings.getUrl()).thenReturn("https://appapi2.test.bankid.com/rp/v5.1");

                // Set trust store
        when(this.settings.getTrustStorePath()).thenReturn(
                "certificates/truststore.jks");
        when(this.settings.getTrustStorePassword()).thenReturn("qwerty123");
        when(this.settings.getTrustStoreType()).thenReturn("JKS");

        // Set client certificates
        when(this.settings.getClientCertStorePath()).thenReturn(
                "certificates/FPTestcert4_20220818.p12");
        when(this.settings.getClientCertStorePassword()).thenReturn("qwerty123");
        when(this.settings.getClientCertStoreType()).thenReturn("PKCS12");

        this.rpApi = new RpApi(
                this.settings,
                this.metrics);
    }

    /**
     * Tests if certificates and URL are valid.
     */
    //@Test
    public void checkConnectionWorks() {
        this.rpApi.checkConnectionWorks();

        // Metrics
        Mockito.verify(this.metrics).successConnectionCheck(Mockito.anyLong());
        Mockito.verify(this.metrics, never()).failedConnectionCheck(Mockito.anyLong());
    }

    /**
     * Test a full cycle of communication with BankID RP API.
     * This test is not normally run.
     */
    //@Test
    public void makeOneAuthCycle() {
        StartTransactionResponse response = this.rpApi.startAuthentication(CLIENT_IP);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getOrderRef());
        Assertions.assertNotNull(response.getAutoStartToken());
        Assertions.assertNotNull(response.getQrStartToken());
        Assertions.assertNotNull(response.getQrStartSecret());

        // Collect
        CollectResponse collectResponse = this.rpApi.collect(response.getOrderRef());
        Assertions.assertNotNull(collectResponse);

        Assertions.assertNotNull(collectResponse.getOrderRef());
        Assertions.assertEquals(response.getOrderRef(), collectResponse.getOrderRef());

        Assertions.assertNotNull(collectResponse.getStatus());
        Assertions.assertEquals("pending", collectResponse.getStatus());
        Assertions.assertNotNull(collectResponse.getHintCode());
        Assertions.assertEquals("outstandingTransaction", collectResponse.getHintCode());

        // Cancel
        boolean cancelResponse = this.rpApi.cancel(response.getOrderRef());
        Assertions.assertTrue(cancelResponse);
    }

    /**
     * Test a full cycle of communication with BankID RP API.
     * This test is not normally run.
     */
    //@Test
    public void makeOneSignCycle() {
        StartSignatureRequest request = new StartSignatureRequest(CLIENT_IP, new Base64String(this.visibleData));
        StartTransactionResponse response = this.rpApi.startSignature(request);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getOrderRef());
        Assertions.assertNotNull(response.getAutoStartToken());
        Assertions.assertNotNull(response.getQrStartToken());
        Assertions.assertNotNull(response.getQrStartSecret());

        // Collect
        CollectResponse collectResponse = this.rpApi.collect(response.getOrderRef());
        Assertions.assertNotNull(collectResponse);

        Assertions.assertNotNull(collectResponse.getOrderRef());
        Assertions.assertEquals(response.getOrderRef(), collectResponse.getOrderRef());

        Assertions.assertNotNull(collectResponse.getStatus());
        Assertions.assertEquals("pending", collectResponse.getStatus());
        Assertions.assertNotNull(collectResponse.getHintCode());
        Assertions.assertEquals("outstandingTransaction", collectResponse.getHintCode());

        // Cancel
        boolean cancelResponse = this.rpApi.cancel(response.getOrderRef());
        Assertions.assertTrue(cancelResponse);
    }
}
