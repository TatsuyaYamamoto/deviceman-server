import React from 'react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { Link, browserHistory } from 'react-router'

import Dialog from 'material-ui/Dialog';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';
import FlatButton from 'material-ui/FlatButton';

import Header from '../components/header.js'

const styles = {
    headline: {
        fontSize: 24,
        paddingTop: 16,
        marginBottom: 12,
        fontWeight: 400,
    },
    button: {
        margin: 12,
    }
};

const BUTTON_ID = {
    TO_MANAGE_VIEW: "to_manage_view",
    TO_LIST: "to_list_view",
    TO_REGISTER_USER_VIEW: "to_register_user_view",
    TO_REGISTER_DEVICE_VIEW: "to_register_device_view"
};

class Top extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            isOpenLoginDialog: false,
        };

        this.openDialog = this.openDialog.bind(this);
        this.closeDialog = this.closeDialog.bind(this);
    };

    openDialog(){
        this.setState({isOpenLoginDialog: true});
    };

    closeDialog(){
        this.setState({isOpenLoginDialog: false});
    };


    submitLoginRequest(){
        this.setState({isOpenLoginDialog: false});
    }


    render() {
        return (
            <div>
                <div style={{textAlign: "center"}}>
                    <img src={"/img/torica.png"} />
                    <h1>Torica</h1>
                    Toriaezu tanmatsu no Rireki wo nokoshite okouCa.

                </div>

                <LoginDialog
                    isOpen={this.state.isOpenLoginDialog}
                    open={this.openDialog}
                    close={this.closeDialog}/>

                <RaisedButton
                    id={BUTTON_ID.TO_MANAGE_VIEW}
                    label="管理画面"
                    onClick={this.openDialog}
                    fullWidth={true}
                    primary={true}
                    style={styles.button} />

                <Link to="/list">
                    <RaisedButton
                        id={BUTTON_ID.TO_LIST}
                        label="登録済みリスト"
                        fullWidth={true}
                        primary={true}
                        style={styles.button} />
                </Link>

                <Link to="/register/user">
                    <RaisedButton
                        id={BUTTON_ID.TO_REGISTER_USER_VIEW}
                        label="ユーザー新規登録"
                        onClick={this.handleClick}
                        fullWidth={true}
                        primary={true}
                        style={styles.button} />
                </Link>

                <Link to="/register/device">
                    <RaisedButton
                        id={BUTTON_ID.TO_REGISTER_DEVICE_VIEW}
                        label="端末新規登録"
                        onClick={this.handleClick}
                        fullWidth={true}
                        primary={true}
                        style={styles.button} />
                    </Link>
            </div>
        );
    }
}

class LoginDialog extends React.Component {

    render(){
        return (
            <Dialog
                title="管理画面"
                modal={false}
                open={this.props.isOpen}
                onRequestClose={this.props.close}
                actions={[
                    <FlatButton
                        label="Cancel"
                        primary={true}
                        onTouchTap={this.props.close}
                    />,
                    <FlatButton
                        label="Submit"
                        primary={true}
                        keyboardFocused={true}
                        onTouchTap={this.close}
                    />,
                ]}>
                <div>
                    <TextField
                        floatingLabelText="ユーザーID"
                    /><br />
                    <TextField
                        type="password"
                        floatingLabelText="パスワード"
                        id="text-field-default"
                    />
                </div>
                <RaisedButton
                    onClick={this.props.close}
                    label="ログイン"
                    fullWidth={true}
                    primary={true}
                    style={styles.button} />
            </Dialog>
        )
    }
}

LoginDialog.propTypes = {
    isOpen: React.PropTypes.bool.isRequired,
    open: React.PropTypes.func.isRequired,
    close: React.PropTypes.func.isRequired
};



Top.propTypes = {
};

function mapStateToProps(state) {
    return {
    };
}

function mapDispatchToProps(dispatch) {
    return {
    };
}

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(Top);