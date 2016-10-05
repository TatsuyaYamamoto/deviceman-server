import request from 'superagent';
import constants from './constants.js';

module.exports = {
    getCheckout: function(){
        return new Promise((resolve, reject) => {
            request
                .get("/torica/api/checkout")
                .end(function(err, res) {
                    if (err) {
                        reject(err);
                    }else{
                        resolve(res.body);
                    }
                })
        })
    },
    getCheckoutLog: function(){
        return new Promise((resolve, reject) => {
            request
                .get("/torica/api/checkout/log")
                .end(function(err, res) {
                    if (err) {
                        reject(err);
                    }else{
                        resolve(res.body);
                    }
                })
        })
    },
    getUsers: function(callback){
        return new Promise((resolve, reject) => {
                request
                    .get("/torica/api/users/")
                    .end(function(err, res) {
                        if (err) {
                            reject(err);
                        }else{
                            resolve(res.body);
                        }
                    })
        })
    },
    registerUser: function(id, name, address, password, callback){
        return new Promise(
            (resolve, reject) => {
                request
                    .post("/torica/api/users/")
                    .send({
                        id: id,
                        name: name,
                        address: address,
                        password: password
                    })
                    .end(
                        (err, res) => {
                            if (err) {
                                reject(err.response);
                            }else{
                                resolve(res.body);
                            }
                        }
                    );
            }
        );
    },
    getDevices: function(){
        return new Promise(
            (resolve, reject) => {
                request
                    .get("/torica/api/devices/")
                    .end(
                        (err, res) => {
                            if (err) {
                                throw err;
                            }else{
                                resolve(res.body);
                            }
                        })

            })
    },
    registerDevice: function(id, name){
        return new Promise(
            (resolve, reject) => {
                request
                    .post("/torica/api/devices/")
                    .send({
                        id: id,
                        name: name
                    })
                    .end(
                        (err, res) => {
                            if (err) {
                                reject(err);
                            } else {
                                resolve(res.body);
                            }
                        }
                    );
            }
        );
    },
    uploadCsvDevices: function(formData){
        return new Promise(
            (resolve, reject) => {
                request
                    .post("/torica/api/devices/upload.csv")
                    .attach('csv_file', formData)
                    .end(
                        (err, res) => {
                            if (err) {
                                reject(err);
                            } else {
                                resolve();
                            }
                        }
                    );
            }
        );
    }
};