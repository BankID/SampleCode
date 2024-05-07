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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

/**
 * Test start signature.
 */
public class StartSignatureRequestTest {

    private final String visibleData = Base64.getEncoder().encodeToString(
        "visible".getBytes(StandardCharsets.UTF_8)
    );
    private final String nonVisibleData = Base64.getEncoder().encodeToString(
        "invisible".getBytes(StandardCharsets.UTF_8)
    );

    /**
     * Serialize as simple as it gets.
     */
    @Test
    public void simpleSerialize() throws JsonProcessingException {
        String expected = "{\"endUserIp\":\"1.2.3.4\",\"userVisibleData\":\"dmlzaWJsZQ==\",\"returnRisk\":true}";

        StartSignatureRequest request = new StartSignatureRequest("1.2.3.4", new Base64String(this.visibleData));

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        Assertions.assertEquals(expected, json);
    }

    /**
     * Serialize with requirements.
     */
    @Test
    public void serializeRequirements() throws JsonProcessingException {
        String expected = "{\"endUserIp\":\"1.2.3.4\",\"userVisibleData\":\"dmlzaWJsZQ==\",\"returnRisk\":true,"
            + "\"requirement\":{"
                + "\"cardReader\":\"class1\","
                + "\"certificatePolicies\":[\"1.2.3.4.5\"]"
            + "}"
            + "}";

        BankIDRequirements requirements = new BankIDRequirements();
        requirements.setCardReader("class1");
        requirements.setCertificatePolicies(Collections.singletonList("1.2.3.4.5"));

        StartSignatureRequest request = new StartSignatureRequest("1.2.3.4", new Base64String(this.visibleData));
        request.setRequirement(requirements);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        Assertions.assertEquals(expected, json);
    }

    /**
     * Serialize with user visible data.
     */
    @Test
    public void serializeUserVisibleData() throws JsonProcessingException {
        String expected = "{\"endUserIp\":\"1.2.3.4\",\"userVisibleData\":\"dmlzaWJsZQ==\",\"returnRisk\":true}";

        StartSignatureRequest request = new StartSignatureRequest(
                "1.2.3.4",
                new Base64String(this.visibleData));
        request.setUserVisibleData(new Base64String(this.visibleData));

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        Assertions.assertEquals(expected, json);
    }

    /**
     * Serialize with user visible data with format.
     */
    @Test
    public void serializeUserVisibleDataWithFormat() throws JsonProcessingException {
        String expected = "{\"endUserIp\":\"1.2.3.4\","
            + "\"userVisibleData\":\"dmlzaWJsZQ==\","
            + "\"userVisibleDataFormat\":\"simpleMarkdownV1\","
            + "\"returnRisk\":true}";

        StartSignatureRequest request = new StartSignatureRequest("1.2.3.4", new Base64String(this.visibleData));
        request.setUserVisibleDataFormat("simpleMarkdownV1");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        Assertions.assertEquals(expected, json);
    }

    /**
     * Serialize with user non visible data.
     */
    @Test
    public void serializeUserNonVisibleData() throws JsonProcessingException {
        String expected = "{\"endUserIp\":\"1.2.3.4\","
            + "\"userVisibleData\":\"dmlzaWJsZQ==\","
            + "\"userNonVisibleData\":\"aW52aXNpYmxl\","
            + "\"returnRisk\":true}";

        StartSignatureRequest request = new StartSignatureRequest("1.2.3.4", new Base64String(this.visibleData));
        request.setUserNonVisibleData(new Base64String(this.nonVisibleData));

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        Assertions.assertEquals(expected, json);
    }

    /**
     * Serialize with web.
     */
    @Test
    public void serializeWeb() throws JsonProcessingException {
        String expected = "{\"endUserIp\":\"1.2.3.4\","
            + "\"userVisibleData\":\"dmlzaWJsZQ==\","
            + "\"returnRisk\":true,"
            + "\"web\":{"
            + "\"referringDomain\":\"localhost\","
            + "\"userAgent\":\"agent\","
            + "\"deviceIdentifier\":\"did\""
            + "}}";

        StartSignatureRequest request = new StartSignatureRequest("1.2.3.4", new Base64String(this.visibleData));
        AdditionalWebData webData = new AdditionalWebData("localhost", "agent", "did");
        request.setWeb(webData);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        Assertions.assertEquals(expected, json);
    }
}
