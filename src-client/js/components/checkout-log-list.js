import React from 'react';
import {Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn} from 'material-ui/Table';

import dateFormat from 'dateformat';

import ApiClient from '../apiclient.js';
import Constant from '../constants.js';

export default class CheckoutLogList extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            logs: []
        }
    }

    componentDidMount(){
        ApiClient.getCheckoutLog()
            .then((body) => {
                this.setState({logs: body.logs})
            })
            .catch((err) => {
            });
    }

    render(){
        return(
            <Table>
                <TableHeader displaySelectAll={false}>
                <TableRow>
                    <TableHeaderColumn>借出ID</TableHeaderColumn>
                    <TableHeaderColumn>借出ユーザー名</TableHeaderColumn>
                    <TableHeaderColumn>端末名</TableHeaderColumn>
                    <TableHeaderColumn>借出日</TableHeaderColumn>
                    <TableHeaderColumn>返却日</TableHeaderColumn>
                </TableRow>
                </TableHeader>
                <TableBody displayRowCheckbox={false}>
                    {this.state.logs.map((log)=>{
                        return (
                            <TableRow key={log.id}>
                                <TableRowColumn>{log.id}</TableRowColumn>
                                <TableRowColumn>{log.user.name}</TableRowColumn>
                                <TableRowColumn>{log.device.name}</TableRowColumn>
                                <TableRowColumn>{dateFormat(log.checkOutTime, Constant.DATEFORMAT_TEMPLATE)}</TableRowColumn>
                                <TableRowColumn>{dateFormat(log.dueReturnTime, Constant.DATEFORMAT_TEMPLATE)}</TableRowColumn>
                            </TableRow>

                            )
                    })}
                </TableBody>
            </Table>
        )
    }
}