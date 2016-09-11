import React from 'react';
import ReactDOM from 'react-dom';
import { createStore, combineReducers, applyMiddleware } from 'redux';
import { Provider } from 'react-redux';
import { Router, hashHistory } from 'react-router'
import { syncHistoryWithStore, routerReducer } from 'react-router-redux'

import injectTapEventPlugin from 'react-tap-event-plugin';
injectTapEventPlugin();

import thunk from 'redux-thunk';

// import authReducer from './reducers/auth.js'
import Routes from './routes.js'

/* Storeの実装 */
import initialState from './initialState.js'

const reducers = combineReducers({
    // authReducer,
    routing: routerReducer
});

const store = createStore(
    reducers,
    applyMiddleware(thunk));

const history = syncHistoryWithStore(hashHistory, store);

// Rendering
ReactDOM.render(
    <Provider store={store}>
        <Router history={history} routes={Routes} />
    </Provider>,
    document.querySelector('#content')
);