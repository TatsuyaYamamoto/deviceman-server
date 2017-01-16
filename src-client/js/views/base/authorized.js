import React from "react";
import {connect} from "react-redux";
import {hashHistory} from "react-router";
import TokenService from "../../service/TokenService.js";

class AuthorizedViewBase extends React.Component {

    componentWillMount() {
        this.checkLoggingin();
    }

    componentWillUpdate() {
        this.checkLoggingin();
    }

    checkLoggingin() {
        console.debug("check logging in.");

        if (TokenService.get() == null) {
            localStorage.removeItem('torica_token');
            hashHistory.push('/')
        }
    }

    render() {
        return (
            <div>
                { this.props.children }
            </div>
        );
    }
}

function mapStateToProps(state) {
    return {};
}

function mapDispatchToProps(dispatch) {
    return {};
}

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(AuthorizedViewBase);
