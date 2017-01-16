import React from "react";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import {Link, hashHistory} from "react-router";
import RaisedButton from "material-ui/RaisedButton";
import TokenService from '../service/TokenService.js';

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
    LOGOUT: "logout"
};

class AdminTop extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
    };

    handleLogout = ()=> {
        TokenService.remove();
        hashHistory.push('/')
    };

    render() {
        return (
            <div>
                <div style={{textAlign: "center"}}>
                    <img src={"/torica/img/torica-admin.png"}/>
                    <h1>Torica Administrator Console</h1>
                    Toriaezu tanmatsu no kashidashi Rireki wo nokoshite okouCa.
                </div>

                <RaisedButton
                    id={BUTTON_ID.LOGOUT}
                    label="ログアウト"
                    fullWidth={true}
                    secondary={true}
                    style={styles.button}
                    onClick={this.handleLogout}/>

                <Link to="/console/devices/new">
                    <RaisedButton
                        id={BUTTON_ID.TO_REGISTER_DEVICE_VIEW}
                        label="端末新規登録"
                        onClick={this.handleClick}
                        fullWidth={true}
                        primary={true}
                        style={styles.button}/>
                </Link>
            </div>
        );
    }
}

AdminTop.propTypes = {};

function mapStateToProps(state) {
    return {};
}

function mapDispatchToProps(dispatch) {
    return {};
}

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(AdminTop);