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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test deserialize Collect response.
 */
public class CollectResponseTest {

    /**
     * In progress response.
     */
    @Test
    public void deserializeInProgress() throws JsonProcessingException {
        String json = "{\n"
            + "  \"orderRef\":\"131daac9-16c6-4618-beb0-365768f37288\", \n"
            + "  \"status\":\"pending\",\n"
            + "  \"hintCode\":\"userSign\"\n"
            + "}";

        ObjectMapper mapper = new ObjectMapper();
        CollectResponse response = mapper.readValue(json, CollectResponse.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("131daac9-16c6-4618-beb0-365768f37288", response.getOrderRef());
        Assertions.assertEquals("pending", response.getStatus());
        Assertions.assertEquals("userSign", response.getHintCode());

        Assertions.assertNull(response.getCompletionData());
    }

    /**
     * Cancelled response.
     */
    @Test
    public void deserializeCancelled() throws JsonProcessingException {
        String json = "{\n"
            + "  \"orderRef\":\"131daac9-16c6-4618-beb0-365768f37288\", \n"
            + "  \"status\":\"failed\",\n"
            + "  \"hintCode\":\"userCancel\"\n"
            + "}";

        ObjectMapper mapper = new ObjectMapper();
        CollectResponse response = mapper.readValue(json, CollectResponse.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("131daac9-16c6-4618-beb0-365768f37288", response.getOrderRef());
        Assertions.assertEquals("failed", response.getStatus());
        Assertions.assertEquals("userCancel", response.getHintCode());

        Assertions.assertNull(response.getCompletionData());
    }

    /**
     * Completed response.
     */
    @Test
    public void deserializeCompleted() throws JsonProcessingException {
        String json = "{\n"
            + "  \"orderRef\":\"131daac9-16c6-4618-beb0-365768f37288\", \n"
            + "  \"status\":\"complete\",\n"
            + "  \"completionData\":{\n"
            + "    \"user\":{\n"
            + "      \"personalNumber\":\"190000000000\",\n"
            + "      \"name\":\"Karl Karlsson\",\n"
            + "      \"givenName\":\"Karl\",\n"
            + "      \"surname\":\"Karlsson\"\n"
            + "    },\n"
            + "    \"device\":{\n"
            + "      \"ipAddress\":\"192.168.0.1\"\n"
            + "    },\n"
            + "    \"cert\":{\n"
            + "      \"notBefore\":\"1502983274000\",\n"
            + "      \"notAfter\":\"1563549674000\"\n"
            + "    },\n"
            + "    \"signature\":\"base64 xml-dig-sig\", \n"
            + "    \"ocspResponse\":\"base64 ocsp response\"\n"
            + "  } \n"
            + "}";

        ObjectMapper mapper = new ObjectMapper();
        CollectResponse response = mapper.readValue(json, CollectResponse.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("131daac9-16c6-4618-beb0-365768f37288", response.getOrderRef());
        Assertions.assertEquals("complete", response.getStatus());
        Assertions.assertNull(response.getHintCode());

        Assertions.assertNotNull(response.getCompletionData());
        Assertions.assertEquals("base64 xml-dig-sig", response.getCompletionData().getSignature());
        Assertions.assertEquals("base64 ocsp response", response.getCompletionData().getOcspResponse());
    }
}
