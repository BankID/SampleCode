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

import com.bankid.codefront.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test authentication request validation.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class AuthenticationRequestTest {

    /**
     * Should trim user visible data.
     */
    @Test
    public void trimUserVisibleData() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUserVisibleData(" test ");

        // Act
        String result = request.getUserVisibleData();

        // Assert
        assertEquals("test", result);
    }

    /**
     * User visible data can be null.
     */
    @Test
    public void userVisibleDataNull() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();

        // Act
        String result = request.getUserVisibleData();

        // Assert
        assertNull(result);
    }

    /**
     * Should trim user non visible data.
     */
    @Test
    public void trimUserNonVisibleData() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUserNonVisibleData(" test ");

        // Act
        String result = request.getUserNonVisibleData();

        // Assert
        assertEquals("test", result);
    }

    /**
     * User non visible data can be null.
     */
    @Test
    public void userNonVisibleDataNull() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();

        // Act
        String result = request.getUserNonVisibleData();

        // Assert
        assertNull(result);
    }

    /**
     * Empty request is valid.
     */
    @Test
    public void validateEmpty() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();

        // Act
        boolean valid = request.validate();

        // Assert
        assertTrue(valid);
    }

    /**
     * Check maxlength of user visible data.
     */
    @Test
    public void validateInvalidVisibleLength() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUserVisibleData(TestUtils.generateString(1501));

        // Act
        boolean valid = request.validate();

        // Assert
        assertFalse(valid);
    }

    /**
     * Check maxlength of user none visible data.
     */
    @Test
    public void validateInvalidNonVisibleLength() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUserNonVisibleData(TestUtils.generateString(1501));

        // Act
        boolean valid = request.validate();

        // Assert
        assertFalse(valid);
    }

    /**
     * Valid user visible data and user non visible data.
     */
    @Test
    public void validateWithData() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUserVisibleData(TestUtils.generateString(200));
        request.setUserNonVisibleData(TestUtils.generateString(200));

        // Act
        boolean valid = request.validate();

        // Assert
        assertTrue(valid);
    }
}
