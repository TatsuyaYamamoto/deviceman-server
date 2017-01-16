import React from "react";
import Dialog from "material-ui/Dialog";
import TextField from "material-ui/TextField";
import FlatButton from "material-ui/FlatButton";

const FORM_INPUT_ID = {
    USER_ID: "userId",
    PASSWORD: "password"
};

const styles = {
    dialog: {
        textAlign: 'center'
    },
    errorMessage: {
        color: 'red'
    }
};

const initState = {
    formValue: {
        id: "",
        password: ""
    },
    errorText: ''
};

const ERROR_MESSAGE = "IDまたはパスワードが正しくありません。";

export default class LoginDialog extends React.Component {
    constructor(props) {
        super(props);
        this.state = initState;
    };

    onChangeForm = (e)=> {
        const formValue = this.state.formValue;

        switch (e.currentTarget.id) {
            case FORM_INPUT_ID.USER_ID:
                formValue.id = e.target.value;
                break;
            case FORM_INPUT_ID.PASSWORD:
                formValue.password = e.target.value;
                break;
        }
        this.setState({formValue: formValue});
    };

    handleLogin = () => {
        const userId = this.state.formValue.id;
        const password = this.state.formValue.password;

        this.props.handleLogin(userId, password, (errorStatus) => {
            this.setState({errorText: ERROR_MESSAGE});
        });
    };

    render() {
        return (
            <Dialog
                title="管理画面"
                modal={false}
                open={this.props.isOpen}
                onRequestClose={()=>{
                    this.setState(initState);
                    this.props.handleOpen(false)
                }}
                style={styles.dialog}
                actions={[
                    <FlatButton
                        label="Cancel"
                        primary={true}
                        onTouchTap={()=> {
                            this.setState(initState);
                            this.props.handleOpen(false)
                        }}
                    />,
                    <FlatButton
                        label="Login"
                        primary={true}
                        keyboardFocused={true}
                        onTouchTap={this.handleLogin}
                    />,
                ]}>
                <div>
                    <TextField
                        id={FORM_INPUT_ID.USER_ID}
                        floatingLabelText="ユーザーID"
                        onChange={this.onChangeForm}/><br />
                    <TextField
                        id={FORM_INPUT_ID.PASSWORD}
                        type="password"
                        floatingLabelText="パスワード"
                        onChange={this.onChangeForm}
                        errorText={this.state.errorText}/>
                </div>
            </Dialog>
        )
    }
}

LoginDialog.propTypes = {
    isOpen: React.PropTypes.bool.isRequired,
    handleLogin: React.PropTypes.func.isRequired,
    handleOpen: React.PropTypes.func.isRequired
};