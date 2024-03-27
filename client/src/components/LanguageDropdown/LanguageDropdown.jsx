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

import { useState } from 'react';
import classNames from 'classnames';

import sweFlag from '../../styling/images/sv.svg';
import engFlag from '../../styling/images/en.svg';
import downIcon from '../../styling/images/angle-down-solid-darkblue.svg';
import { useLocalization } from '../../contexts/localization/Localization';

const FLAG_MAP = {
  sv: sweFlag,
  en: engFlag,
};

const LanguageDropdown = () => {
  const {
    translate, translationSets, activeTranslationSet, changeLanguage,
  } = useLocalization();

  const [open, setOpen] = useState(false);

  const handleOpenToggle = () => {
    setOpen(!open);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleSelectLanguage = (htmlLang) => {
    changeLanguage(htmlLang);
    setOpen(false);
  };

  return (
    <div className={classNames('language-dropdown', open && 'open')}>
      { open && (
        // eslint-disable-next-line
        <div
          className='language-dropdown-overlay'
          onClick={handleClose}
        />
      )}
      <button
        type='button'
        className='language-dropdown-item-active'
        onClick={handleOpenToggle}
      >
        <img
          className='language-dropdown-flag'
          src={FLAG_MAP[activeTranslationSet.htmlLang]}
          alt=''
        />
        <span>{activeTranslationSet['lang-name']}</span>
        <img
          className='language-dropdown-down-icon'
          src={downIcon}
          alt={translate('expand')}
        />
      </button>

      <div className='language-dropdown-options'>
        {translationSets.map((translationSet) => (
          <button
            type='button'
            key={translationSet.htmlLang}
            className='language-dropdown-item'
            onClick={() => handleSelectLanguage(translationSet.htmlLang)}
          >
            <img
              className='language-dropdown-flag'
              src={FLAG_MAP[translationSet.htmlLang]}
              alt=''
            />
            <span>{translationSet['lang-name-long']}</span>
          </button>
        ))}
      </div>
    </div>
  );
};

export default LanguageDropdown;
