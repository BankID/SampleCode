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

import api from './api';
import { URLS } from './constants';
import { getTextOptions } from './utils';

let getResultTimeout;
let inProgress = true;

// If the user cancels the flow in the browser, for example when
// navigating away, we need to call the API to cancel the flow.
const abort = () => {
  clearTimeout(getResultTimeout);
  if (inProgress) {
    api.cancel();
  }
};

// Function to start polling for a result.
const pollForResult = ({
  transactionId,
  testingType,
  flowType,
  navigate,
  onUpdate,
  onError,
}) => {
  const getResult = () => {
    // We call the API to get the status.
    api.check()
      .then((response) => {
        if (transactionId && response.data.transactionId !== transactionId) {
          inProgress = false;
          navigate(URLS.resultFailed, {
            state: {
              testingType,
              flowType,
              statusHintCode: response.data.hintCode,
            },
          });

          return;
        }

        // We report back the data to the components to show helpful text to the user.
        if (typeof onUpdate === 'function') {
          onUpdate(response.data);
        }

        switch (response.data.status) {
          // If the status is PENDING we wait a second then check again.
          case 'PENDING': {
            getResultTimeout = setTimeout(getResult, 1000);
            break;
          }
          // If the status is COMPLETE we navigate away.
          case 'COMPLETE': {
            inProgress = false;
            navigate(URLS.resultSuccess, {
              state: {
                testingType,
                completionResponse: response.data.completionResponse,
              },
            });
            break;
          }
          // Same if the status is FAILED.
          case 'FAILED': {
            inProgress = false;
            navigate(URLS.resultFailed, {
              state: {
                testingType,
                flowType,
                statusHintCode: response.data.hintCode,
              },
            });
            break;
          }
          default:
            // Unknown status code.
            // eslint-disable-next-line no-console
            console.log(`Unknown status ${response.data.status}`);
        }
      }).catch(onError);
  };

  getResult();
};

const init = ({
  flowType,
  testingType,
  navigate,
  activeLanguage,
  translate,
  onInitDone,
  onUpdate,
}) => {
  // If anything bad happens, we navigate away to an error page.
  const handleError = (err) => {
    let errorType;
    if (err.response.status === 403) {
      errorType = 'invalidCsrfToken';
    }

    navigate(URLS.resultFailed, {
      state: {
        testingType,
        flowType,
        errorType,
      },
    });
  };

  // We find out what API function to use, based on whether we're identifying or signing.
  const initFunction = testingType === 'identify' ? api.authentication : api.sign;

  // We call the API to initate the flow.
  initFunction(
    // In this example we pass along the text settings from the sidebar.
    getTextOptions(testingType, activeLanguage, translate),
  ).then((response) => {
    // If init is successful we return the init data needed to continue the flow.
    if (typeof onInitDone === 'function') {
      onInitDone(response.data);
    }

    // Then we start polling for the result.
    pollForResult({
      transactionId: response.data.transactionId,
      flowType,
      testingType,
      navigate,
      onUpdate,
      onError: handleError,
    });
  }).catch(handleError);
};

export default {
  init,
  pollForResult,
  abort,
};
