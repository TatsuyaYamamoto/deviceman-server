import request from 'superagent';
import constants from './constants.js';

module.exports = {
    getCheckout: function(callback){
        request
            .get("/api/checkout")
            .end(function(err, res) {
                if (err) {
                    throw err;
                }
                callback(res);
            })
    },
    getCheckoutLog: function(callback){
        request.get("/api/checkout/log")
            .end(function(err, res) {
                if (err) {
                    throw err;
                }
                callback(res);
            })
    },
    getUsers: function(callback){
        request.get("/api/users/")
            .end(function(err, res) {
                if (err) {
                    throw err;
                }
                callback(res);
            })
    },
    registerUser: function(id, name, address, password, callback){
        request
            .post("/api/users/")
            .send({
                id: id,
                name: name,
                address: address,
                password: password
            })
            .end(function(err, res) {
                if (err) {
                    throw err;
                }
                callback(res);
            })
    },
    getDevices: function(callback){
        request.get("/api/devices/")
            .end(function(err, res) {
                if (err) {
                    throw err;
                }
                callback(res);
            })
    },
    registerDevice: function(id, name, callback){
        request
            .post("/api/devices/")
            .send({
                id: id,
                name: name
            })
            .end(function(err, res) {
                if (err) {
                    throw err;
                }
                callback(res);
            })
    }
};