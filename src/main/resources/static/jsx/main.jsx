/*********************************************
 * chekcout log component
 */
var CheckoutLog = React.createClass({
    getInitialState: function() {
        return {data: []};
    },
    componentDidMount: function() {
        var self = this;
        apiclient.getCheckoutLog(function(res){
            self.setState({data: res.body.logs});
        })
    },
    render: function() {
        return (
            <div className="checkoutLog">
                <h1>Logs</h1>
                <table>
                    <LogTableHeader />
                    <LogTableBody data={this.state.data} />
                </table>
            </div>
        );
    }
});

var LogTableHeader = React.createClass({
    render: function() {
        return (
            <thead className="LogTableHeader">
            <tr>
                <th>借出ユーザーID</th>
                <th>端末名</th>
                <th>借出日</th>
                <th>返却日</th>
            </tr>
            </thead>
        );
    }
});


var LogTableBody = React.createClass({
    render: function() {
        var commentNodes = this.props.data.map(
            (log)=>{
                return (
                    <LogTableItem
                        key={log.id}
                        userId={log.userId}
                        deviceName={log.device.name}
                        checkOutTime={log.checkOutTime}
                        returnTime={log.returnTime}>
                    </LogTableItem>
                );
            });

        return (
            <tbody className="logTableBody">
                {commentNodes}
            </tbody>
        );
    }
});

var LogTableItem = React.createClass({
    render: function() {
        var userId          = this.props.userId;
        var deviceName      = this.props.deviceName;
        var checkOutTime    = new Date(this.props.checkOutTime).toDateString();
        var returnTime      = new Date(this.props.returnTime).toDateString();

        return (
            <tr className="logItem">
                <td className="userId">{userId}</td>
                <td className="deviceName">{deviceName}</td>
                <td className="checkOutTime">{checkOutTime}</td>
                <td className="returnTime">{returnTime}</td>
            </tr>
        );
    }
});


/*********************************************
 * current chekcout list component
 */

var CurrentCheckout = React.createClass({
    getInitialState: function() {
        return {data: []};
    },
    componentDidMount: function() {
        var self = this;
        apiclient.getCheckout(function(res){
            self.setState({data: res.body.checkouts});
        });
    },
    render: function() {
        return (
            <div className="cuurentCheckout">
                <h1>Current Checkout List</h1>
                <table>
                    <CurrentCheckoutTableHeader />
                    <LogTableBody data={this.state.data} />
                </table>
            </div>
        );
    }
});

var CurrentCheckoutTableHeader = React.createClass({
    render: function() {
        return (
            <thead className="LogTableHeader">
            <tr>
                <th>借出ユーザーID</th>
                <th>端末名</th>
                <th>借出日</th>
                <th>返却予定日</th>
            </tr>
            </thead>
        );
    }
});


/*********************************************
 * user component
 */


var UserListBox = React.createClass({
    getInitialState: function() {
        return {data: []};
    },
    componentDidMount: function() {
        var self = this;
        apiclient.getUsers(function(res){
            self.setState({data: res.body.users});
        });
    },
    render: function() {
        return (
            <div className="userListBox">
                <h1>User List</h1>
                <table>
                    <UserTableHeader />
                    <UserTableBody data={this.state.data} />
                </table>
            </div>
        );
    }
});

var UserTableHeader = React.createClass({
    render: function() {
        return (
            <thead className="LogTableHeader">
            <tr>
                <th>ユーザーID</th>
                <th>ユーザー名</th>
                <th>アドレス</th>
                <th>QRコード</th>
            </tr>
            </thead>
        );
    }
});

var UserTableBody = React.createClass({
    render: function() {
        var userNodes = this.props.data.map(
            (user)=>{
                var qrURL = "http://chart.apis.google.com/chart?chs=150x150&cht=qr&chl=" + user.id;
                return (
                    <tr
                        className="logItem"
                        key={user.id}　>
                        <td className="userId">{user.id}</td>
                        <td className="userName">{user.name}</td>
                        <td className="address">{user.address}</td>
                        <td className="qrCode">
                            <a href={qrURL}>
                                <img src={qrURL}></img>
                            </a>
                        </td>
                    </tr>
                );
            });

        return (
            <tbody className="userTableBody">
            {userNodes}
            </tbody>
        );
    }
});


var UserCreateForm = React.createClass({
    render: function() {
        return (
            <div className="userCreateForm">
                <h1>User Create Form</h1>
                <form>
                    <input type="text" placeholder="employee ID" />
                    <input type="text" placeholder="your name" />
                    <input type="text" placeholder="address" />
                    <input type="password" placeholder="password" />
                    <input type="submit" value="Post" />
                </form>
            </div>
        );
    }
});




/*********************************************
 * device component
 */


var DeviceListBox = React.createClass({
    getInitialState: function() {
        return {data: []};
    },
    componentDidMount: function() {
        var self = this;
        apiclient.getDevices(function(res){
            self.setState({data: res.body.devices});
        });
    },
    render: function() {
        return (
            <div className="deviceListBox">
                <h1>Device List</h1>
                <table>
                    <DeviceTableHeader />
                    <DeviceTableBody data={this.state.data} />
                </table>
            </div>
        );
    }
});

var DeviceTableHeader = React.createClass({
    render: function() {
        return (
            <thead className="deviceTableHeader">
            <tr>
                <th>ユーザーID</th>
                <th>ユーザー名</th>
                <th>QRコード</th>
            </tr>
            </thead>
        );
    }
});

var DeviceTableBody = React.createClass({
    render: function() {
        var deviceNodes = this.props.data.map(
            (device)=>{
                var qrURL = "http://chart.apis.google.com/chart?chs=150x150&cht=qr&chl=" + device.id;
                return (
                    <tr
                        className="logItem"
                        key={device.id}　>
                        <td className="deviceId">{device.id}</td>
                        <td className="deviceName">{device.name}</td>
                        <td className="qrCode">
                            <a href={qrURL}>
                                <img src={qrURL}></img>
                            </a>
                        </td>
                    </tr>
                );
            });

        return (
            <tbody className="deviceTableBody">
            {deviceNodes}
            </tbody>
        );
    }
});






/*********************************************
 * root content component
 */



var Content = React.createClass({
    render: function() {
        return (
            <div className="content">
                <UserCreateForm />
                <UserListBox />
                <DeviceListBox />
                <CurrentCheckout />
                <CheckoutLog />
            </div>
        );
    }
});

ReactDOM.render(
    <Content/>,
    document.getElementById('content'));