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

import com.bankid.codefront.models.service.BankIDTransaction;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;

/**
 * Value stored in session about the transaction.
 */
@Component
@SessionScope
@JsonSerialize
public class SessionValue implements Serializable {
    private static final long serialVersionUID = 1L;

    private BankIDTransaction bankIDTransaction;
    private boolean authTransaction;

    /**
     * Returns the BankID transaction.
     * @return the BankID transaction.
     */
    public BankIDTransaction getBankIDTransaction() {
        return this.bankIDTransaction;
    }

    /**
     * Sets the BankID transaction.
     * @param bankIDTransaction the BankID transaction.
     */
    public void setBankIDTransaction(BankIDTransaction bankIDTransaction) {
        this.bankIDTransaction = bankIDTransaction;
    }

    /**
     * Return true if is an auth transaction.
     * @return true if is an auth transaction.
     */
    public boolean isAuthTransaction() {
        return this.authTransaction;
    }

    /**
     * Set true is auth transaction.
     * @param authTransaction true if authentication.
     */
    public void setAuthTransaction(boolean authTransaction) {
        this.authTransaction = authTransaction;
    }

}
