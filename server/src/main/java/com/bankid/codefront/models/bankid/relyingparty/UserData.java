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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Information related to the user.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserData implements Serializable {
    private String personalNumber;
    private String name;
    private String givenName;
    private String surname;

    /**
     * Returns the personal number.
     * @return the personal number.
     */
    public String getPersonalNumber() {
        return this.personalNumber;
    }

    /**
     * Sets the personal number.
     * @param personalNumber the personal number.
     */
    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    /**
     * Returns the given name and surname of the user.
     * @return the given name and surname of the user.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the given name and surname of the user.
     * @param name the given name and surname of the user.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the given name of the user.
     * @return the given name of the user.
     */
    public String getGivenName() {
        return this.givenName;
    }

    /**
     * Sets the given name of the user.
     * @param givenName the given name of the user.
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    /**
     * Returns the surname of the user.
     * @return the surname of the user.
     */
    public String getSurname() {
        return this.surname;
    }

    /**
     * Sets the surname of the user.
     * @param surname the surname of the user.
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }
}
