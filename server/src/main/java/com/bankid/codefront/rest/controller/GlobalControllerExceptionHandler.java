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

package com.bankid.codefront.rest.controller;

import com.bankid.codefront.utils.CodeFrontWebApplicationException;
import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles global exceptions. The last bastion.
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    /**
     * Returns a JSON serialized error object with the message from the exception.
     *
     * @param e The exception to handle.
     * @return The error response.
     */
    @ResponseBody
    @ExceptionHandler(CodeFrontWebApplicationException.class)
    public ResponseEntity<String> handleCodeFrontWebApplicationException(CodeFrontWebApplicationException e) {
        String jsonMessage = "{ \"errorMessage\": \""
                + Encode.forJavaScript(e.getRawMessage())
                + "\" }";

        return new ResponseEntity<>(jsonMessage, e.getStatusCode());
    }

    /**
     * Returns a JSON serialized error object with a general error.
     *
     * @param e The exception to handle.
     * @return The error response.
     */
    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        this.logger.error("Unhandled error: {}", e.getMessage());

        String jsonMessage = "{ \"errorMessage\": \"General error\" }";

        return new ResponseEntity<>(jsonMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
