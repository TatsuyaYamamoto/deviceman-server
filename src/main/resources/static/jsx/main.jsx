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
 * root content component
 */



var Content = React.createClass({
    render: function() {
        return (
            <div className="content">
                <CurrentCheckout />
                <CheckoutLog />
            </div>
        );
    }
});

ReactDOM.render(
    <Content/>,
    document.getElementById('content'));