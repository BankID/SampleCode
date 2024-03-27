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

import {
  createContext,
  useState,
  useMemo,
  useContext,
} from 'react';

import sweJson from './swe.json';
import engJson from './eng.json';

const translationSets = [sweJson, engJson];

const getInitialTranslationSet = () => {
  const selectedLocale = translationSets.find((it) => it.htmlLang === localStorage.getItem('selectedLocale'));
  if (selectedLocale) {
    return selectedLocale;
  }

  return translationSets.find(
    (it) => navigator.languages.includes(it.htmlLang) || navigator.languages.includes(it.locale),
  ) || engJson;
};

const defaultContext = {
  translate: () => {},
  translationSets: [],
  activeTranslationSet: {},
  changeLanguage: () => {},
  activeLanguage: '',
};

const LocalizationContext = createContext(defaultContext);

const Localization = ({ children }) => {
  const [activeTranslationSet, setActiveTranslationSet] = useState(getInitialTranslationSet());

  const contextValue = useMemo(() => {
    const translate = (...keys) => {
      if (!activeTranslationSet) {
        return keys[0];
      }

      // eslint-disable-next-line no-restricted-syntax
      for (const key of keys) {
        if (activeTranslationSet[key]) {
          return activeTranslationSet[key];
        }
      }

      return keys[0];
    };

    const changeLanguage = (language) => {
      setActiveTranslationSet(translationSets.find((it) => it.htmlLang === language) || null);
      localStorage.setItem('selectedLocale', language);
    };

    return {
      translate,
      changeLanguage,
      translationSets,
      activeTranslationSet,
      activeLanguage: activeTranslationSet.htmlLang,
    };
  }, [activeTranslationSet]);

  return (
    <LocalizationContext.Provider value={contextValue}>
      {children}
    </LocalizationContext.Provider>
  );
};

export default Localization;

export const useLocalization = () => useContext(LocalizationContext);
