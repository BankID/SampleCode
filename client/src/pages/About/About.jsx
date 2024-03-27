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

import { useNavigate } from 'react-router-dom';
import Button from '../../components/Button/Button';
import { useLocalization } from '../../contexts/localization/Localization';
import backgroundImage from '../../styling/images/eid-still.png';
import { URLS } from '../../constants';

const Start = () => {
  const { translate } = useLocalization();
  const navigate = useNavigate();

  const handleBackClick = (e) => {
    e.preventDefault();
    navigate(-1);
  };

  return (
    <>
      <div className='thin-content-container'>
        <h1>
          {translate('about-title')}
        </h1>

        <p className='subtitle-paragraph'>
          {translate('about-subtitle')}
        </p>

        <h4>{translate('about-paragraphs-title-1')}</h4>
        <p>{translate('about-paragraphs-body-1')}</p>

        <h4>{translate('about-paragraphs-title-2')}</h4>
        <p>{translate('about-paragraphs-body-2')}</p>

        <h4>{translate('about-paragraphs-title-3')}</h4>
        <p>{translate('about-paragraphs-body-3')}</p>

        <Button
          to={URLS.start}
          onClick={handleBackClick}
          text={translate('back')}
          buttonType='tertiary'
        />

      </div>

      <div className='background-container'>
        <img
          src={backgroundImage}
          alt=''
          className='background-image'
        />
      </div>
    </>
  );
};

export default Start;
