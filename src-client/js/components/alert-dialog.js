import React from 'react';

import Dialog from 'material-ui/Dialog';
import TextField from 'material-ui/TextField';
import FlatButton from 'material-ui/FlatButton';

export default class AlertDialog extends React.Component {
    constructor(props){
        super(props);
    }

    render(){
        return (
            <Dialog
                title={this.props.title}
                actions={[
                    <FlatButton
                        label="OK"
                        primary={true}
                        onTouchTap={this.props.onRequestClose}/>
                ]}
                keyboardFocused={true}
                modal={false}
                open={this.props.isOpen}
                onRequestClose={this.props.onRequestClose}>
                {this.props.description}
            </Dialog>
        )
    }
}

AlertDialog.propTypes = {
    isOpen: React.PropTypes.bool.isRequired,
    onRequestClose: React.PropTypes.func.isRequired,
    title: React.PropTypes.string,
    description: React.PropTypes.string
};