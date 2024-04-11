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

import { Link, useLocation } from 'react-router-dom';

import Guide from '../../components/Guide/Guide';
import StartButton from '../../components/StartButton/StartButton';
import { URLS } from '../../constants';
import { useLocalization } from '../../contexts/localization/Localization';
import CircleIcon from '../../components/CircleIcon/CircleIcon';

const ResultFailed = () => {
  const { translate } = useLocalization();
  const location = useLocation();
  const testingType = (location.state || {}).testingType || 'identify';
  const { statusHintCode, flowType, errorType } = location.state || {};

  return (
    <div className='page-container'>
      <div className='grow' />

      <Guide status='failed'>
        <CircleIcon type='error' className='guide-icon' />

        <div className='flex-col-center'>

          <h3 className='guide-header'>
            { testingType === 'identify' && translate('identify-with-bankid') }
            { testingType === 'sign' && translate('sign-with-bankid') }
          </h3>

          <p className='guide-description'>
            {translate(
              `error-message-${errorType}`,
              `hintcode-${flowType}-${statusHintCode || 'unknown'}`,
              `hintcode-${statusHintCode || 'unknown'}`
            )}
          </p>

          <div className='flex-col-center'>
            {errorType === 'invalidCsrfToken' ? (
              <a
                href={URLS.start}
                className='button secondary'
              >
                {translate('refresh')}
              </a>
            ) : (
              <StartButton
                testingType={testingType}
                text={translate('try-again')}
                buttonType='secondary'
              />
            )}
            <Link
              to={URLS.start}
              className='button none'
            >
              {translate('cancel')}
            </Link>
          </div>
        </div>
      </Guide>

      <div className='grow' />
    </div>
  );
};

export default ResultFailed;
