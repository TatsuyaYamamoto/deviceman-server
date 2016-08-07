import React from 'react';
import FieldGroup from './components/field-group.jsx'
import Apiclient from '../js/apiclient.js';

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
    getDevices: function(){
        Apiclient.getDevices(function(res){
            this.setState({data: res.body.devices});
        }.bind(this));
    },
    registerDevice: function (id, name) {
        Apiclient.registerDevice(id, name, function(res){
            // nothing
        }.bind(this));
    },

    getInitialState: function() {
        return {data: []};
    },
    componentDidMount: function() {
        this.getDevices();
    },

    render: function() {
        return (
            <div className="deviceListBox">
                <h1>Device Create Form</h1>
                <CreateDeviceForm api={this.registerDevice}/>
                <h1>Device List</h1>
                <Table>
                    <DeviceTableHeader />
                    <DeviceTableBody data={this.state.data} />
                </Table>
            </div>
        );
    }
});

var DeviceTableHeader = React.createClass({
    render: function() {
        return (
            <thead className="deviceTableHeader">
            <tr>
                <th>端末ID</th>
                <th>端末名</th>
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

var CreateDeviceForm = React.createClass({
    /* initialize */
    getInitialState: function() {
        return {
            id: '',
            name: ''
        };
    },

    /* onChange Event handling */
    handleIdChange: function(e) {
        this.setState({id: e.target.value});
    },
    handleNameChange: function(e) {
        this.setState({name: e.target.value});
    },

    /* submit Event handling */
    handleSubmit: function(e){
        e.preventDefault();
        var id = this.state.id.trim();
        var name = this.state.name.trim();

        this.props.api(id, name)
    },

    render: function () {
        return (
            <Form onSubmit={this.handleSubmit}>
                <FieldGroup
                    id="formControlsText"
                    type="text"
                    label="ID"
                    placeholder="WiFi MAC address"
                    value={this.state.id}
                    onChange={(e)=>this.handleIdChange(e)}
                />
                <FieldGroup
                    id="formControlsText"
                    type="text"
                    label="Name"
                    placeholder="端末名"
                    value={this.state.name}
                    onChange={(e)=>this.handleNameChange(e)}
                />
                <Button type="submit">Submit</Button>
            </Form>
        )
    }
});