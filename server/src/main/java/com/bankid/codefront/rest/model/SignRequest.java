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

/**
 * Information to start a sign transaction.
 *
 * <p>
 * OBSERVE: Sent from client only for demonstration purposes,
 * the values should be set by the backend in production code.
 * </p>
 */
public class SignRequest extends AuthenticationRequest {

    private static final int SIGN_VISIBLE_MAX_LENGTH = 40000;
    private static final int SIGN_NON_VISIBLE_MAX_LENGTH = 200000;

    /**
     * Validate sign request.
     *
     * @return          true if valid.
     */
    @Override
    public boolean validate() {
        String userVisibleData = this.getUserVisibleData();
        if (userVisibleData == null || userVisibleData.isEmpty()) {
            return false;
        }

        // Max length visible data
        if (base64Encode(userVisibleData).length() > SIGN_VISIBLE_MAX_LENGTH) {
            return false;
        }

        // Check user non visible data
        String userNonVisibleData = this.getUserNonVisibleData();
        if (userNonVisibleData != null && !userNonVisibleData.isEmpty()) {
            // Max length
            if (base64Encode(userNonVisibleData).length() > SIGN_NON_VISIBLE_MAX_LENGTH) {
                return false;
            }
        }

        return true;
    }

}
