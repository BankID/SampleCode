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

package com.bankid.codefront.rest.contoller;

import com.bankid.codefront.models.service.Status;

/**
 * Transaction status.
 */
public class CheckResponse {
    private String hintCode;
    private Status status;
    private String qrCode;

    /**
     * Returns the hint code.
     * @return the hint code.
     */
    public String getHintCode() {
        return this.hintCode;
    }

    /**
     * Sets the hint code.
     * @param hintCode the hint code.
     */
    public void setHintCode(String hintCode) {
        this.hintCode = hintCode;
    }

    /**
     * Returns the status.
     * @return the status.
     */
    public Status getStatus() {
        return this.status;
    }

    /**
     * Sets the status.
     * @param status the status.
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Returns the qr code.
     * @return the qr code.
     */
    public String getQrCode() {
        return this.qrCode;
    }

    /**
     * Sets qr code.
     * @param qrCode the qr code.
     */
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
