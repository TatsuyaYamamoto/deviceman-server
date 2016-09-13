import React from 'react';
import {Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn} from 'material-ui/Table';
import Toggle from 'material-ui/Toggle';

import dateFormat from 'dateformat';

import ApiClient from '../apiclient.js';
import Constant from '../constants.js';

const styles = {
    block: {
        maxWidth: 250,
    },
    toggle: {
        marginBottom: 16,
    },
};

export default class DeviceList extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            devices: [],
            isShowDisabledDevices: false
        };

        this.onToggleShowingDisabledDevice = this.onToggleShowingDisabledDevice.bind(this);
    }

    componentDidMount(){
        ApiClient.getDevices()
            .then((body) => {
                const devices = body.devices;
                devices.map((device)=>{
                    if(device.disabled){
                        device.disabledString ="無効"
                    }else{
                        device.disabledString ="有効"
                    }
                });


                this.setState({devices: devices})
            })
            .catch((err) => {
            });
    }

    onToggleShowingDisabledDevice(){
        this.setState({isShowDisabledDevices: !this.state.isShowDisabledDevices})
    }

    render(){
        return(

            <div>
                <Toggle
                    label="借り出し無効の端末も表示する"
                    labelPosition="right"
                    defaultToggled={false}
                    onToggle={this.onToggleShowingDisabledDevice}
                    style={styles.toggle}
                />
                <Table>
                    <TableHeader displaySelectAll={false}>
                        <TableRow>
                            <TableHeaderColumn>端末ID</TableHeaderColumn>
                            <TableHeaderColumn>端末名</TableHeaderColumn>
                            <TableHeaderColumn>登録日</TableHeaderColumn>
                            <TableHeaderColumn>QRコード</TableHeaderColumn>
                            <TableHeaderColumn>ステータス</TableHeaderColumn>
                        </TableRow>
                    </TableHeader>
                    <TableBody displayRowCheckbox={false}>
                        {this.state.devices.map((device)=>{
                            var qrURL = `http://chart.apis.google.com/chart?chs=150x150&cht=qr&chl=${device.id}`;

                            if(!this.state.isShowDisabledDevices && device.disabled){
                                return;
                            }

                            return (
                                <TableRow key={device.id}>
                                    <TableRowColumn>{device.id}</TableRowColumn>
                                    <TableRowColumn>{device.name}</TableRowColumn>
                                    <TableRowColumn>{dateFormat(device.created, Constant.DATEFORMAT_TEMPLATE)}</TableRowColumn>
                                    <TableRowColumn>
                                        <a href={qrURL}>
                                            <img src={qrURL}></img>
                                        </a>
                                    </TableRowColumn>
                                    <TableRowColumn>{device.disabledString}</TableRowColumn>
                                </TableRow>
                            )
                        })}
                    </TableBody>
                </Table>
            </div>


        )
    }
}