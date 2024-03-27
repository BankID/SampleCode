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

import com.bankid.codefront.rest.controller.GlobalControllerExceptionHandler;
import com.bankid.codefront.rest.controller.StartController;
import com.bankid.codefront.utils.CodeFrontWebApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test global exception handler.
 */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("checkstyle:magicnumber")
public class GlobalControllerExceptionHandlerTest {

    private MockMvc mockMvc;

    @Mock
    private StartController startController;

    /**
     * Setup mock before test.
     */
    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.startController)
            .setControllerAdvice(new GlobalControllerExceptionHandler())
            .build();
    }

    /**
     * Test catch of CodeFrontWebApplicationException.
     * @throws Exception
     */
    @Test
    public void testGlobalExceptionHandlerError() throws Exception {
        when(this.startController.start(any(HttpServletRequest.class), any(HttpServletResponse.class)))
               .thenThrow(new CodeFrontWebApplicationException(HttpStatus.BAD_REQUEST, "Unexpected Exception"));
        this.mockMvc.perform(get("/api/start"))
            .andExpect(status().is(400))
            .andExpect(content().string("{ \"errorMessage\": \"Unexpected Exception\" }"));
    }

    /**
     * Test catch of runtime exceptions.
     * @throws Exception
     */
    @Test
    public void testRunTimeError() throws Exception {
        when(this.startController.start(any(HttpServletRequest.class), any(HttpServletResponse.class)))
            .thenThrow(new RuntimeException("Unexpected Exception"));
        this.mockMvc.perform(get("/api/start"))
            .andExpect(status().is(500))
            .andExpect(content().string("{ \"errorMessage\": \"General error\" }"));
    }
}

