import React from 'react';
import { connect } from 'react-redux';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import getMuiTheme from 'material-ui/styles/getMuiTheme';

import Theme from '../Theme.js';

class AppViewBase extends React.Component {

    render() {
        return (
        <MuiThemeProvider muiTheme={getMuiTheme(Theme)}>
            { this.props.children }
        </MuiThemeProvider>
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
)(AppViewBase);