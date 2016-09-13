import React from 'react';
import {Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn} from 'material-ui/Table';

import dateFormat from 'dateformat';

import ApiClient from '../apiclient.js';
import Constant from '../constants.js';


export default class CheckoutList extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            checkouts: []
        }
    }

    componentDidMount(){
        ApiClient.getCheckout()
            .then((body) => {
                this.setState({checkouts: body.checkouts})
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
                    <TableHeaderColumn>返却予定日</TableHeaderColumn>
                </TableRow>
                </TableHeader>
                <TableBody displayRowCheckbox={false}>
                    {this.state.checkouts.map((checkout)=>{
                        return (
                            <TableRow key={checkout.id}>
                                <TableRowColumn>{checkout.id}</TableRowColumn>
                                <TableRowColumn>{checkout.user.name}</TableRowColumn>
                                <TableRowColumn>{checkout.device.name}</TableRowColumn>
                                <TableRowColumn>{dateFormat(checkout.checkOutTime, Constant.DATEFORMAT_TEMPLATE)}</TableRowColumn>
                                <TableRowColumn>{dateFormat(checkout.dueReturnTime, Constant.DATEFORMAT_TEMPLATE)}</TableRowColumn>
                            </TableRow>

                            )
                    })}
                </TableBody>
            </Table>
        )
    }
}