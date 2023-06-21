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

import org.owasp.encoder.Encode;

import java.io.Serializable;

/**
 * A number of helper methods for values that must be validated.
 */
public abstract class ValidatedValue implements Serializable {
    private String value;

    @Override
    public String toString() {
        return this.getValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!this.getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }

        final ValidatedValue other = (ValidatedValue) obj;

        if (other.value == null && this.value == null) {
            return true;
        }

        if (this.value == null) {
            return false;
        }

        return (this.value.equals(other.value));
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    /**
     * Returns the value.
     * @return the value.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Sets the value.
     * @param value the value.
     */
    protected void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns the name of the value for exception text purposes.
     * @return the name of the value for exception text purposes.
     */
    protected abstract String getNameOfValue();

    /**
     * Check the value for null and throw NullPointerException if it is null.
     * @param value the value to check for null.
     */
    protected void throwForNull(Object value) {
        this.throwForNull(value, this.getNameOfValue());
    }

    /**
     * Check the value for null and throw NullPointerException if it is null.
     * @param value     the value to check for null.
     * @param valueName the name of the value to check.
     */
    protected void throwForNull(Object value, String valueName) {
        if (value == null) {
            throw new NullPointerException("Supplied " + valueName + " is null");
        }
    }

    /**
     * Check that the value matches a regex pattern.
     * @param value        the value to check for pattern.
     * @param patternValue the pattern to use.
     */
    void throwForIllegalPattern(String value, String patternValue) {

        throwForNull(patternValue, this.getNameOfValue() + " validation pattern");

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patternValue);
        if (!pattern.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Supplied "
                            + this.getNameOfValue()
                            + " is invalid: '"
                            + Encode.forJava(value)
                            + "'.");
        }
    }
}
