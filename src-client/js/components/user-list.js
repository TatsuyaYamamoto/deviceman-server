import React from 'react';
import {Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn} from 'material-ui/Table';
import ApiClient from '../apiclient.js';


export default class UserList extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            users: []
        }
    }

    componentWillMount(){
        ApiClient.getUsers()
            .then((body) => {
                this.setState({users: body.users})
            })
            .catch((err) => {
            });
    }

    render(){
        return(
            <Table>
                <TableHeader>
                <TableRow>
                    <TableHeaderColumn>ユーザーID</TableHeaderColumn>
                    <TableHeaderColumn>ユーザー名</TableHeaderColumn>
                    <TableHeaderColumn>アドレス</TableHeaderColumn>
                    <TableHeaderColumn>QRコード</TableHeaderColumn>
                </TableRow>
                </TableHeader>
                <TableBody>
                    {this.state.users.map((user)=>{
                        var qrURL = `http://chart.apis.google.com/chart?chs=150x150&cht=qr&chl=${user.id}`;
                        return (
                            <TableRow key={user.id}>
                                <TableRowColumn>{user.id}</TableRowColumn>
                                <TableRowColumn>{user.name}</TableRowColumn>
                                <TableRowColumn>{user.address}</TableRowColumn>
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