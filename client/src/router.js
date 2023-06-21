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

import { useEffect } from 'react';
import { Route, Routes, useLocation } from 'react-router-dom';

import { URLS } from './constants';
import Start from './pages/Start/Start';
import ScanQR from './pages/ScanQR/ScanQR';
import OpenDesktopApp from './pages/OpenDesktopApp/OpenDesktopApp';
import OpenMobileApp from './pages/OpenMobileApp/OpenMobileApp';
import ResultSuccess from './pages/ResultSuccess/ResultSuccess';
import ResultFailed from './pages/ResultFailed/ResultFailed';
import About from './pages/About/About';
import Error from './pages/Error/Error';

const Router = () => {
  const location = useLocation();

  useEffect(() => {
    window.scrollTo({ top: 0 });
  }, [location.pathname]);

  return (
    <Routes>
      <Route path={URLS.start} element={<Start />} />
      <Route path={URLS.scanQr} element={<ScanQR />} />
      <Route path={URLS.openDesktopApp} element={<OpenDesktopApp />} />
      <Route path={URLS.openMobileApp} element={<OpenMobileApp />} />
      <Route path={URLS.resultSuccess} element={<ResultSuccess />} />
      <Route path={URLS.resultFailed} element={<ResultFailed />} />
      <Route path={URLS.about} element={<About />} />
      <Route path='*' element={<Error />} />
    </Routes>
  );
};

export default Router;
