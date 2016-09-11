import React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router'

import {Tabs, Tab} from 'material-ui/Tabs';

import UserList from '../components/user-list.js'
import DeviceList from '../components/device-list.js'
import CheckoutList from '../components/checkout-list.js'
import CheckoutLogList from '../components/checkout-log-list.js'

class RegisteredList extends React.Component {

    constructor(props){
        super(props);
        this.state = {
            value: 'a',
        };

        this.handleChangeTab = this.handleChangeTab.bind(this);
    }

    handleChangeTab(value){
        this.setState({
            value: value,
        });
    };

    openDialog(title, description){
        const dialog = this.state.dialog;
        dialog.title  = title;
        dialog.description = description;

        this.setState({dialog: dialog});
        this.setState({isOpenSuccessDialog: true})
    }

    render() {
        return (
            <Tabs
                value={this.state.value}
                onChange={this.handleChangeTab}>

                <Tab label="ユーザーリスト" value="a" >
                    <UserList/>
                </Tab>
                <Tab label="端末リスト" value="b">
                    <DeviceList/>
                </Tab>
                <Tab label="借り出し中端末リスト" value="c">
                    <CheckoutList />
                </Tab>
                <Tab label="借り出し履歴" value="d">
                    <CheckoutLogList />
                </Tab>
            </Tabs>
        );
    }
}


RegisteredList.propTypes = {
};

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
)(RegisteredList);