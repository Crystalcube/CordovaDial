var exec = require('cordova/exec');
var myFunc = {};

myFunc.dial = function (arg0, success, error) {
    exec(success, error, 'CordovaDial', 'dial', [arg0]);
};

modules.exports = myFunc;
