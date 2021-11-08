import { Redirect, Route } from 'react-router-dom';
import { IonApp, IonRouterOutlet } from '@ionic/react';
import { IonReactRouter } from '@ionic/react-router';

/* Core CSS required for Ionic components to work properly */
import '@ionic/react/css/core.css';

/* Basic CSS for apps built with Ionic */
import '@ionic/react/css/normalize.css';
import '@ionic/react/css/structure.css';
import '@ionic/react/css/typography.css';

/* Optional CSS utils that can be commented out */
import '@ionic/react/css/padding.css';
import '@ionic/react/css/float-elements.css';
import '@ionic/react/css/text-alignment.css';
import '@ionic/react/css/text-transformation.css';
import '@ionic/react/css/flex-utils.css';
import '@ionic/react/css/display.css';

/* Theme variables */
import './theme/variables.css';
import {MealProvider} from "./components/meal/MealProvider";
import MealList from "./components/meal/MealList";
import React from "react";
import EditMeal from "./components/meal/EditMeal";
import {AuthProvider} from "./components/auth/AuthProvider";
import {Login} from "./components/auth/Login";
import {PrivateRoute} from "./components/auth/PrivateRoute";

const App: React.FC = () => (
  <IonApp>
    <IonReactRouter>
      <IonRouterOutlet>
        <AuthProvider>
          <Route path="/login" component={Login} exact={true}/>
          <MealProvider>
            <PrivateRoute path="/meals" component={MealList} exact={true}/>
            <PrivateRoute path="/meal" component={EditMeal} exact={true}/>
            <PrivateRoute path="/meal/:id" component={EditMeal} exact={true}/>
          </MealProvider>
          <Route exact path="/" render={() => <Redirect to="/meals"/>}/>
        </AuthProvider>
      </IonRouterOutlet>
    </IonReactRouter>
  </IonApp>
);

export default App;
