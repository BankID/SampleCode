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

import com.bankid.codefront.models.service.CompletionResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test transformation of completion result to completion response.
 */
public class CompletionResponseTest {

    /**
     * Should handle empty personal number.
     */
    @Test
    public void emptyPersonalNumber() {
        String name = "Test User";
        String personalNumber = "";
        String signedText = "123";
        CompletionResult completionResult = new CompletionResult(name, personalNumber, signedText);

        CompletionResponse completionResponse = new CompletionResponse(completionResult);

        assertEquals(name, completionResponse.getName());
        assertEquals(signedText, completionResponse.getSignedText());
        assertEquals(personalNumber, completionResponse.getPersonalNumber());
    }

    /**
     * Should handle personal number is null.
     */
    @Test
    public void nullPersonalNumber() {
        String name = "Test User";
        String personalNumber = null;
        String signedText = "123";
        CompletionResult completionResult = new CompletionResult(name, personalNumber, signedText);

        CompletionResponse completionResponse = new CompletionResponse(completionResult);

        assertEquals(name, completionResponse.getName());
        assertEquals(signedText, completionResponse.getSignedText());
        assertNull(completionResponse.getPersonalNumber());
    }

    /**
     * Should format and mask last 4 digits.
     */
    @Test
    public void maskDigitsPersonalNumber() {
        String name = "Test User";
        String personalNumber = "123456789012";
        String signedText = "123";
        CompletionResult completionResult = new CompletionResult(name, personalNumber, signedText);

        CompletionResponse completionResponse = new CompletionResponse(completionResult);

        assertEquals(name, completionResponse.getName());
        assertEquals(signedText, completionResponse.getSignedText());
        assertEquals("12345678-XXXX", completionResponse.getPersonalNumber());
    }
}
