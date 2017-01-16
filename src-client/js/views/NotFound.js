import React from "react";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import {Link} from "react-router";

const style = {
    button: {
        margin: 12,
    }
};

class NotFound extends React.Component {
    render() {
        return (
            <div>
                not found
            </div>
        );
    }
}


NotFound.propTypes = {};

function mapStateToProps(state) {
    return {};
}

function mapDispatchToProps(dispatch) {
    return {};
}

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(NotFound);