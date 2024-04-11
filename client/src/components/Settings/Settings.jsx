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
import { useLocation } from 'react-router-dom';
import { URLS } from '../../constants';

import { useLocalization } from '../../contexts/localization/Localization';
import Button from '../Button/Button';
import FoldableContent from '../FoldableContent/FoldableContent';
import StartButton from '../StartButton/StartButton';
import externalLinkIcon from '../../styling/images/system-external-link.svg';

const Settings = () => {
  const { translate, activeLanguage } = useLocalization();
  const location = useLocation();

  const inProgress = [
    URLS.openDesktopApp, URLS.openMobileApp, URLS.scanQr,
  ].includes(location.pathname);
  const { testingType } = location.state || {};

  const defaultIdentificationText = translate('default-identify-text');
  const [identificationText, setIdentificationText] = useState(sessionStorage.getItem(`identify.text.${activeLanguage}`) ?? defaultIdentificationText);
  const [formattedIdentificationText, setFormattedIdentificationText] = useState(
    !sessionStorage.getItem('identify.text.formatted')
      || sessionStorage.getItem('identify.text.formatted') === 'true',
  );

  const defaultSigningText = translate('default-sign-text');
  const [signText, setSignText] = useState(sessionStorage.getItem(`sign.text.${activeLanguage}`) ?? defaultSigningText);
  const [formattedSignText, setFormattedSignText] = useState(
    !sessionStorage.getItem('sign.text.formatted')
      || sessionStorage.getItem('sign.text.formatted') === 'true',
  );

  useEffect(() => {
    sessionStorage.setItem(`identify.text.${activeLanguage}`, identificationText);
    sessionStorage.setItem('identify.text.formatted', formattedIdentificationText);

    sessionStorage.setItem(`sign.text.${activeLanguage}`, signText);
    sessionStorage.setItem('sign.text.formatted', formattedSignText);
  }, [identificationText, formattedIdentificationText, signText, formattedSignText]);

  useEffect(() => {
    setIdentificationText(sessionStorage.getItem(`identify.text.${activeLanguage}`) ?? defaultIdentificationText);
    setSignText(sessionStorage.getItem(`sign.text.${activeLanguage}`) ?? defaultSigningText);
  }, [activeLanguage]);

  return (
    <>
      <FoldableContent title={translate('identification-text')}>
        <textarea
          className='setting-textarea'
          value={identificationText}
          onChange={(e) => setIdentificationText(e.target.value)}
        />

        <div className='sidebar-settings-formatting-container'>
          <label
            className='checkbox-label'
            htmlFor='formattedIdentificationTextCheckbox'
          >
            <input
              id='formattedIdentificationTextCheckbox'
              type='checkbox'
              className='checkbox'
              checked={formattedIdentificationText}
              onChange={(e) => setFormattedIdentificationText(e.target.checked)}
            />
            {translate('use-formatted-identification-text')}
          </label>

          <a
            href={translate('formatting-guide-url')}
            target="_blank"
            rel="noopener noreferrer"
            className="sidebar-settings-link"
          >
            {translate('formatting-guide')}
            <img src={externalLinkIcon} alt='' />
          </a>
        </div>

        <div className='button-row space-between'>
          <Button
            to={URLS.start}
            text={translate('cancel')}
            buttonType='tertiary'
            disabled={!(inProgress && testingType === 'identify')}
          />

          <StartButton
            testingType='identify'
            text={translate('start-test')}
            disabled={inProgress || (formattedIdentificationText && !identificationText.length)}
            title={formattedIdentificationText && !identificationText.length ? translate('identification-text-validation-error') : undefined}
          />
        </div>

      </FoldableContent>

      <FoldableContent title={translate('signing-text')}>
        <textarea
          className='setting-textarea'
          value={signText}
          onChange={(e) => setSignText(e.target.value)}
        />

        <div className='sidebar-settings-formatting-container'>
          <label
            className='checkbox-label'
            htmlFor='formattedSignTextCheckbox'
          >
            <input
              id='formattedSignTextCheckbox'
              type='checkbox'
              className='checkbox'
              checked={formattedSignText}
              onChange={(e) => setFormattedSignText(e.target.checked)}
              />
            {translate('use-formatted-signing-text')}
          </label>
          <a
            href={translate('formatting-guide-url')}
            target="_blank"
            rel="noopener noreferrer"
            className="sidebar-settings-link"
          >
            {translate('formatting-guide')}
            <img src={externalLinkIcon} alt='' />
          </a>
        </div>

        <div className='button-row space-between'>
          <Button
            to={URLS.start}
            text={translate('cancel')}
            buttonType='tertiary'
            disabled={!(inProgress && testingType === 'sign')}
          />

          <StartButton
            testingType='sign'
            text={translate('start-test')}
            disabled={inProgress || !signText.length}
            title={!signText.length ? translate('sign-text-validation-error') : undefined}
          />
        </div>

      </FoldableContent>
    </>
  );
};

export default Settings;
