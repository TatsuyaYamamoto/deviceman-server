import React from "react";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import {Link, hashHistory} from "react-router";
import RaisedButton from "material-ui/RaisedButton";
import ApiClient from "../Apiclient.js";
import LoginDialog from "../components/dialog/LoginDialog.js";
import CreateUserDialog from "../components/dialog/CreateUserDialog.js";
import AlertDialog from "../components/dialog/AlertDialog.js";
import TokenService from "../service/TokenService.js";

const styles = {
    headline: {
        fontSize: 24,
        paddingTop: 16,
        marginBottom: 12,
        fontWeight: 400,
    },
    button: {
        margin: 12,
    },
    circularProgress: {
        position: 'absolute',
        right: 0
    }
};

const BUTTON_ID = {
    TO_MANAGE_VIEW: "to_manage_view",
    TO_LIST: "to_list_view",
    TO_REGISTER_USER_VIEW: "to_register_user_view",
    TO_REGISTER_DEVICE_VIEW: "to_register_device_view"
};

const RESOURCES = {
    SUCCESS_CREATE_USER_DIALOG: {
        TITLE: "ユーザー登録が成功しました",
        DESCRIPTION: "入力されたアドレスにユーザー情報を送信しました。登録内容に間違いがないかご確認下さい（・８・）"
    }
};

const initialState = {
    loginDialog: {
        isOpen: false
    },
    createUserDialog: {
        isOpen: false
    },
    successDialog: {
        isOpen: false
    },
    isProgress: false
};

class Top extends React.Component {
    constructor(props) {
        super(props);
        this.state = initialState;
    };

    /****************************
     * ログインダイアログ
     */
    handleLoginDialog = (isOpen)=> {
        const currentState = this.state.loginDialog;
        currentState.isOpen = isOpen != null ? isOpen : !currentState.isOpen;

        this.setState({loginDialog: currentState});
    };

    handleLogin = (userId, password, errorCallback)=> {

        ApiClient.getToken(userId, password)
            .then((body) => {
                TokenService.set(body.token);
                this.handleLoginDialog(false);
                hashHistory.push('console');
            })
            .catch((err) => {
                errorCallback(err.status);
            });
    };

    /****************************
     * ユーザー作成ダイアログ
     */
    handleCreateUserDialog = (isOpen)=> {
        const currentState = this.state.createUserDialog;
        currentState.isOpen = isOpen != null ? isOpen : !currentState.isOpen;

        this.setState({createUserDialog: currentState});
    };

    handleOpenSuccessDialog = (isOpen)=> {
        const successDialogState = this.state.successDialog;
        successDialogState.isOpen = isOpen;
        this.setState({successDialog: successDialogState})
    };

    handleCreateUser = (userId, userName, address, errorCallback) => {
        ApiClient.registerUser(userId, userName, address)
            .then((obj) => {
                this.handleCreateUserDialog(false);
                this.handleOpenSuccessDialog(true);
            })
            .catch((response) => {
                errorCallback(response.status);
            });
    };


    render() {
        return (
            <div>
                <div style={{textAlign: "center"}}>
                    <img src={"/torica/img/torica.png"}/>
                    <h1>Torica</h1>
                    Toriaezu tanmatsu no kashidashi Rireki wo nokoshite okouCa.
                </div>

                <LoginDialog
                    isOpen={this.state.loginDialog.isOpen}
                    handleLogin={this.handleLogin}
                    handleOpen={this.handleLoginDialog}/>

                <CreateUserDialog
                    isOpen={this.state.createUserDialog.isOpen}
                    handleCreate={this.handleCreateUser}
                    handleClose={this.handleCreateUserDialog}/>

                <AlertDialog
                    isOpen={this.state.successDialog.isOpen}
                    onRequestClose={()=> {
                        this.handleOpenSuccessDialog(false);
                    }}
                    title={RESOURCES.SUCCESS_CREATE_USER_DIALOG.TITLE}
                    description={RESOURCES.SUCCESS_CREATE_USER_DIALOG.DESCRIPTION}/>

                <RaisedButton
                    id={BUTTON_ID.TO_MANAGE_VIEW}
                    label="管理画面"
                    onClick={()=> {
                        this.handleLoginDialog(true);
                    }}
                    fullWidth={true}
                    primary={true}
                    style={styles.button}/>

                <RaisedButton
                    id={BUTTON_ID.TO_REGISTER_USER_VIEW}
                    label="ユーザー新規登録"
                    onClick={()=> {
                        this.handleCreateUserDialog(true);
                    }}
                    fullWidth={true}
                    primary={true}
                    style={styles.button}/>

                <Link to="/list">
                    <RaisedButton
                        id={BUTTON_ID.TO_LIST}
                        label="ユーザー・端末・貸出情報"
                        fullWidth={true}
                        primary={true}
                        style={styles.button}/>
                </Link>

            </div>
        );
    }
}

Top.propTypes = {};

function mapStateToProps(state) {
    return {};
}

function mapDispatchToProps(dispatch) {
    return {};
}

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(Top);
