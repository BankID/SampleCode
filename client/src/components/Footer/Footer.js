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

import { useLocalization } from '../../contexts/localization/Localization';
import githubLogo from '../../styling/images/github.svg';
import { URLS, GITHUB_URL } from '../../constants';

const Footer = () => {
  const { translate } = useLocalization();
  const location = useLocation();
  const year = new Date().getFullYear();

  const pathName = location.pathname.endsWith('/')
    ? location.pathname.substring(0, location.pathname.length - 1)
    : location.pathname;

  const footerOnTopOfContent = !pathName
    || [URLS.start, URLS.about].includes(pathName)
    || !Object.values(URLS).includes(pathName);

  return (
    <div className={classNames('footer container', footerOnTopOfContent && 'on-top-of-content')}>
      <span className='inline-block'>
        Copyright &copy;
        {year}
        {' '}
        Finansiell ID-Teknik AB. All rights reserved.
      </span>
      <span className='inline-block'>
        <Link to={URLS.about}>{translate('footer-about')}</Link>
        &nbsp;
        <span>|</span>
        &nbsp;
        <a
          href={translate('footer-cookies-link')}
          target='_blank'
          rel='noopener noreferrer'
        >
          {translate('footer-cookies')}
        </a>
        &nbsp;
        <span>|</span>
        &nbsp;
        <a
          href={translate('footer-integrity-link')}
          target='_blank'
          rel='noopener noreferrer'
        >
          {translate('footer-integrity')}
        </a>
      </span>

      {!footerOnTopOfContent && (
        <>
          <div className='grow' />
          <a
            href={GITHUB_URL}
            className='github-link'
            target='_blank'
            rel='noopener noreferrer'
          >
            {translate('read-the-code-on-github')}
            <img src={githubLogo} alt='' />
          </a>
        </>
      )}
    </div>
  );
};

export default Footer;
