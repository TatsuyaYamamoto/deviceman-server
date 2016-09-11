import React from 'react';
import {
    IndexRoute,
    Router,
    Route
} from 'react-router';

import BaseApp from './views/baseapp.js'
// import LoginView from './virews/login.js'
import TopView from './views/top.js'
import RegisterUser from './views/register-user.js'
import RegisterDevice from './views/register-device.js'
import RegisteredList from './views/registered-list.js'
import NotFound from './views/notfound.js'

export default (
    <Route path="/" component={BaseApp}>
        <IndexRoute component={TopView} />
        <Route path="list" component={RegisteredList} />
        <Route path="register">
            <Route path="user" component={RegisterUser} />
            <Route path="device" component={RegisterDevice}/>
        </Route>
            {/*<Route path="/login" component={LoginView} />*/}
            {/*<Route path="/user" component={UserView} />*/}
            {/*<Route path="/humidity" component={HumidityView} />*/}


            <Route path='*' component={NotFound} />
    </Route>
)