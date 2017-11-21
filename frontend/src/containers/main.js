import React from 'react';
import { Switch, Route } from 'react-router-dom'
import HomeView from '../views/home/homeview';
import SoegeresultatView from '../views/soegeresultat/soegeresultatview';
import DeltagerView from '../views/deltager/deltagerview';
import VirksomhedView from '../views/virksomhed/virksomhedview';

const Main = () => (
  <div>
    <Switch>
      <Route exact path='/' component={HomeView} />
      <Route path='/soegeresultat/:soegning' component={SoegeresultatView} />
      <Route path='/deltager/:enhedsnummer' component={DeltagerView} />
      <Route path='/virksomhed/:cvrnummer' component={VirksomhedView} />
    </Switch>
  </div>
);

export default Main;

