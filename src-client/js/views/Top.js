import React from "react";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import {Link, hashHistory} from "react-router";
import CircularProgress from "material-ui/CircularProgress";
import RaisedButton from "material-ui/RaisedButton";
import ApiClient from "../Apiclient.js";
import LoginDialog from "../components/dialog/LoginDialog.js";
import CreateUserDialog from "../components/dialog/CreateuserDialog.js";
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
        isOpen: false,
        error: {
            id: "",
            name: "",
            address: "",
        }
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
                console.log(body)
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
    openCreateUserDialog = ()=> {
        const state = this.state.createUserDialog;
        state.isOpen = true;
        state.error = {};

        this.setState({createUserDialog: state});
    }

    closeCreateUserDialog = ()=> {
        const state = this.state.createUserDialog;
        state.isOpen = false;

        this.setState({createUserDialog: state});
    }

    openSuccessDialog = ()=> {
        const successDialogState = this.state.successDialog;
        successDialogState.isOpen = true;
        this.setState({successDialog: successDialogState})
    }

    closeSuccessDialog = ()=> {
        const successDialogState = this.state.successDialog;
        successDialogState.isOpen = false;
        this.setState({successDialog: successDialogState})
    }

    handleCreateUser = (userId, userName, address) => {
        console.trace("Top#createUser()", userId, userName, address);

        this.setState({isProgress: true});

        ApiClient.registerUser(userId, userName, address)
            .then((obj) => {
                this.closeCreateUserDialog();
                this.openSuccessDialog();
                this.setState({isProgress: false})
            })
            .catch((response) => {
                const state = this.state.createUserDialog;

                switch (response.status) {
                    case 400:
                        const errors = response.body.errors;
                        state.error = {};
                        errors.forEach((error)=> {
                            if (error.field == 'id') {
                                state.error.id = error.message;
                            }
                            if (error.field == 'name') {
                                state.error.name = error.message;
                            }
                            if (error.field == 'address') {
                                state.error.address = error.message;
                            }
                        });
                        break;
                    case 409:
                        state.error.id = "ユーザーIDが重複しています。別のIDを入力して下さい。";
                        break;
                }

                this.setState({createUserDialog: state, isProgress: false});
            });
    };


    render() {
        return (
            <div>
                {this.state.isProgress && <CircularProgress style={styles.circularProgress}/>}

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
                    errorText={this.state.createUserDialog.error}
                    create={this.handleCreateUser}
                    onClose={this.closeCreateUserDialog}/>

                <AlertDialog
                    isOpen={this.state.successDialog.isOpen}
                    onRequestClose={this.closeSuccessDialog}
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
                    onClick={this.openCreateUserDialog}
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
