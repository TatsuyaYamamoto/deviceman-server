import React from "react";
import {Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn} from "material-ui/Table";
import TextField from 'material-ui/TextField';

import dateFormat from "dateformat";
import ApiClient from "../../apiclient.js";
import Constant from "../../constants.js";

const styles = {
    block: {
        maxWidth: 250,
    },
    toggle: {
        marginBottom: 16,
    },
};

export default class DeviceList extends React.Component {
    render() {
        return (

            <div>
                <TextField
                    floatingLabelText="Search with ID, NAME, and Address"
                    type="search"/>
                <Table>
                    <TableHeader displaySelectAll={false}>
                        <TableRow>
                            <TableHeaderColumn>端末ID</TableHeaderColumn>
                            <TableHeaderColumn>端末名</TableHeaderColumn>
                            <TableHeaderColumn>登録日</TableHeaderColumn>
                            <TableHeaderColumn>QRコード</TableHeaderColumn>
                        </TableRow>
                    </TableHeader>
                    <TableBody displayRowCheckbox={false}>
                        {this.props.devices.map((device)=> {
                            var qrURL = `http://chart.apis.google.com/chart?chs=150x150&cht=qr&chl=${device.id}`;

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
                                </TableRow>
                            )
                        })}
                    </TableBody>
                </Table>
            </div>
        )
    }
}
DeviceList.propTypes = {
    devices: React.PropTypes.array.isRequired
};
