import React from "react";
import {connect} from "react-redux";
import {Link} from "react-router";
import {Tabs, Tab} from "material-ui/Tabs";
import TextField from "material-ui/TextField";
import IconButton from "material-ui/IconButton";
import SearchIcon from "material-ui/svg-icons/action/search";
import UserList from "../components/table/UserListTable.js";
import DeviceList from "../components/table/DeviceTable.js";
import CheckoutList from "../components/checkout-list.js";
import ApiClient from "../Apiclient.js";

const KEYCODE_ENTER = 13;
const TABS = {
    USER: "user",
    DEVICE: "device",
    CHECKOUT: "checkout"
};

class RegisteredList extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            tab: TABS.USER,
            users: [],
            devices: [],
            checkouts: [],
            searchQuery: ""
        };

    }

    componentDidMount() {
        this.setUsers();
        this.setDevices();
        this.setCheckouts();
    }

    handleChangeTab = (value)=> {
        // tabs以下のイベントでも発火するので、値確認の上stateを更新する
        if ([TABS.USER, TABS.DEVICE, TABS.CHECKOUT].indexOf(value) >= 0) {
            this.setState({tab: value});
        }
    };

    onChangeSeatchText = (event) => {
        this.setState({searchQuery: event.target.value});
    };

    handleKeyPress = (event) => {
        if (event.which === KEYCODE_ENTER) {
            this.handleSearch();
        }
    };

    handleSearch = () => {
        switch (this.state.tab) {
            case TABS.USER:
                this.setUsers(this.state.searchQuery);
                break;
            case TABS.DEVICE:
                this.setDevices(this.state.searchQuery);
                break;
        }
    };

    setUsers = (query) => {
        ApiClient.getUsers(query)
            .then((body) => {
                this.setState({users: body.users})
            });
    };

    setDevices = (query) => {
        ApiClient.getDevices(query)
            .then((body) => {
                this.setState({devices: body.devices})
            });
    };

    setCheckouts = () => {
        ApiClient.getCheckout()
            .then((body) => {
                this.setState({checkouts: body.checkouts})
            });
    };

    getSearchComponent = () => {
        return (
            <div>
                <TextField
                    floatingLabelText="Search"
                    value={this.state.searchQuery}
                    onKeyPress={this.handleKeyPress}
                    onChange={this.onChangeSeatchText}/>
                <IconButton onClick={this.handleSearch}>
                    <SearchIcon/>
                </IconButton>
            </div>
        );
    };


    render() {
        return (
            <Tabs
                value={this.state.tab}
                onChange={this.handleChangeTab}>
                <Tab label="ユーザーリスト" value={TABS.USER}>
                    <div>
                        {this.getSearchComponent()}
                        <UserList users={this.state.users}/>
                    </div>
                </Tab>
                <Tab label="端末リスト" value={TABS.DEVICE}>
                    <div>
                        {this.getSearchComponent()}
                        <DeviceList devices={this.state.devices}/>
                    </div>
                </Tab>
                <Tab label="借り出し中端末リスト" value={TABS.CHECKOUT}>
                    <div>
                        <CheckoutList />
                    </div>
                </Tab>
            </Tabs>
        );
    }
}


RegisteredList.propTypes = {};

function mapStateToProps(state) {
    return {};
}

function mapDispatchToProps(dispatch) {
    return {};
}

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(RegisteredList);
