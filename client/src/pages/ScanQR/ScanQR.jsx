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

import Guide from '../../components/Guide/Guide';
import QRCode from '../../components/QRCode/QRCode';
import { URLS } from '../../constants';
import useDevice from '../../hooks/useDevice';
import { useLocalization } from '../../contexts/localization/Localization';
import mobileIcon from '../../styling/images/open-mobile.svg';
import flowUtils from '../../flow-utils';
import FlowUserActionSafeguard, { pageOpenedByUserAction } from '../../components/FlowUserActionSafeguard/FlowUserActionSafeguard';

const ScanQR = () => {
  const { translate, activeLanguage } = useLocalization();
  const { isMobileOrTablet } = useDevice();
  const navigate = useNavigate();
  const location = useLocation();
  const testingType = (location.state || {}).testingType || 'identify';

  const [qrCode, setQRCode] = useState(null);
  const [statusHintCode, setStatusHintCode] = useState(null);

  // Here we take care of initiating a flow and checking the status afterwards.
  // The user will not leave the tab, so the flow is quite easy compared to
  // mobile. The only thing we need to care about is updating the QR code
  // when we get a new one.
  useEffect(() => {
    if (!pageOpenedByUserAction(location)) {
      return undefined;
    }

    flowUtils.init({
      flowType: 'qr',
      testingType,
      navigate,
      activeLanguage,
      translate,
      onUpdate: (data) => {
        setStatusHintCode(data.hintCode);
        setQRCode(data.qrCode);
      },
    });

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

          { statusHintCode && (
            <p className='guide-description'>
              {translate(`hintcode-qr-${statusHintCode}`, `hintcode-${statusHintCode}`)}
            </p>
          )}

          { statusHintCode === 'userSign' && (
          <img src={mobileIcon} alt='' className='open-mobile-icon' />
          )}

          { qrCode && (
          <QRCode qrCode={qrCode} />
          )}

          <div className='grow' />

          { statusHintCode === 'outstandingTransaction' && !isMobileOrTablet && (
            <Link
              to={URLS.openDesktopApp}
              className='guide-link'
              state={{ testingType, triggeredByUser: true }}
            >
              {translate('open-bankid-on-this-device')}
            </Link>
          )}

          { statusHintCode === 'userSign' && (
            <Link
              to={URLS.start}
              className='guide-link'
            >
              {translate('cancel')}
            </Link>
          )}
        </Guide>

        <div className='grow' />
      </div>
    </>
  );
};

export default ScanQR;
