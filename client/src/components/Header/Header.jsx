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
import classNames from 'classnames';

import { URLS } from '../../constants';
import logo from '../../styling/images/logo-bank-id.svg';
import settingsCog from '../../styling/images/gear-solid.svg';
import { useLocalization } from '../../contexts/localization/Localization';
import LanguageDropdown from '../LanguageDropdown/LanguageDropdown';
import { useSidebar } from '../../contexts/sidebar/Sidebar';
import HelpText from '../HelpText/HelpText';

const Header = () => {
  const { toggle: toggleSidebar } = useSidebar();
  const { translate } = useLocalization();
  const { isOpen: sidebarOpen } = useSidebar();
  const location = useLocation();

  const shouldShowHelpText = !location.pathname
    || location.pathname === URLS.start
    || location.pathname === URLS.about;

  return (
    <div className={classNames('header-container', sidebarOpen && 'sidebar-open')}>
      <div className='header container'>
        <Link
          to={URLS.start}
          className='header-link'
        >
          <img
            src={logo}
            alt='BankID'
            className='header-logo'
          />
          <span>{translate('page-title')}</span>
        </Link>

        <div className='header-right-container'>
          <div className='container'>
            <div className='header-right-content'>
              <LanguageDropdown />

              <button
                type='button'
                className='settings-button'
                onClick={toggleSidebar}
              >
                <img
                  className='settings-icon'
                  src={settingsCog}
                  alt={translate('settings')}
                />
              </button>

              {shouldShowHelpText && (
                <HelpText />
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Header;
