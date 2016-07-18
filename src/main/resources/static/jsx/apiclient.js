var request = window.superagent;

var apiclient = {
    getCheckout: function(callback){
        request.get("/api/checkout", function(err, res) {
            if (err) {
                throw err;
            }
            callback(res);
        })
    },
    getCheckoutLog: function(callback){
        request.get("/api/checkout/log", function(err, res) {
            if (err) {
                throw err;
            }
            callback(res);
        })
    },
    getUsers: function(callback){
        request.get("/api/users/", function(err, res) {
            if (err) {
                throw err;
            }
            callback(res);
        })
    },
    getDevices: function(callback){
        request.get("/api/devices/", function(err, res) {
            if (err) {
                throw err;
            }
            callback(res);
        })
    }

};