import React from 'react';
import Apiclient from './apiclient.js';
import FieldGroup from './components/field-group.jsx'
import {
    Table,
    Col,
    Form,
    FormControl,
    FormGroup,
    ControlLabel,
    Button
} from 'react-bootstrap';



module.exports = React.createClass({
    /* event handling */
    getUsers: function(){
        Apiclient.getUsers(function(res){
            if (this.isMounted()){
                this.setState({data: res.body.users});
            }
        }.bind(this));
    },
    registerUser: function (id, name, address, password) {
        Apiclient.registerUser(id, name, address, password, function(res){
            // nothing
        }.bind(this));
    },

    /* component state */
    getInitialState: function() {
        return {data: []};
    },
    componentDidMount: function() {
        this.getUsers();
    },
    render: function() {
        return (
            <div className="userListBox">
                <CreateUserForm api={this.registerUser}/>
                <h1>User List</h1>
                <Table>
                    <UserTableHeader />
                    <UserTableBody data={this.state.data} />
                </Table>
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

var CreateUserForm = React.createClass({
    /* initialize */
    getInitialState: function() {
        return {
            id: '',
            name: '',
            address: '',
            password: ''
        };
    },

    /* onChange Event handling */
    handleIdChange: function(e) {
        this.setState({id: e.target.value});
    },
    handleNameChange: function(e) {
        this.setState({name: e.target.value});
    },
    handleAddressChange: function(e) {
        this.setState({address: e.target.value});
    },
    handlePasswordChange: function(e) {
        this.setState({password: e.target.value});
    },

    /* submit Event handling */
    handleSubmit: function(e){
        e.preventDefault();
        var id = this.state.id.trim();
        var name = this.state.name.trim();
        var address = this.state.address.trim();
        var password = this.state.password.trim();

        this.props.api(id, name, address, password)
    },
    render: function () {
        return (
            <div className="userCreateForm">
                <h1>User Create Form</h1>

                <Form onSubmit={this.handleSubmit}>
                    <FieldGroup
                        id="formControlsText"
                        type="text"
                        label="ID"
                        placeholder="社員番号とか"
                        value={this.state.id}
                        onChange={(e)=>this.handleIdChange(e)}
                    />
                    <FieldGroup
                        id="formControlsText"
                        type="text"
                        label="Name"
                        placeholder="表示名"
                        value={this.state.name}
                        onChange={(e)=>this.handleNameChange(e)}
                    />
                    <FieldGroup
                        id="formControlsText"
                        type="text"
                        label="Address"
                        placeholder="通知用アドレス"
                        value={this.state.address}
                        onChange={(e)=>this.handleAddressChange(e)}
                    />
                    <FieldGroup
                        id="formControlsText"
                        type="text"
                        label="Password"
                        placeholder="パスワード"
                        value={this.state.password}
                        onChange={(e)=>this.handlePasswordChange(e)}
                    />
                    <Button type="submit">Submit</Button>
                </Form>
            </div>
        )
    }
});