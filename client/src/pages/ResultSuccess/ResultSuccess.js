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

import { useLocation } from 'react-router-dom';
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';

import Guide from '../../components/Guide/Guide';
import { useLocalization } from '../../contexts/localization/Localization';
import CircleIcon from '../../components/CircleIcon/CircleIcon';
import StartButton from '../../components/StartButton/StartButton';

// BankID Markdown treats *words surrounded by asteriskt* as bold,
// not italic which is the default markdown behaviour, so in this example
// where we show the text in the UI we need to customize the library
// rendering our markdown.
const reactMarkdownModifications = {
  em: ({ node, ...props }) => <strong {...props} />,
};

const ResultSuccess = () => {
  const { translate } = useLocalization();
  const location = useLocation();
  const testingType = (location.state || {}).testingType || 'identify';
  const { completionResponse } = location.state || {};

  return (
    <div className='page-container'>
      <div className='grow' />

      <Guide status='success'>
        <CircleIcon type='success' className='guide-icon' />

        <div className='flex-col-center'>
          <h3 className='guide-header'>
            {testingType === 'identify' && translate('identification-success')}
            {testingType === 'sign' && translate('sign-success')}
          </h3>

          {completionResponse && (
            <div className='result-grid'>
              <span>
                {translate('name')}
                :
              </span>
              <span>{completionResponse.name}</span>

              <span>
                {translate('personal-number')}
                :
              </span>
              <span>{completionResponse.personalNumber}</span>

              {completionResponse.signedText && (
                <div className='result-grid-signed-text'>
                  <p className='result-grid-signed-text-header'>
                    {translate('signed-text')}
                    :
                  </p>

                  <ReactMarkdown
                    className='result-grid-intention-text-markdown'
                    remarkPlugins={[remarkGfm]}
                    components={reactMarkdownModifications}
                  >
                    {completionResponse.signedText}
                  </ReactMarkdown>
                </div>
              )}
            </div>
          )}

          {testingType === 'identify' && (
            <div className='button-row'>
              <StartButton
                testingType='sign'
                text={translate('test-sign')}
                buttonType='secondary'
              />
            </div>
          )}

        </div>
      </Guide>

      <div className='grow' />
    </div>
  );
};

export default ResultSuccess;
