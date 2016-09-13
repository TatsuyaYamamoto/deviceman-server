import React from 'react';
import ReactDOM from 'react-dom'
import { connect } from 'react-redux';
import { Link } from 'react-router'

import TextField from 'material-ui/TextField';
import FlatButton from 'material-ui/FlatButton';
import RaisedButton from 'material-ui/RaisedButton';
import Dialog from 'material-ui/Dialog';

import Header from '../components/header.js';

import ApiClient from '../apiclient.js';

import request from 'superagent';

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

const FORM_INPUT_ID = {
    USER_ID: "userId",
    USER_NAME: "userName",
    ADDRESS: "address",
};

class RegisterUser extends React.Component {

    constructor(props){
        super(props);
        this.state = {
            isOpenSuccessDialog: false,
            formValue: {
                id: "",
                name: "",
                address: "",
            },
            errorText: {
                id: "",
                name: "",
                address: "",
            },
            dialog: {
                title: "",
                description: ""
            }
        };

        this.openDialog = this.openDialog.bind(this);
        this.closeSuccessDialog = this.closeSuccessDialog.bind(this);
        this.onChangeForm = this.onChangeForm.bind(this);
        this.submit = this.submit.bind(this);
    }

    openDialog(title, description){
        const dialog = this.state.dialog;
        dialog.title  = title;
        dialog.description = description;

        this.setState({dialog: dialog});
        this.setState({isOpenSuccessDialog: true})
    }

    closeSuccessDialog(){
        this.setState({isOpenSuccessDialog: false})
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

    submit(){
        const userId = this.state.formValue.id;
        const userName = this.state.formValue.name;
        const address = this.state.formValue.address;

        ApiClient.registerUser(userId, userName, address)
            .then((obj) => {
                this.openDialog("ユーザー登録が成功しました。")
            })
            .catch((err) => {
                switch (err.status){
                    case 400:
                        this.openDialog("入力が不正です。");
                        break;
                    case 409:
                        this.openDialog("ユーザーIDが重複しています。", "別のIDを入力して下さい。");
                        break;
                }
            });
    }

    render() {
        return (
            <div>
                <h2 style={styles.headline}>ユーザー登録</h2>

                <div>
                    <TextField
                        id={FORM_INPUT_ID.USER_ID}
                        floatingLabelText="ユーザーID"
                        hintText="社員番号を入力して下さい"
                        errorText={this.state.errorText.id}
                        onChange={this.onChangeForm}
                    /><br />
                    <TextField
                        id={FORM_INPUT_ID.USER_NAME}
                        floatingLabelText="ユーザー名"
                        hintText="名前を入力して下さい"
                        errorText={this.state.errorText.name}
                        onChange={this.onChangeForm}
                    /><br />
                    <TextField
                        id={FORM_INPUT_ID.ADDRESS}
                        floatingLabelText="アドレス"
                        hintText="通知用のアドレスを入力して下さい"
                        errorText={this.state.errorText.address}
                        onChange={this.onChangeForm}
                    />
                </div>
                <RaisedButton
                    label="登録"
                    onClick={this.submit}
                    fullWidth={true}
                    primary={true}
                    style={styles.button} />


                <Dialog
                    title={this.state.dialog.title}
                    actions={[
                        <FlatButton
                            label="OK"
                            primary={true}
                            onTouchTap={this.closeSuccessDialog}/>
                    ]}
                    modal={false}
                    open={this.state.isOpenSuccessDialog}
                    onRequestClose={this.closeSuccessDialog}>
                    {this.state.dialog.description}
                </Dialog>
            </div>
        );
    }
}


RegisterUser.propTypes = {
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
)(RegisterUser);