import React from 'react';
import {Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn} from 'material-ui/Table';
import ApiClient from '../apiclient.js';


export default class DeviceList extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            devices: []
        }
    }

    componentWillMount(){
        ApiClient.getDevices()
            .then((body) => {
                this.setState({devices: body.devices})
            })
            .catch((err) => {
            });
    }

    render(){
        return(
            <Table>
                <TableHeader className="deviceTableHeader">
                <TableRow>
                    <TableHeaderColumn>端末ID</TableHeaderColumn>
                    <TableHeaderColumn>端末名</TableHeaderColumn>
                    <TableHeaderColumn>登録日</TableHeaderColumn>
                    <TableHeaderColumn>QRコード</TableHeaderColumn>
                </TableRow>
                </TableHeader>
                <TableBody className="deviceTableBody">
                    {this.state.devices.map((device)=>{
                        var qrURL = `http://chart.apis.google.com/chart?chs=150x150&cht=qr&chl=${device.id}`;
                        return (
                            <TableRow key={device.id}>
                                <TableRowColumn>{device.id}</TableRowColumn>
                                <TableRowColumn>{device.name}</TableRowColumn>
                                <TableRowColumn>{device.created}</TableRowColumn>
                                <TableRowColumn>
                                    <a href={qrURL}>
                                        <img src={qrURL}></img>
                                    </a>
                                </TableRowColumn>
                            </TableRow>

                            )
                    })}
                </TableBody>
            </Table>
        )
    }
}