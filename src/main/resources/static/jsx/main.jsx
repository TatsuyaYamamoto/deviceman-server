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

ReactDOM.render((
    <Router history={hashHistory}>
        <Route path="/" component={Index}>
            <IndexRoute component={Log} />
            <Route path="/user" component={User} />
            <Route path="/device" component={Device} />
            <Route path="/checkout" component={Checkout} />
            <Route path="/log" component={Log} />
        </Route>
    </Router>
), document.getElementById('content'));