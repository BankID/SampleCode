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
 * Test start transaction.
 */
public class StartTransactionResponseTest {
    /**
     * Test to verify that deserialization works nicely. The json data is taken from
     * http://www.bankid.com/rp/info
     */
    @Test
    public void fromRpGuidelines() throws JsonProcessingException {
        String json = "{\n"
            + "  \"orderRef\":\"131daac9-16c6-4618-beb0-365768f37288\", \n"
            + "  \"autoStartToken\":\"7c40b5c9-fa74-49cf-b98c-bfe651f9a7c6\", \n"
            + "  \"qrStartToken\":\"67df3917-fa0d-44e5-b327-edcc928297f8\", \n"
            + "  \"qrStartSecret\":\"d28db9a7-4cde-429e-a983-359be676944c\"\n"
            + "}";

        ObjectMapper mapper = new ObjectMapper();
        StartTransactionResponse response = mapper.readValue(json, StartTransactionResponse.class);

        Assertions.assertEquals("131daac9-16c6-4618-beb0-365768f37288", response.getOrderRef());
        Assertions.assertEquals("7c40b5c9-fa74-49cf-b98c-bfe651f9a7c6", response.getAutoStartToken());
        Assertions.assertEquals("67df3917-fa0d-44e5-b327-edcc928297f8", response.getQrStartToken());
        Assertions.assertEquals("d28db9a7-4cde-429e-a983-359be676944c", response.getQrStartSecret());
    }

    /**
     * Test to verify that if BankID adds another property (which isn't a breaking change),
     * we must be able to handle that.
     */
    @Test
    public void ignoreUnknownProperties() throws JsonProcessingException {
        String json = "{\n"
            + "  \"orderRef\":\"131daac9-16c6-4618-beb0-365768f37288\", \n"
            + "  \"autoStartToken\":\"7c40b5c9-fa74-49cf-b98c-bfe651f9a7c6\", \n"
            + "  \"qrStartToken\":\"67df3917-fa0d-44e5-b327-edcc928297f8\", \n"
            + "  \"qrStartSecret\":\"d28db9a7-4cde-429e-a983-359be676944c\", \n"
            + "  \"unknownProperty\":\"propertyValue\"\n"
            + "}";

        ObjectMapper mapper = new ObjectMapper();
        StartTransactionResponse response = mapper.readValue(json, StartTransactionResponse.class);

        Assertions.assertEquals("131daac9-16c6-4618-beb0-365768f37288", response.getOrderRef());
        Assertions.assertEquals("7c40b5c9-fa74-49cf-b98c-bfe651f9a7c6", response.getAutoStartToken());
        Assertions.assertEquals("67df3917-fa0d-44e5-b327-edcc928297f8", response.getQrStartToken());
        Assertions.assertEquals("d28db9a7-4cde-429e-a983-359be676944c", response.getQrStartSecret());
    }
}
