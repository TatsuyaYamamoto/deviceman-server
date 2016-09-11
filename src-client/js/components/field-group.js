import React from 'react';
import {
    FormGroup,
    ControlLabel,
    FormControl,
    HelpBlock
} from 'react-bootstrap';

module.exports = function(props){
    return (
        <FormGroup controlId={props.id}>
            <ControlLabel>{props.label}</ControlLabel>
            <FormControl
                type ={props.type}
                placeholder ={props.placeholder}
                value={props.value}
                onChange={props.onChange}
            />
        </FormGroup>
    );
};