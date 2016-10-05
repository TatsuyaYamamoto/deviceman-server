import React from 'react';
import { connect } from 'react-redux';
import { hashHistory } from 'react-router'

class AuthorizedViewBase extends React.Component {

    componentWillMount(){
        this.checkLoggingin();
    }
    componentWillUpdate(){
        this.checkLoggingin();
    }

    checkLoggingin(){
        console.debug("check logging in.");
        const token = localStorage.getItem('torica_token');

        if(token == null || token != 'tmp'){
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
)(AuthorizedViewBase);