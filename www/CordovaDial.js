var exec = require('cordova/exec');

exports.dial = function (arg0, success, error) {
    exec(success, error, 'CordovaDial', 'dial', [arg0]);
};
