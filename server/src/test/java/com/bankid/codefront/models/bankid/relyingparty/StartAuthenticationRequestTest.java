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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

/**
 * Test start authentication request to json.
 */
public class StartAuthenticationRequestTest {

    private final String visibleData = Base64.getEncoder().encodeToString("visible".getBytes(StandardCharsets.UTF_8));

    private final String nonVisibleData = Base64.getEncoder().encodeToString(
        "invisible".getBytes(StandardCharsets.UTF_8)
    );

    /**
     * Serialize as simple as it gets.
     */
    @Test
    public void simpleSerialize() throws JsonProcessingException, JSONException {
        StartAuthenticationRequest request = new StartAuthenticationRequest("1.2.3.4");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        String expectedJson = """
            {
                "endUserIp": "1.2.3.4",
                "returnRisk": true
            }
        """;
        JSONAssert.assertEquals(expectedJson, json, JSONCompareMode.STRICT);
    }

    /**
     * Serialize with requirements.
     */
    @Test
    public void serializeRequirements() throws JsonProcessingException, JSONException {
        BankIDRequirements requirements = new BankIDRequirements();
        requirements.setCardReader("class1");
        requirements.setCertificatePolicies(Collections.singletonList("1.2.3.4.5"));

        StartAuthenticationRequest request = new StartAuthenticationRequest("1.2.3.4");
        request.setRequirement(requirements);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        String expectedJson = """
            {
                "endUserIp": "1.2.3.4",
                "returnRisk": true,
                "requirement": {
                  "cardReader": "class1",
                  "certificatePolicies": ["1.2.3.4.5"]
                }
            }
        """;
        JSONAssert.assertEquals(expectedJson, json, JSONCompareMode.STRICT);
    }

    /**
     * Serialize with visible data.
     */
    @Test
    public void serializeUserVisibleData() throws JsonProcessingException, JSONException {
        StartAuthenticationRequest request = new StartAuthenticationRequest("1.2.3.4");
        request.setUserVisibleData(new Base64String(this.visibleData));
        request.setUserVisibleDataFormat("simpleMarkdownV1");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        String expectedJson = """
            {
                "endUserIp": "1.2.3.4",
                "userVisibleData": "dmlzaWJsZQ==",
                "userVisibleDataFormat": "simpleMarkdownV1",
                "returnRisk": true
            }
        """;
        JSONAssert.assertEquals(expectedJson, json, JSONCompareMode.STRICT);
    }

    /**
     * Serialize with requirements.
     */
    @Test
    public void inputValidationOfFormat() {

        StartAuthenticationRequest request = new StartAuthenticationRequest("1.2.3.4");
        Assertions.assertThrowsExactly(
                IllegalArgumentException.class,
                () -> request.setUserVisibleDataFormat("ops"),
                "TBD");
    }

    /**
     * Serialize with non-visible data.
     */
    @Test
    public void serializeUserNonVisibleData() throws JsonProcessingException, JSONException {
        StartAuthenticationRequest request = new StartAuthenticationRequest("1.2.3.4");
        request.setUserNonVisibleData(new Base64String(this.nonVisibleData));

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        String expectedJson = """
            {
                "endUserIp": "1.2.3.4",
                "userNonVisibleData": "aW52aXNpYmxl",
                "returnRisk": true
            }
        """;
        JSONAssert.assertEquals(expectedJson, json, JSONCompareMode.STRICT);
    }

    /**
     * Serialize without returnRisk.
     */
    @Test
    public void serializeNoRiskInformation() throws JsonProcessingException, JSONException {
        StartAuthenticationRequest request = new StartAuthenticationRequest("1.2.3.4");
        request.setReturnRisk(null);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        String expectedJson = """
            {
                "endUserIp": "1.2.3.4"
            }
        """;
        JSONAssert.assertEquals(expectedJson, json, JSONCompareMode.STRICT);
    }

    /**
     * Serialize with web.
     */
    @Test
    public void serializeWeb() throws JsonProcessingException, JSONException {
        StartAuthenticationRequest request = new StartAuthenticationRequest("1.2.3.4");
        AdditionalWebData webData = new AdditionalWebData("localhost", "agent", "did");
        request.setWeb(webData);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        String expectedJson = """
            {
                "endUserIp": "1.2.3.4",
                "returnRisk": true,
                "web": {
                  "referringDomain": "localhost",
                  "userAgent": "agent",
                  "deviceIdentifier": "did"
                }
            }
        """;
        JSONAssert.assertEquals(expectedJson, json, JSONCompareMode.STRICT);
    }

    /**
     * Serialize with returnUrl.
     */
    @Test
    public void serializeReturnUrl() throws JsonProcessingException, JSONException {
        StartAuthenticationRequest request = new StartAuthenticationRequest("1.2.3.4");
        request.setReturnUrl("https://localhost/loggedIn");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        String expectedJson = """
            {
                "endUserIp": "1.2.3.4",
                "returnRisk": true,
                "returnUrl": "https://localhost/loggedIn"
            }
        """;
        JSONAssert.assertEquals(expectedJson, json, JSONCompareMode.STRICT);
    }
}
