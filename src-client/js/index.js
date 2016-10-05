import React from 'react';
import ReactDOM from 'react-dom';
import { createStore, combineReducers, applyMiddleware } from 'redux';
import { Provider } from 'react-redux';
import { Router, hashHistory } from 'react-router'

import injectTapEventPlugin from 'react-tap-event-plugin';
injectTapEventPlugin();

import thunk from 'redux-thunk';

import Routing from './routing.js';

import authReducer from './reducers/auth-reducer.js'

const reducers = combineReducers({
    authReducer
});

const store = createStore(
    reducers,
    applyMiddleware(thunk));


/****************************
 * Rendering
 */
ReactDOM.render(
    <Provider store={store}>
        <Router history={hashHistory} routes={Routing} />
    </Provider>,
    document.querySelector('#content')
);