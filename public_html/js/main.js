require.config({
    urlArgs: "_=" + (new Date()).getTime(),
    baseUrl: "js",
    paths: {
        jquery: "lib/jquery",
        underscore: "lib/underscore",
        backbone: "lib/backbone",
        materialize: 'lib/materialize.min',
        hammerjs: 'lib/hammerjs'
    },
    shim: {
        'backbone': {
            deps: ['underscore', 'jquery'],
            exports: 'Backbone'
        },
        'underscore': {
            exports: '_'
        },
        'materialize': {
            deps: ['jquery', 'hammerjs']
        }
    }

});

define([
    'backbone',
    'router'
], function(
    Backbone,
    router
){
    Backbone.history.start();
});
