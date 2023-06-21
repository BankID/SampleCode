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

package com.bankid.codefront.bankid.relyingparty.signature;

import com.bankid.codefront.models.Base64String;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Parses and optionally validates signature.
 */
public class DigitalSignature {
    private final String signatureUsage;
    private final String userNonVisibleData;
    private final String userVisibleData;

    /**
     * Creates an instance of the parsed digital signature.
     * @param xmlSignatureB64 the signature to parse.
     */
    public DigitalSignature(Base64String xmlSignatureB64)
            throws SignatureParseException {
        if (xmlSignatureB64 == null) {
            throw new NullPointerException("xmlSignature is null");
        }

        // Decode the base64 encoded xml signature to a regular XML string.
        String xmlSignature = new String(
                Base64.getDecoder().decode(
                    xmlSignatureB64.getValue()),
                    StandardCharsets.UTF_8);

        try {
            // Parse
            Document doc = parseAsDocument(xmlSignature);
            XPath xPath = XPathFactory.newInstance().newXPath();
            xPath.setNamespaceContext(new BidSignatureNamespaceContext());

            Element docElement = doc.getDocumentElement();

            String bidSignedData = "/digsig:Signature/digsig:Object/bidsig:bankIdSignedData";

            this.signatureUsage =
                    getXpathString(
                        xPath,
                        bidSignedData + "/bidsig:clientInfo/bidsig:funcId",
                        docElement);

            this.userNonVisibleData =
                    getXpathString(
                        xPath,
                        bidSignedData + "/bidsig:usrNonVisibleData",
                        docElement);

            this.userVisibleData =
                    getXpathString(
                            xPath,
                            bidSignedData + "/bidsig:usrVisibleData",
                            docElement);
        } catch (IOException | ParserConfigurationException | XPathExpressionException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new SignatureParseException(e);
        }
    }

    /**
     * Returns the signature usage, either Identification or Signature.
     * @return the signature usage.
     */
    public String getSignatureUsage() {
        return this.signatureUsage;
    }

    /**
     * Returns the user visible data.
     * @return the user visible data.
     */
    public String getUserVisibleData() {
        return this.userVisibleData;
    }

    /**
     * Returns the user non-visible data.
     * @return the user non-visible data.
     */
    public String getUserNonVisibleData() {
        return this.userNonVisibleData;
    }

    /**
     * Parses a string as an XML document.
     * @param xml the xml string.
     * @return a Document.
     */
    private Document parseAsDocument(String xml) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setNamespaceAware(true);
        dbf.setValidating(false);
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

        DocumentBuilder db = dbf.newDocumentBuilder();
        try (OutputStreamWriter errorWriter = new OutputStreamWriter(System.err, StandardCharsets.UTF_8)) {
            db.setErrorHandler(new XmlParseErrorHandler(new PrintWriter(errorWriter, true)));
            InputSource is = new InputSource(new StringReader(xml));
            return db.parse(is);
        }
    }


    /**
     * Returns the value for a xpath in a document.
     * @param xPath           the base xpath object.
     * @param path            the xpath to the value.
     * @param documentElement the document.
     * @throws XPathExpressionException If <code>path</code> cannot be evaluated.
     *
     * @return the value for the xpath or null.
     *
     */
    private static String getXpathString(XPath xPath, String path, Element documentElement)
            throws XPathExpressionException {

        Element element = getXpath(xPath, path, documentElement);

        if (element == null) {
            return null;
        }

        return element.getTextContent();
    }

    /**
     * Returns the Element for a xpath in a document.
     * @param xPath           the base xpath object.
     * @param path            the xpath to the value.
     * @param documentElement the document.
     * @throws XPathExpressionException If <code>path</code> cannot be evaluated.
     *
     * @return the Element for the xpath.
     *
     */
    private static Element getXpath(XPath xPath, String path, Element documentElement)
            throws XPathExpressionException {

        return (Element) xPath.evaluate(
                path,
                documentElement,
                XPathConstants.NODE);
    }
}
