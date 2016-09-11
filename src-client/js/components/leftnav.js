import React from 'react';
import Drawer from 'material-ui/Drawer';
import MenuItem from 'material-ui/MenuItem';
import { Link, hashHistory } from 'react-router'
import {spacing, typography, zIndex} from 'material-ui/styles';
import {cyan500} from 'material-ui/styles/colors';

const styles = {
    logo: {
        cursor: 'pointer',
        fontSize: 24,
        color: typography.textFullWhite,
        lineHeight: `${spacing.desktopKeylineIncrement}px`,
        fontWeight: typography.fontWeightLight,
        backgroundColor: cyan500,
        paddingLeft: spacing.desktopGutter,
        marginBottom: 8,
    }
}
export default class LeftNav extends React.Component {
    constructor(props){
        super(props);
    }
    linkTo(path){
        hashHistory.push(path);
    }
    render() {
        return (
            <Drawer
                docked={false}
                width={200}
                open={this.props.open}
                onRequestChange={(open) => {this.props.handleToggle(open)}}>

                <div style={styles.logo} onTouchTap={()=>{this.props.handleToggle(false)}}>
                    Torica
                </div>

                <MenuItem onClick={()=>this.linkTo('/')}>TOP</MenuItem>
                <MenuItem onClick={()=>this.linkTo('/user')}>USER</MenuItem>
                <MenuItem onClick={()=>this.linkTo('/humidity')}>HUMIDITY</MenuItem>
            </Drawer>

        );
    }
}
LeftNav.propTypes = {
    open: React.PropTypes.bool.isRequired,
    handleToggle: React.PropTypes.func.isRequired
};