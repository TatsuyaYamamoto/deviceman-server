import React from "react";
import {Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn} from "material-ui/Table";
import dateFormat from "dateformat";
import Constant from "../../Constants.js";
import DeviceDetailDialog from "../../components/dialog/DeviceDetailDialog.js";

const styles = {
    block: {
        maxWidth: 250,
    },
    toggle: {
        marginBottom: 16,
    },
};

export default class DeviceList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            deviceDetailDialog: {
                isOpen: false,
                device: {}
            }
        };
    }

    handleOpenDeviceDetailDialog = (isOpen, rowIndex, columnIndex) => {
        const dialogState = this.state.deviceDetailDialog;
        dialogState.isOpen = isOpen;
        dialogState.device = this.props.devices[rowIndex];

        this.setState({deviceDetailDialog: dialogState});
    };

    render() {
        return (
            <div>
                <DeviceDetailDialog
                    isOpen={this.state.deviceDetailDialog.isOpen}
                    handleOpen={this.handleOpenDeviceDetailDialog}
                    device={this.state.deviceDetailDialog.device}/>
                <Table onCellClick={(rowIndex, columnIndex)=> {
                    this.handleOpenDeviceDetailDialog(true, rowIndex, columnIndex)
                }}>
                    <TableHeader displaySelectAll={false}>
                        <TableRow>
                            <TableHeaderColumn>端末ID(IMEI)</TableHeaderColumn>
                            <TableHeaderColumn>MACアドレス</TableHeaderColumn>
                            <TableHeaderColumn>端末名</TableHeaderColumn>
                            <TableHeaderColumn>登録日</TableHeaderColumn>
                        </TableRow>
                    </TableHeader>
                    <TableBody
                        showRowHover={true}
                        displayRowCheckbox={false}>
                        {this.props.devices.map((device)=> {
                            return (
                                <TableRow key={device.id}>
                                    <TableRowColumn>{device.id}</TableRowColumn>
                                    <TableRowColumn>{device.macAddress}</TableRowColumn>
                                    <TableRowColumn>{device.name}</TableRowColumn>
                                    <TableRowColumn>
                                        {dateFormat(device.created, Constant.DATEFORMAT_TEMPLATE)}</TableRowColumn>
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
