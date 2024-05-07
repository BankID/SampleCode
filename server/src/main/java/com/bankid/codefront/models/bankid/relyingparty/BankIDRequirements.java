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

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Represents the requirements for authentication or signature.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankIDRequirements {

    /**
     * The type of card reader required for transaction.
     *
     * <p>"class1" - (default).
     * The transaction must be performed using a card reader where the PIN code
     * is entered on the computers keyboard, or a card reader of higher class.
     *
     * <p>"class2" -
     * The transaction must be performed using a card reader where the PIN code
     * is entered on the reader, or a reader of higher class.
     *
     * <p>- defaults to "class1".
     * This condition should be combined with a certificatePolicies for a smart card to avoid undefined behavior.
     */
    private String cardReader;

    /**
     * The oid in certificate policies in the user certificate. List of String.
     * One wildcard is allowed from position 5 and forward.
     * The wildcard is: *
     * Example: 1.2.752.78.*
     *
     * <p>The values for production BankIDs are:
     * - "1.2.752.78.1.1" - BankID on file
     * - "1.2.752.78.1.2" - BankID on smart card
     * - "1.2.752.78.1.5" - Mobile BankID
     * - "1.2.752.71.1.3" - Nordea e-id on file and on smart card.
     *
     * <p>The values for test BankIDs are:
     * - "1.2.3.4.5" - BankID on file
     * - "1.2.3.4.10" - BankID on smart card
     * - "1.2.3.4.25" - Mobile BankID
     * - "1.2.752.71.1.3" - Nordea e-id on file and on smart card
     * - "1.2.752.60.1.6" - Test BankID for some BankID Banks
     *
     * <p>Default
     *
     * <p>If no certificate policies are set, the following are default in the production system:
     * - 1.2.752.78.1.1
     * - 1.2.752.78.1.2
     * - 1.2.752.78.1.5
     * - 1.2.752.71.1.3
     *
     * <p>The following are default in the test system:
     * - 1.2.3.4.5
     * - 1.2.3.4.10
     * - 1.2.3.4.25
     * - 1.2.752.60.1.6
     * - 1.2.752.71.1.3
     *
     * <p>If one certificate policy is set, all the default policies are dismissed.
     */
    private List<String> certificatePolicies;

    /**
     * User required to sign the transaction with their PIN code, even if they have biometrics activated.
     *
     * <p>Default
     *
     * <p>False, the user is not required to use pin code.
     */
    private Boolean pinCode;

    /**
     * If present, and set to "true", the client need to provide MRTD (Machine readable travel document)
     * information to complete the order.
     * Only Swedish passports and national ID cards are supported.
     *
     * <p>Default
     *
     * <p>The client does not need to provide MRTD information to complete the order.
     */
    private Boolean mrtd;

    /**
     * A personal number to be used to complete the transaction.
     * If a BankID with another personal number attempts to sign the transaction, it fails.
     */
    private String personalNumber;

    /**
     * Set the acceptable risk level for the transaction.
     * If the risk of the transaction is higher than the required limit the transaction will be blocked.
     * String:
     * · "LOW" – only accept low risk transactions
     * · "MODERATE" – accept low and moderate risk transactions.
     *
     * <p>Risk indicator will be more effective if endUserIp and additional data is sent correctly
     */
    private RiskRequirement risk;

    /**
     * Returns the card reader requirement.
     * @return the card reader requirement.
     */
    public String getCardReader() {
        return this.cardReader;
    }

    /**
     * Sets the card reader requirement.
     * @param cardReader the card reader requirement.
     */
    public void setCardReader(String cardReader) {
        this.cardReader = cardReader;
    }

    /**
     * Returns the certificate policies requirement.
     * @return the certificate policies requirement.
     */
    public List<String> getCertificatePolicies() {
        return this.certificatePolicies;
    }

    /**
     * Sets the certificate policies requirement.
     * @param certificatePolicies the certificate policies requirement.
     */
    public void setCertificatePolicies(List<String> certificatePolicies) {
        this.certificatePolicies = certificatePolicies;
    }

    /**
     * Returns true if pinCode required.
     * @return true if pinCode is required.
     */
    public Boolean getPinCode() {
        return this.pinCode;
    }

    /**
     * Sets the pinCode requirement.
     * @param pinCode true if required, false to allow fingerPrint and null to use BankID default.
     */
    public void setPinCode(Boolean pinCode) {
        this.pinCode = pinCode;
    }

    /**
     * Returns the mrtd requirement.
     * @return true if MRTD (Machine readable travel document) is required.
     */
    public Boolean getMrtd() {
        return this.mrtd;
    }

    /**
     * Sets the mrtd requirement.
     * @param mrtd the mrtd requirement.
     */
    public void setMrtd(Boolean mrtd) {
        this.mrtd = mrtd;
    }

    /**
     * Returns the personalNumber requirement.
     * @return the personalNumber required to complete the transaction.
     */
    public String getPersonalNumber() {
        return this.personalNumber;
    }

    /**
     * Sets the personalNumber requirement.
     * @param personalNumber - the personalNumber required to complete the transaction.
     */
    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    /**
     * Returns the risk requirement.
     * @return the highest risk level to complete the transaction.
     */
    public RiskRequirement getRisk() {
        return this.risk;
    }

    /**
     * Sets the risk requirement.
     * @param risk - the highest risk level to complete the transaction.
     */
    public void setRisk(RiskRequirement risk) {
        this.risk = risk;
    }
}
