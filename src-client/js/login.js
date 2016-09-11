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
    /* submit Event handling */
    handleSubmit: function(e){
        e.preventDefault();
        var id = this.state.id.trim();
        var name = this.state.name.trim();
        var address = this.state.address.trim();
        var password = this.state.password.trim();

        this.props.api(id, name, address, password)
    },
    render: function() {
        return (
            <div className="userListBox">
                <CreateUserForm api={this.registerUser}/>
                <h1>login</h1>
                <Form onSubmit={this.handleSubmit}>
                    <FieldGroup
                        id="formControlsText"
                        type="text"
                        label="ユーザーID"
                        value={this.state.id}
                        onChange={(e)=>this.handleIdChange(e)}
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
        );
    }
});