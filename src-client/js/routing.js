import React from 'react';
import { Route, IndexRoute } from 'react-router'

/* base view component */
import AppViewBase from './views/base/app.js'
import AuthorizedViewBase from './views/base/authorized.js'

/* general page */
import TopView from './views/top.js'
import RegisterUser from './views/register-user.js'
import RegisterDevice from './views/register-device.js'
import RegisteredList from './views/registered-list.js'

/* admin page */
import AdminTopView from './views/admin-top.js'

/* common page */
import NotFound from './views/notfound.js'

export default (
    <Route path="/" component={AppViewBase}>

        {/* Guest can read */}
        <IndexRoute component={TopView} />
        <Route path="list" component={RegisteredList} />
        <Route path="users">
            <Route path="new" component={RegisterUser} />
        </Route>

        {/* authorized user only */}
        <Route path="console" components={AuthorizedViewBase}>
            <IndexRoute component={AdminTopView} />
            <Route path="devices">
                <Route path="new" component={RegisterDevice} />
            </Route>
        </Route>

        {/* NOT FOUND */}
        <Route path='*' component={NotFound} />
    </Route>
);