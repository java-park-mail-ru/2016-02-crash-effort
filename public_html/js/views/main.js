define([
    'backbone',
    'tmpl/main'
], function(
    Backbone,
    tmpl
){

    var View = Backbone.View.extend({
        template: tmpl,
        initialize: function () {

        },
        render: function () {
            this.$el.html(this.template());
            return this;
        }

    });

    return new View();
});