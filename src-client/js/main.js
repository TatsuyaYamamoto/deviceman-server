import React from 'react';
import ReactDOM from 'react-dom';
import {
    IndexRoute,
    Router,
    Route
} from 'react-router';
import { Provider } from 'react-redux';
import { createHistory } from 'history';

import Header from './header.jsx'
import User from './userbox.jsx'
import Device from './devicebox.jsx'
import Checkout from './checkoutbox.jsx'
import Log from './logbox.jsx'
import LoginView from './login.jsx'

require('bootstrap/dist/css/bootstrap.min.css');

/************************************
 * redux store
 */
const store = configureStore();



/************************************
 * 全画面共通の基底コンポーネント
 * リロード時の状態管理を行う
 */
var AppBase = React.createClass({
    render: function() {
        return (
            <div>
                <div>
                    { this.props.children }
                </div>
            </div>
        );
    }
});

/************************************
 * 認証済み状態のviewをラップするコンポーネント
 */
var AuthorizedIndex = React.createClass({

    // componentWillMount: function() {
    //     this.guestWillTransfer(this.props, this.context.router);
    // },
    //
    // componentWillUpdate: function(nextProps, nextState) {
    //     this.guestWillTransfer(nextProps, this.context.router);
    // },
    //
    // guestWillTransfer: function(props, router) {
    //     if (!props.auth.isLoggedIn) {
    //         router.replace('/login');
    //     }
    // },

    render: function() {
        return (
            <div>
                <Header/>
                <div>
                    { this.props.children }
                </div>
            </div>
        );
    }
});

/************************************
 * 未認証の状態のviewをラップするコンポーネント
 */
var UnauthorizedIndex = React.createClass({
    render: function() {
        return (
            <div>
                <div>
                    { this.props.children }
                </div>
            </div>
        );
    }
});

/************************************
 * 404のルーティングで表示するviewコンポーネント
 */
var NotFound = React.createClass({
    render: function () {
        return (
            <h2>Not found(・8・) oh...</h2>
        );
    }
});

/************************************
 * virtual dom render
 */
ReactDOM.render((
    <Provider store={store}>
        <Router history={createHistory}>
            <Route path="/" component={AppBase}>
                <Route component={AuthorizedIndex}>
                    <IndexRoute component={Log} />
                    <Route path="/user" component={User} />
                    <Route path="/device" component={Device} />
                    <Route path="/checkout" component={Checkout} />
                    <Route path="/log" component={Log} />
                </Route>
            </Route>

            {/*<Route component={UnauthorizedIndex}>*/}
            {/*<Route path="/login" component={LoginView} />*/}
            {/*</Route>*/}

            <Route path='*' component={NotFound} />
        </Router>
    </Provider>),
    document.getElementById('content'));