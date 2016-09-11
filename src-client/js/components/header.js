import React from 'react';
import AppBar from 'material-ui/AppBar';
import FlatButton from 'material-ui/FlatButton';

import LeftNav from './leftnav.js';

export default class Header extends React.Component {
    constructor(props){
        super(props);
        this.handleToggle = this.handleToggle.bind(this);
        this.state = {open: false};
    }


    handleToggle(open){
        this.setState({open: open});
    }

    handleLogoutButton() {
        alert('onTouchTap triggered on the title component');
    }

    render() {
        return (
            <div>
                <AppBar
                    title=""
                    onTitleTouchTap={this.handleTouchTap}
                    iconElementRight={
                        <FlatButton
                            label="LOGOUT"
                            onClick={()=>this.handleLogoutButton()}/>
                    }
                    onLeftIconButtonTouchTap={()=>this.handleToggle(!this.state.open)}/>

                <LeftNav
                    open={this.state.open}
                    handleToggle={this.handleToggle}/>

            </div>


        );
    }
}
Header.propTypes = {

};