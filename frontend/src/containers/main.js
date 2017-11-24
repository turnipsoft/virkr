import React from 'react';
import { Switch, Route } from 'react-router-dom'
import HomeView from '../views/home/homeview';
import SoegeresultatView from '../views/soegeresultat/soegeresultatview';
import DeltagerView from '../views/deltager/deltagerview';
import VirksomhedView from '../views/virksomhed/virksomhedview';
import DeltagerGrafView from  '../views/deltager/graf/deltagergrafview';
import EjerGrafView from '../views/virksomhed/ejergraf/ejergrafview';

const Main = () => (
  <div>
    <Switch>
      <Route exact path='/' component={HomeView} />
      <Route path='/deltagergraf/:enhedsnummer' component={DeltagerGrafView} />
      <Route path='/ejergraf/:cvrnummer' component={EjerGrafView} />
      <Route path='/soegeresultat/:soegning' component={SoegeresultatView} />
      <Route path='/deltager/:enhedsnummer' component={DeltagerView} />
      <Route path='/virksomhed/:cvrnummer' component={VirksomhedView} />
    </Switch>
  </div>
);


export default Main;

