import React from 'react';
import ReactDOM from 'react-dom';
import Virksomhed from './components/virksomhed';

import './images/regnskab-baggrund.png'
import './main.css';
import 'bootstrap/dist/css/bootstrap.min.css'
import 'font-awesome-webpack'

ReactDOM.render(
  <Virksomhed />,
  document.getElementById('react')
);
