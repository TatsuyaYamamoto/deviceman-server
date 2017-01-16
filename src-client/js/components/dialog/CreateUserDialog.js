import React from "react";
import Dialog from "material-ui/Dialog";
import TextField from "material-ui/TextField";
import FlatButton from "material-ui/FlatButton";

const FORM_INPUT_ID = {
    USER_ID: "userId",
    USER_NAME: "userName",
    ADDRESS: "address"
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
        name: "",
        address: ""
    },
    errorText: {
        id: "",
        name: "",
        address: ""
    },
    isSubmitButtonEnable: false
};

const TextFieldProps = {
    id: {
        errorText: "fx00000の形式で入力して下さい。",
        regExp: /^fx[0-9]{5}$/
    },
    name: {
        errorText: "60文字以内で入力して下さい。",
        regExp: /^.{1,60}$/
    },
    address: {
        errorText: "アドレスを入力して下さい",
        regExp: /[a-z0-9\+\-_]+(\.[a-z0-9\+\-_]+)*@([a-z0-9\-]+\.)+[a-z]{2,6}/
    }
};

export default class CreateUserDialog extends React.Component {
    constructor(props) {
        super(props);
        this.state = Object.assign(initState);
    };

    onChangeForm = (e) => {
        const formValue = this.state.formValue;
        const errorText = this.state.errorText;
        const inputed = e.target.value;

        switch (e.currentTarget.id) {
            case FORM_INPUT_ID.USER_ID:
                formValue.id = inputed;
                if (inputed.match(TextFieldProps.id.regExp) == null) {
                    errorText.id = TextFieldProps.id.errorText;
                } else {
                    errorText.id = "";
                }
                break;
            case FORM_INPUT_ID.USER_NAME:
                formValue.name = inputed;
                if (inputed.match(TextFieldProps.name.regExp) == null) {
                    errorText.name = TextFieldProps.name.errorText;
                } else {
                    errorText.name = "";
                }
                break;
            case FORM_INPUT_ID.ADDRESS:
                formValue.address = inputed;
                if (inputed.match(TextFieldProps.address.regExp) == null) {
                    errorText.address = TextFieldProps.address.errorText;
                } else {
                    errorText.address = "";
                }
                break;
        }
        const enable =
            formValue.id.length > 0 && errorText.id == '' &&
            formValue.name.length > 0 && errorText.name == '' &&
            formValue.address.length > 0 && errorText.address == '';

        this.setState({
            formValue: formValue,
            errorText: errorText,
            isSubmitButtonEnable: enable
        });
    };

    handleCreate = () => {
        const userId = this.state.formValue.id;
        const userName = this.state.formValue.name;
        const address = this.state.formValue.address;

        this.props.handleCreate(userId, userName, address, (status)=> {
            if (status == 409) {
                const errorText = this.state.errorText;
                errorText.id = "IDが重複しています。";

                this.setState({errorText: errorText});
            }
        });
    };

    render() {
        return (
            <Dialog
                title="新規ユーザー登録"
                modal={false}
                contentStyle={styles.dialog}
                open={this.props.isOpen}
                onRequestClose={
                    ()=> {
                        this.props.handleClose();
                        this.setState(initState);
                    }}
                actions={[
                    <FlatButton
                        label="Cancel"
                        primary={true}
                        onTouchTap={()=> {
                            this.props.handleClose();
                            this.setState(initState);
                        }}/>,
                    <FlatButton
                        label="Submit"
                        primary={true}
                        keyboardFocused={true}
                        disabled={!this.state.isSubmitButtonEnable}
                        onTouchTap={this.handleCreate}/>
                ]}>
                <div>
                    <TextField
                        id={FORM_INPUT_ID.USER_ID}
                        floatingLabelText="社員番号"
                        onChange={this.onChangeForm}
                        errorText={this.state.errorText.id}
                    /><br />
                    <TextField
                        id={FORM_INPUT_ID.USER_NAME}
                        floatingLabelText="ユーザー名"
                        onChange={this.onChangeForm}
                        errorText={this.state.errorText.name}
                    /><br />
                    <TextField
                        id={FORM_INPUT_ID.ADDRESS}
                        floatingLabelText="アドレス"
                        onChange={this.onChangeForm}
                        errorText={this.state.errorText.address}
                    />
                </div>
            </Dialog>
        )
    }
}

CreateUserDialog.propTypes = {
    isOpen: React.PropTypes.bool.isRequired,
    handleCreate: React.PropTypes.func.isRequired,
    handleClose: React.PropTypes.func.isRequired
};
