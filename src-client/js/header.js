import React from 'react';

import {
    Link,
} from 'react-router';

import {
    Nav,
    Navbar,
    NavItem,
    NavDropdown,
    MenuItem
} from 'react-bootstrap';

export default class Header extends React.Component{
    render() {
        return (
            <Navbar>
                <Navbar.Header>
                    <Navbar.Brand>
                        <a href="#">Torica console</a>
                    </Navbar.Brand>
                </Navbar.Header>
                <Nav>
                    <NavItem eventKey={2} href="#"><Link to="/user">USER</Link></NavItem>
                    <NavItem eventKey={2} href="#"><Link to="/device">DEVICE</Link></NavItem>
                    <NavItem eventKey={2} href="#"><Link to="/checkout">CHECKOUT</Link></NavItem>
                    <NavItem eventKey={2} href="#"><Link to="/log">LOG</Link></NavItem>
                </Nav>
            </Navbar>
        );
    }
};