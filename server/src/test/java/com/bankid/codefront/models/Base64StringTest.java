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

package com.bankid.codefront.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test Base64 model.
 */
public class Base64StringTest {
    /**
     * Test input validation of base64 encoded string with null input.
     */
    @Test
    public void inputSignatureValidationNull() {
        try {
            new Base64String(null);

            Assertions.fail("Should have thrown an exception");
        } catch (NullPointerException exc) {
            Assertions.assertEquals(
                    "Supplied Base64String is null",
                    exc.getMessage());
        }
    }

    /**
     * Test input validation of base64 encoded string with empty input.
     */
    @Test
    public void inputSignatureValidationEmpty() {
        try {
            new Base64String("");

            Assertions.fail("Should have thrown an exception");
        } catch (IllegalArgumentException exc) {
            Assertions.assertEquals(
                    "Supplied Base64String is too short: ''.",
                    exc.getMessage());
        }
    }

    /**
     * Test input validation of base64 encoded string with invalid characters.
     */
    @Test
    public void inputSignatureValidationNotBase64() {
        try {
            new Base64String("!#%&/");

            Assertions.fail("Should have thrown an exception");
        } catch (IllegalArgumentException exc) {
            Assertions.assertEquals(
                    "Supplied Base64String is invalid: '!#%&/'.",
                    exc.getMessage());
        }
    }

    /**
     * Test input validation of base64 encoded string with invalid number of characters (missing two '=').
     */
    @Test
    public void inputSignatureValidationInvalidLength2() {
        try {
            new Base64String("YWFhCg");

            Assertions.fail("Should have thrown an exception");
        } catch (IllegalArgumentException exc) {
            Assertions.assertEquals(
                    "Supplied Base64String is invalid: 'YWFhCg'.",
                    exc.getMessage());
        }
    }

    /**
     * Test input validation of base64 encoded string with invalid number of characters (missing two '=').
     */
    @Test
    public void inputSignatureValidationInvalidLength1() {
        try {
            new Base64String("YWFhCg=");

            Assertions.fail("Should have thrown an exception");
        } catch (IllegalArgumentException exc) {
            Assertions.assertEquals(
                    "Supplied Base64String is invalid: 'YWFhCg='.",
                    exc.getMessage());
        }
    }

    /**
     * Test input validation of base64 encoded string with valid input.
     */
    @Test
    public void success() {
        String input = "YWFhCg==";
        Base64String str = new Base64String(input);

        Assertions.assertEquals(input, str.getValue());
        Assertions.assertEquals("Base64String", str.getNameOfValue());
    }
}
