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
 * Test deserialize completion data.
 */
public class CompletionDataTest {

    /**
     * Deserialize completed data.
     */
    @Test
    public void deserializeCompleted() throws JsonProcessingException {
        String json =
                "{\n"
                    + "  \"user\":{\n"
                    + "    \"personalNumber\":\"190000000000\",\n"
                    + "    \"name\":\"Karl Karlsson\",\n"
                    + "    \"givenName\":\"Karl\",\n"
                    + "    \"surname\":\"Karlsson\"\n"
                    + "  },\n"
                    + "  \"device\":{\n"
                    + "    \"ipAddress\":\"192.168.0.1\",\n"
                    + "    \"uhi\":\"abc123\"\n"
                    + "  },\n"
                    + "  \"bankIdIssueDate\":\"2020-02-01\",\n"
                    + "  \"stepUp\":{\n"
                    + "    \"mrtd\":true\n"
                    + "  },\n"
                    + "  \"signature\":\"base64 xml-dig-sig\", \n"
                    + "  \"ocspResponse\":\"base64 ocsp response\"\n"
                    + "}";

        ObjectMapper mapper = new ObjectMapper();
        CompletionData response = mapper.readValue(json, CompletionData.class);

        Assertions.assertEquals("base64 xml-dig-sig", response.getSignature());
        Assertions.assertEquals("base64 ocsp response", response.getOcspResponse());

        UserData user = response.getUser();
        Assertions.assertNotNull(user);
        Assertions.assertEquals("190000000000", user.getPersonalNumber());
        Assertions.assertEquals("Karl Karlsson", user.getName());
        Assertions.assertEquals("Karl", user.getGivenName());
        Assertions.assertEquals("Karlsson", user.getSurname());

        DeviceData device = response.getDevice();
        Assertions.assertNotNull(device);
        Assertions.assertEquals("192.168.0.1", device.getIpAddress());
        Assertions.assertEquals("abc123", device.getUhi());

        StepUpData stepUpData = response.getStepUp();
        Assertions.assertEquals(Boolean.TRUE, stepUpData.getMrtd());

        String bankIdIssueDate = response.getBankIdIssueDate();
        Assertions.assertEquals("2020-02-01", bankIdIssueDate);
    }

    /**
     * Test to verify that if BankID adds another property (which isn't a breaking change),
     * we must be able to handle that.
     */
    @Test
    public void ignoreUnknownProperties() throws JsonProcessingException {
        String json =
                "{\n"
                    + "  \"user\":{\n"
                    + "    \"personalNumber\":\"190000000000\",\n"
                    + "    \"name\":\"Karl Karlsson\",\n"
                    + "    \"givenName\":\"Karl\",\n"
                    + "    \"surname\":\"Karlsson\",\n"
                    + "    \"unknownProperty\":\"propertyValue\"\n"
                    + "  },\n"
                    + "  \"device\":{\n"
                    + "    \"ipAddress\":\"192.168.0.1\",\n"
                    + "    \"uhi\":\"abc123\",\n"
                    + "    \"unknownProperty\":\"propertyValue\"\n"
                    + "  },\n"
                    + "  \"bankIdIssueDate\":\"2020-02-01\",\n"
                    + "  \"stepUp\":{\n"
                    + "    \"unknownProperty\":\"propertyValue\"\n"
                    + "  },\n"
                    + "  \"signature\":\"base64 xml-dig-sig\", \n"
                    + "  \"ocspResponse\":\"base64 ocsp response\",\n"
                    + "  \"unknownProperty\":\"propertyValue\"\n"
                    + "}";

        ObjectMapper mapper = new ObjectMapper();
        CompletionData response = mapper.readValue(json, CompletionData.class);

        Assertions.assertEquals("base64 xml-dig-sig", response.getSignature());
        Assertions.assertEquals("base64 ocsp response", response.getOcspResponse());

        UserData user = response.getUser();
        Assertions.assertNotNull(user);
        Assertions.assertEquals("190000000000", user.getPersonalNumber());
        Assertions.assertEquals("Karl Karlsson", user.getName());
        Assertions.assertEquals("Karl", user.getGivenName());
        Assertions.assertEquals("Karlsson", user.getSurname());

        DeviceData device = response.getDevice();
        Assertions.assertNotNull(device);
        Assertions.assertEquals("192.168.0.1", device.getIpAddress());
        Assertions.assertEquals("abc123", device.getUhi());

        StepUpData stepUpData = response.getStepUp();
        Assertions.assertNull(stepUpData.getMrtd());
    }
}
