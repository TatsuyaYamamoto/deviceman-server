import React from 'react';

import Dialog from 'material-ui/Dialog';
import TextField from 'material-ui/TextField';
import FlatButton from 'material-ui/FlatButton';

const FORM_INPUT_ID = {
    USER_ID: "userId",
    PASSWORD: "password"
};

const styles = {
    dialog: {
        textAlign:'center'
    },
    errorMessage: {
        color: 'red'
    }
};

export default class LoginDialog extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            isOpenSuccessDialog: false,
            formValue: {
                id: "",
                password: ""
            }
        };

        this.onChangeForm = this.onChangeForm.bind(this);
        this.login = this.login.bind(this);
    }

    onChangeForm(e){
        const formValue = this.state.formValue;

        switch(e.currentTarget.id){
            case FORM_INPUT_ID.USER_ID:
                formValue.id = e.target.value;
                break;
            case FORM_INPUT_ID.PASSWORD:
                formValue.password = e.target.value;
                break;
        }
        this.setState({formValue: formValue});
    }

    login(){
        const userId = this.state.formValue.id;
        const password = this.state.formValue.password;

        this.props.login(userId, password);
    }

    render(){
        return (
            <Dialog
                title="管理画面"
                modal={false}
                open={this.props.isOpen}
                onRequestClose={this.props.onClose}
                style={styles.dialog}
                actions={[
                    <FlatButton
                        label="Cancel"
                        primary={true}
                        onTouchTap={this.props.onClose}
                    />,
                    <FlatButton
                        label="Login"
                        primary={true}
                        keyboardFocused={true}
                        onTouchTap={this.login}
                        onClick={this.login}
                    />,
                ]}>
                <div>
                    <TextField
                        id={FORM_INPUT_ID.USER_ID}
                        floatingLabelText="ユーザーID"
                        onChange={this.onChangeForm}
                        errorText={this.props.errorText.id}
                    /><br />
                    <TextField
                        id={FORM_INPUT_ID.PASSWORD}
                        type="password"
                        floatingLabelText="パスワード"
                        onChange={this.onChangeForm}
                        errorText={this.props.errorText.password}
                    />
                </div>
            </Dialog>
        )
    }
}

LoginDialog.propTypes = {
    isOpen: React.PropTypes.bool.isRequired,
    login: React.PropTypes.func.isRequired,
    errorText: React.PropTypes.object.isRequired,
    onClose: React.PropTypes.func.isRequired
};