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
/**
 * Here we've decided to use a library for creating the QR images. At the time of
 * writing this library is safe to use. But you should always do your research
 * and not use libraries you do not trust.
 */
import QRLib from 'qrcode';
import { useLocalization } from '../../contexts/localization/Localization';

const qrCodeOptions = {
  width: 200,
  margin: 3,
  errorCorrectionLevel: 'L',
};

const QrCode = ({ qrCode }) => {
  const { translate } = useLocalization();
  const [qrImage, setQrImage] = useState();

  useEffect(() => {
    QRLib.toDataURL(qrCode, qrCodeOptions)
      .then((url) => {
        setQrImage(url);
      })
      .catch(() => {
        setQrImage(undefined);
      });
  }, [qrCode]);

  if (!qrImage) {
    return null;
  }

  return (
    <img src={qrImage} alt={translate('qr-code')} />
  );
};

export default QrCode;
