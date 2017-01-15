import React from "react";
import {Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn} from "material-ui/Table";
import TextField from "material-ui/TextField";

export default class UserList extends React.Component {
    render() {
        return (
            <div>
                <TextField
                    floatingLabelText="Search with ID, NAME, and Address"
                    type="search"/>
                <Table>
                    <TableHeader displaySelectAll={false}>
                        <TableRow>
                            <TableHeaderColumn>ユーザーID</TableHeaderColumn>
                            <TableHeaderColumn>ユーザー名</TableHeaderColumn>
                            <TableHeaderColumn>アドレス</TableHeaderColumn>
                        </TableRow>
                    </TableHeader>
                    <TableBody displayRowCheckbox={false}>
                        {this.props.users.map((user)=> {
                            return (
                                <TableRow key={user.id}>
                                    <TableRowColumn>{user.id}</TableRowColumn>
                                    <TableRowColumn>{user.name}</TableRowColumn>
                                    <TableRowColumn>{user.address}</TableRowColumn>
                                </TableRow>
                            )
                        })}
                    </TableBody>
                </Table>
            </div>
        )
    }
}

UserList.propTypes = {
    users: React.PropTypes.array.isRequired
};
