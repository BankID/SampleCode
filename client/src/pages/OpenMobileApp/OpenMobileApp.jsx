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

import { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';

import { URLS } from '../../constants';
import Guide from '../../components/Guide/Guide';
import { useLocalization } from '../../contexts/localization/Localization';
import useHash from '../../hooks/useHash';
import useDevice from '../../hooks/useDevice';
import mobileIcon from '../../styling/images/open-mobile.svg';
import flowUtils from '../../flow-utils';
import FlowUserActionSafeguard, { pageOpenedByUserAction } from '../../components/FlowUserActionSafeguard/FlowUserActionSafeguard';

const createReturnUrl = (device, testingType) => {
  // Here we create the URL that the BankID app should open when the authentication
  // is completed. On iOS all links open in Safari, even when another browser is
  // set as the default browser. So here we check if we're in another browser and
  // if so we replace the protocol with one provided by the specific browser so we
  // know it opens correctly. In some browsers we need to specify the full URL, in
  // some we can just say "Go back to Chrome".
  if (device.isChromeOnAppleDevice) {
    return encodeURIComponent('googlechrome://');
  }
  if (device.isFirefoxOnAppleDevice) {
    return encodeURIComponent('firefox://');
  }
  if (device.isOperaTouchOnAppleDevice) {
    return encodeURIComponent(`${window.location.href.replace('http', 'touch-http')}#initiated=true&testingType=${testingType}`);
  }

  return encodeURIComponent(`${window.location.href}#initiated=true&testingType=${testingType}`);
};

const OpenMobileApp = () => {
  const { translate, activeLanguage } = useLocalization();
  const location = useLocation();
  const navigate = useNavigate();
  const { hashParams } = useHash();
  const testingType = (location.state || {}).testingType || hashParams?.testingType || 'identify';
  const device = useDevice();
  const [statusHintCode, setStatusHintCode] = useState(null);
  const [openBankIDUrl, setOpenBankIDUrl] = useState(null);

  // Here we take care of initiating a flow and checking the status afterwards.
  // The flow can differ a bit depending on the device and browser. For example
  // in some browsers on iOS you will not be able to get back in the same tab,
  // and if you have an obscure browser you might not even be able to get back
  // to the same browser. So here we need to handle both when a flow is handled
  // from start to finish in one tab, as well as when a flow is started in one tab
  // and finished in another.
  useEffect(() => {
    if (!pageOpenedByUserAction(location, hashParams)) {
      return undefined;
    }

    const handleFlowUpdate = (data) => {
      setStatusHintCode(data.hintCode);
    };

    // If we don't have the field "initiated" in our hash-params it means it's our first time here.
    // Let's initiate the flow.
    if (!hashParams?.initiated) {
      // We call the API to initiate the flow.
      flowUtils.init({
        flowType: 'mobile',
        testingType,
        navigate,
        activeLanguage,
        translate,
        onInitDone: (data) => {
          // When initiating we get an "autoStartToken" back which we pass along to app.bankid.com
          // to automatically open the BankID app.
          const { autoStartToken } = data;

          // We also need to pass app.bankid.com an url to redirect back to. This needs so be set
          // based on the specific device and browser for the user to be redirected back to the
          // same tab correctly.
          // This is where we add "#initiated=true" to the URL.
          const returnUrl = createReturnUrl(device, testingType);

          // When we got everything we need we tell the user to open the BankID app, when we return
          // the flow will be continued in the else-statement below.
          setOpenBankIDUrl(`https://app.bankid.com/?autostarttoken=${autoStartToken}&redirect=${returnUrl}`);
        },
        onUpdate: handleFlowUpdate,
      });
    } else {
      // If we do have the field "initiated" in the hash it means we've already been here and
      // initiated a request and have now returned from the BankID app. Let's start polling
      // for the result.
      flowUtils.pollForResult({
        flowType: 'mobile',
        testingType,
        navigate,
        onUpdate: handleFlowUpdate,
      });
    }

    return () => {
      flowUtils.abort();
    };
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  return (
    <>
      <FlowUserActionSafeguard />

      <div className='page-container'>
        <div className='grow' />

        <Guide>

          <h3 className='guide-header'>
            { testingType === 'identify' && translate('identify-with-bankid') }
            { testingType === 'sign' && translate('sign-with-bankid') }
          </h3>

          { statusHintCode === 'userSign' && (
            <img src={mobileIcon} alt='' className='open-mobile-icon' />
          )}

          { statusHintCode && (
            <p className='guide-description'>
              {translate(`hintcode-mobile-${statusHintCode}`, `hintcode-${statusHintCode}`)}
            </p>
          )}

          {openBankIDUrl && (
            <a
              href={openBankIDUrl}
              referrerPolicy="origin"
              className='button secondary guide-button'
            >
              {translate('open-bankid-app')}
            </a>
          )}

          <div className='grow' />

          <Link
            to={URLS.scanQr}
            className='guide-link'
            state={{ testingType, triggeredByUser: true }}
          >
            {translate('open-bankid-on-another-device')}
          </Link>
        </Guide>

        <div className='grow' />
      </div>
    </>
  );
};

export default OpenMobileApp;
