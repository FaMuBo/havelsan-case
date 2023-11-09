import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

import SignIn from './login';
import Map from './map';

function App() {
  return (
    <Router>
      <div>
        <Routes>
          <Route path='/' element={<SignIn />} />{' '}
          <Route path='/map' element={<Map />} />{' '}
        </Routes>
      </div>
    </Router>
  );
}

export default App;
