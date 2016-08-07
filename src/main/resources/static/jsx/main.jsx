import React from 'react';
import ReactDOM from 'react-dom';
import {
    IndexRoute,
    Router,
    Route,
    hashHistory
} from 'react-router';

import Header from './header.jsx'
import User from './userbox.jsx'
import Device from './devicebox.jsx'
import Checkout from './checkoutbox.jsx'
import Log from './logbox.jsx'

require('bootstrap/dist/css/bootstrap.min.css');

var Index = React.createClass({
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
var NotFound = React.createClass({
    render: function () {
        return (
            <h2>Not found(・8・) oh...</h2>
        );
    }
});

ReactDOM.render((
    <Router history={hashHistory}>
        <Route path="/" component={Index}>
            <IndexRoute component={Log} />
            <Route path="/user" component={User} />
            <Route path="/device" component={Device} />
            <Route path="/checkout" component={Checkout} />
            <Route path="/log" component={Log} />
        </Route>
        <Route path='*' component={NotFound} />
    </Router>
), document.getElementById('content'));