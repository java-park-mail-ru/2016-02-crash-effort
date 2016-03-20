define([
    'backbone',
    'tmpl/game'
], function(
    Backbone,
    tmpl
){

    var View = Backbone.View.extend({
        events: {
            "click .js-go-back":   "goBack"
        },

        template: tmpl,
        initialize: function () {

        },
        render: function () {
            this.$el.html(this.template());
            this.delegateEvents();
            return this;
        },
        goBack: function() {
            Backbone.history.navigate('', { trigger: true });
        }
    });

    return new View();
});