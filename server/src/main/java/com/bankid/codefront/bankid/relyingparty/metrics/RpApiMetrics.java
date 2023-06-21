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

package com.bankid.codefront.bankid.relyingparty.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Metrics helper for RpApi class.
 */
@Component
public class RpApiMetrics {
    private static final String METRICS_PREFIX =
            com.bankid.codefront.config.Metrics.BASE_METRICS_PREFIX
                    + "bankid.rp.";

    /**
     * Records a failed connection check.
     * @param timeTakenMillis the time in milliseconds for the connection check.
     */
    public void failedConnectionCheck(long timeTakenMillis) {
        Timer.builder(METRICS_PREFIX + "connCheck.timer.failed")
                .description("Timing of doing a failed connection check with BankID RP.")
                .register(Metrics.globalRegistry)
                .record(timeTakenMillis, TimeUnit.MILLISECONDS);

        Counter.builder(METRICS_PREFIX + "connCheck.failed")
                .description("Number of failed connection check with BankID RP")
                .register(Metrics.globalRegistry)
                .increment();
    }

    /**
     * Records a successful connection check.
     * @param timeTakenMillis the time in milliseconds for the connection check.
     */
    public void successConnectionCheck(long timeTakenMillis) {
        Timer.builder(METRICS_PREFIX + "connCheck.timer.failed")
                .description("Timing of doing a failed connection check with BankID RP.")
                .register(Metrics.globalRegistry)
                .record(timeTakenMillis, TimeUnit.MILLISECONDS);

        Counter.builder(METRICS_PREFIX + "connCheck.failed")
                .description("Number of failed connection checks.")
                .register(Metrics.globalRegistry)
                .increment();
    }
}
