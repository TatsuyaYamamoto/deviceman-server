import React from 'react';

import Dialog from 'material-ui/Dialog';
import TextField from 'material-ui/TextField';
import FlatButton from 'material-ui/FlatButton';

const FORM_INPUT_ID = {
    USER_ID: "userId",
    USER_NAME: "userName",
    ADDRESS: "address"
};

const styles = {
    dialog: {
        textAlign:'center'
    },
    errorMessage: {
        color: 'red'
    }
};


export default class CreateUserDialog extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            isOpen: false,
            formValue: {
                id: "",
                name: "",
                password: ""
            }
        };

        this.onChangeForm = this.onChangeForm.bind(this);
        this.create = this.create.bind(this);
    }

    onChangeForm(e){
        const formValue = this.state.formValue;

        switch(e.currentTarget.id){
            case FORM_INPUT_ID.USER_ID:
                formValue.id = e.target.value;
                break;
            case FORM_INPUT_ID.USER_NAME:
                formValue.name = e.target.value;
                break;
            case FORM_INPUT_ID.ADDRESS:
                formValue.address = e.target.value;
                break;
        }
        this.setState({formValue: formValue});
    }

    create(){
        const userId = this.state.formValue.id;
        const userName = this.state.formValue.name;
        const address = this.state.formValue.address;

        this.props.create(userId, userName, address);
    }

    render(){
        return (
            <Dialog
                title="新規ユーザー登録"
                modal={false}
                contentStyle={styles.dialog}
                open={this.props.isOpen}
                onRequestClose={this.props.onClose}
                actions={[
                    <FlatButton
                        label="Cancel"
                        primary={true}
                        onTouchTap={this.props.onClose}
                    />,
                    <FlatButton
                        label="Submit"
                        primary={true}
                        keyboardFocused={true}
                        onTouchTap={this.create}
                    />
                ]}>
                <div>
                    <TextField
                        id={FORM_INPUT_ID.USER_ID}
                        floatingLabelText="社員番号"
                        onChange={this.onChangeForm}
                        errorText={this.props.errorText.id}
                    /><br />
                    <TextField
                        id={FORM_INPUT_ID.USER_NAME}
                        floatingLabelText="ユーザー名"
                        onChange={this.onChangeForm}
                        errorText={this.props.errorText.name}
                    /><br />
                    <TextField
                        id={FORM_INPUT_ID.ADDRESS}
                        floatingLabelText="アドレス"
                        onChange={this.onChangeForm}
                        errorText={this.props.errorText.address}
                    />
                </div>
            </Dialog>
        )
    }
}

CreateUserDialog.propTypes = {
    isOpen: React.PropTypes.bool.isRequired,
    create: React.PropTypes.func.isRequired,
    errorText: React.PropTypes.object.isRequired,
    onClose: React.PropTypes.func.isRequired
};