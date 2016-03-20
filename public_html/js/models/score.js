define([
    'backbone'
], function(
    Backbone
){

    var Score = Backbone.Model.extend({
        defaults: {
            name: '',
            score: 0
        }
    });

    return Score;
});