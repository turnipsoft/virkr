import React from 'react';
import { Switch, Route } from 'react-router-dom'
import HomeView from '../views/homeview';
import SoegeresultatView from '../views/soegeresultat/soegeresultatview';
import DeltagerView from '../views/deltager/deltagerview';

const Main = () => (
  <main>
    <Switch>
      <Route exact path='/' component={HomeView}/>
      <Route path='/soegeresultat/:soegning' component={SoegeresultatView} />
      <Route path='/deltager/:enhedsnummer' component={DeltagerView} />
    </Switch>
  </main>
);

export default Main;

