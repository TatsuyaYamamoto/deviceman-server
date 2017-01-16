import React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router'

import TextField from 'material-ui/TextField';
import FlatButton from 'material-ui/FlatButton';
import RaisedButton from 'material-ui/RaisedButton';
import Dialog from 'material-ui/Dialog';

import Header from '../components/header.js';

import ApiClient from '../Apiclient.js';

import Dropzone from 'react-dropzone'

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
    PASSWORD: "password"
};

class RegisterDevice extends React.Component {

    constructor(props){
        super(props);
        this.state = {
            isOpenSuccessDialog: false,
            formValue: {
                id: "",
                name: ""
            },
            errorText: {
                id: "",
                name: ""
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
        this.onDrop = this.onDrop.bind(this);
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
        }
        this.setState({formValue: formValue});
    }

    submit(){
        const deviceId = this.state.formValue.id;
        const deviceName = this.state.formValue.name;

        ApiClient.registerDevice(deviceId, deviceName)
            .then((obj) => {
                this.openDialog("端末登録が成功しました。")
            })
            .catch((err) => {
                switch (err.status){
                    case 400:
                        this.openDialog("入力が不正です。");
                        break;
                    case 409:
                        this.openDialog("IDが重複しています。", "別のIDを入力して下さい。");
                        break;
                }
            });
    }

    onDrop(files) {
        ApiClient.uploadCsvDevices(files[0])
            .then((obj) => {
                this.openDialog("端末登録が成功しました。")
            })
            .catch((err) => {
                switch (err.status){
                    case 400:
                        this.openDialog("不正なファイルです。");
                        break;
                }
            });
    }
    render() {
        return (
            <div>
                <h2 style={styles.headline}>端末登録</h2>

                <div>
                    <TextField
                        id={FORM_INPUT_ID.USER_ID}
                        floatingLabelText="端末ID"
                        hintText="WiFi Macアドレスを入力して下さい"
                        errorText={this.state.errorText.id}
                        onChange={this.onChangeForm}
                    /><br />
                    <TextField
                        id={FORM_INPUT_ID.USER_NAME}
                        floatingLabelText="端末名"
                        hintText="端末名を入力して下さい"
                        errorText={this.state.errorText.name}
                        onChange={this.onChangeForm}
                    />
                </div>
                <RaisedButton
                    label="登録"
                    onClick={this.submit}
                    fullWidth={true}
                    primary={true}
                    style={styles.button} />

                <Dropzone
                    onDrop={this.onDrop}
                    accept="text/csv,application/vnd.ms-excel" >
                    <div>
                        ファイルを指定またはドラッグ&ドロップ
                        <p>形式: csv</p>
                    </div>
                </Dropzone>

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


RegisterDevice.propTypes = {
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
)(RegisterDevice);