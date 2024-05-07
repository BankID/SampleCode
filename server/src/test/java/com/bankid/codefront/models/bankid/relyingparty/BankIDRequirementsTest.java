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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * Test requirements to json.
 */
public class BankIDRequirementsTest {

    /**
     * Certificate policy to json.
     * @throws Exception
     */
    @Test
    public void toJsonRequirementCertificatePolicy() throws Exception {
        BankIDRequirements requirements = new BankIDRequirements();
        requirements.setCertificatePolicies(Collections.singletonList("1.2.752.78.1.5"));

        // Act
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requirements);

        // Verify
        String expectedJson = "{\"certificatePolicies\":[\"1.2.752.78.1.5\"]}";
        Assertions.assertEquals(expectedJson, json);
    }

    /**
     * Certificate policies to json.
     * @throws Exception
     */
    @Test
    public void toJsonRequirementCertificatePolicies() throws Exception {
        BankIDRequirements requirements = new BankIDRequirements();
        requirements.setCertificatePolicies(Arrays.asList("1.2.752.78.1.5", "1.2.752.71.1.3", "1.2.752.78.1.2"));

        // Act
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requirements);

        // Verify
        String expectedJson = "{\"certificatePolicies\":[\"1.2.752.78.1.5\",\"1.2.752.71.1.3\",\"1.2.752.78.1.2\"]}";
        Assertions.assertEquals(expectedJson, json);
    }

    /**
     * Set card reader.
     * @throws Exception
     */
    @Test
    public void toJsonRequirementCardReader() throws Exception {
        BankIDRequirements requirements = new BankIDRequirements();
        requirements.setCardReader("class2");

        // Act
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requirements);

        // Verify
        Assertions.assertEquals("{\"cardReader\":\"class2\"}", json);
    }

    /**
     * Require pinCode.
     * @throws Exception
     */
    @Test
    public void toJsonRequirementPinCode() throws Exception {
        BankIDRequirements requirements = new BankIDRequirements();
        requirements.setPinCode(true);

        // Act
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requirements);

        // Verify
        Assertions.assertEquals("{\"pinCode\":true}", json);
    }

    /**
     * Empty requirement.
     * @throws Exception
     */
    @Test
    public void toJsonRequirementEmpty() throws Exception {
        BankIDRequirements requirements = new BankIDRequirements();

        // Act
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requirements);

        // Verify
        Assertions.assertEquals("{}", json);
    }

    /**
     * Require risk at most level low.
     * @throws Exception
     */
    @Test
    public void toJsonRequirementRisk() throws Exception {
        BankIDRequirements requirements = new BankIDRequirements();
        requirements.setRisk(RiskRequirement.LOW);

        // Act
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requirements);

        // Verify
        Assertions.assertEquals("{\"risk\":\"LOW\"}", json);
    }
}
