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

package com.bankid.codefront.rest.controller.metrics;

import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Metrics;

import java.util.concurrent.TimeUnit;

/**
 * Helper class for TransactionController to handle metrics.
 */
@Component
public class TransactionControllerMetrics {
    private static final String METRICS_PREFIX =
            com.bankid.codefront.config.Metrics.BASE_METRICS_PREFIX
            + "rest.transactions.";

    /**
     * Add metrics for a successful start authentication call.
     * @param timeTakenMillis the time taken to handle the call.
     */
    public void successStartAuthentication(long timeTakenMillis) {
        Timer.builder(METRICS_PREFIX + "auth.timer.success")
                .description("Timing of creating successful authentication request.")
                .register(Metrics.globalRegistry)
                .record(timeTakenMillis, TimeUnit.MILLISECONDS);

        Counter.builder(METRICS_PREFIX + "auth.success")
                .description("Number of successful authentication request.")
                .register(Metrics.globalRegistry)
                .increment();
    }

    /**
     * Add metrics for a failed start authentication call.
     * @param timeTakenMillis the time taken to handle the call.
     */
    public void failedStartAuthentication(long timeTakenMillis) {
        Timer.builder(METRICS_PREFIX + "auth.timer.failed")
                .description("Timing of creating failed authentication request.")
                .register(Metrics.globalRegistry)
                .record(timeTakenMillis, TimeUnit.MILLISECONDS);

        Counter.builder(METRICS_PREFIX + "auth.failed")
                .description("Number of failed authentication request.")
                .register(Metrics.globalRegistry)
                .increment();
    }

    /**
     * Add metrics for a successful start signature call.
     * @param timeTakenMillis the time taken to handle the call.
     */
    public void successStartSign(long timeTakenMillis) {
        Timer.builder(METRICS_PREFIX + "sign.timer.success")
                .description("Timing of creating successful signature request.")
                .register(Metrics.globalRegistry)
                .record(timeTakenMillis, TimeUnit.MILLISECONDS);

        Counter.builder(METRICS_PREFIX + "sign.success")
                .description("Number of successful signature request.")
                .register(Metrics.globalRegistry)
                .increment();
    }

    /**
     * Add metrics for a failed start signature call.
     * @param timeTakenMillis the time taken to handle the call.
     */
    public void failedStartSign(long timeTakenMillis) {
        Timer.builder(METRICS_PREFIX + "sign.timer.failed")
                .description("Timing of creating failed signature request.")
                .register(Metrics.globalRegistry)
                .record(timeTakenMillis, TimeUnit.MILLISECONDS);

        Counter.builder(METRICS_PREFIX + "sign.failed")
                .description("Number of failed signature request.")
                .register(Metrics.globalRegistry)
                .increment();
    }

    /**
     * Add metrics for a successful collect call.
     * @param timeTakenMillis the time taken to handle the call.
     */
    public void successCollect(long timeTakenMillis) {
        Timer.builder(METRICS_PREFIX + "collect.timer.success")
                .description("Timing of collecting request.")
                .register(Metrics.globalRegistry)
                .record(timeTakenMillis, TimeUnit.MILLISECONDS);

        Counter.builder(METRICS_PREFIX + "collect.success")
                .description("Number of collecting request.")
                .register(Metrics.globalRegistry)
                .increment();
    }

    /**
     * Add metrics for a successful full transaction.
     * @param timeTakenSeconds the time taken to handle the transaction.
     */
    public void finalizedTransaction(long timeTakenSeconds) {
        Timer.builder(METRICS_PREFIX + "finalized.timer.success")
                .description("Timing of collecting finalized request.")
                .register(Metrics.globalRegistry)
                .record(timeTakenSeconds, TimeUnit.SECONDS);

        Counter.builder(METRICS_PREFIX + "finalized.success")
                .description("Number of finalized request.")
                .register(Metrics.globalRegistry)
                .increment();
    }

    /**
     * Add metrics for a failed full transaction.
     * @param timeTakenSeconds the time taken to handle the transaction.
     */
    public void failedTransaction(long timeTakenSeconds) {
        Timer.builder(METRICS_PREFIX + "finalized.timer.failed")
                .description("Timing of collecting finalized request.")
                .register(Metrics.globalRegistry)
                .record(timeTakenSeconds, TimeUnit.SECONDS);

        Counter.builder(METRICS_PREFIX + "finalized.failed")
                .description("Number of failed request.")
                .register(Metrics.globalRegistry)
                .increment();
    }

    /**
     * Add metrics for a failed domain check against host header.
     */
    public void domainMismatch() {
        Counter.builder(METRICS_PREFIX + "domain_mismatch")
            .description("Number of domain mismatches.")
            .register(Metrics.globalRegistry)
            .increment();
    }
}
