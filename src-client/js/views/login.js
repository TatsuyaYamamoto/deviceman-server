import React from 'react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { routerActions } from 'react-router-redux'
import { Link, hashHistory } from 'react-router'

import { authAction } from '../actions/auth.js'
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';

class LoginForm extends React.Component {
    render() {
        return (
            <div>
                <FormInput handleClick={this.props.onClick} />
                <FormDisplay data={this.props.value} />
            </div>
        );
    }
}
LoginForm.propTypes = {
    onClick: React.PropTypes.func.isRequired,
    value: React.PropTypes.string,
};

// Veiw (Presentational Components)
class FormInput extends React.Component {
    constructor(props) {
        super(props);
    }

    submit(e) {
        hashHistory.push('/himono')

        e.preventDefault();
        this.props.handleClick(
            this.refs.userId.getValue().trim(),
            this.refs.password.getValue().trim()
        );
    }

    render() {
        return (
        <div style={{textAlign: "center"}}>
            <h1>HIMONO SYSTEM CONSOLE</h1>
            <form>
                <TextField
                    ref="userId"
                    defaultValue=""
                    hintText=""
                    floatingLabelText="UserID"/>
                <br />
                <TextField
                    ref="password"
                    defaultValue=""
                    hintText=""
                    floatingLabelText="Password"
                    type="password"/>
                <br />
                <RaisedButton
                    onClick={(event) => this.submit(event)}
                    primary={true}
                    label="Login" />

            </form>

            <br/>
            <RaisedButton label="Create new user!" />
        </div>);
    }
}
FormInput.propTypes = {
    handleClick: React.PropTypes.func.isRequired,
};

// Veiw (Presentational Components)
class FormDisplay extends React.Component {
    render() {
        return (
            <div>{this.props.data}</div>
        );
    }
}
FormDisplay.propTypes = {
    data: React.PropTypes.string,
};

/*******************************************************
 * Connect to Redux
 */

/**
 *
 * @param state
 * @returns {{value: *}}
 */
function mapStateToProps(state) {
    return {
        value: state.value,
    };
}
function mapDispatchToProps(dispatch) {
    return {
        onClick(userId, password) {
            dispatch(authAction.requestLogin(userId, password));
        }
    };
}

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(LoginForm);