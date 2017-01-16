import React from "react";
import {Route, IndexRoute} from "react-router";
import AppViewBase from "./views/base/app.js";
import AuthorizedViewBase from "./views/base/authorized.js";
import TopView from "./views/Top.js";
import RegisterDevice from "./views/RegisterDevice.js";
import RegisteredList from "./views/RegisteredList.js";
import AdminTopView from "./views/AdminTop.js";
import NotFound from "./views/NotFound.js";

export default (
    <Route path="/" component={AppViewBase}>

        {/* Guest can read */}
        <IndexRoute component={TopView}/>
        <Route path="list" component={RegisteredList}/>
        {/* authorized user only */}
        <Route path="console" components={AuthorizedViewBase}>
            <IndexRoute component={AdminTopView}/>
            <Route path="devices">
                <Route path="new" component={RegisterDevice}/>
            </Route>
        </Route>

        {/* NOT FOUND */}
        <Route path='*' component={NotFound}/>
    </Route>
);