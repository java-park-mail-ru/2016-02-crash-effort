define([
    'backbone',
    'views/main',
    'views/game',
    'views/login',
    'views/scoreboard',
    'views/registration',
    'models/session'
], function(
    Backbone,
    mainView,
    gameView,
    loginView,
    scoreboardView,
    registrationView,
    session
){

    var Router = Backbone.Router.extend({
        routes: {
            '': 'mainAction',
            'scoreboard': 'scoreboardAction',
            'game': 'gameAction',
            'login': 'loginAction',
            'registration': 'registrationAction',
            '*default': 'defaultActions'
        },

        $page: $('#page'),

        mainAction: function () {
            this.$page.html(mainView.render().el);
        },
        scoreboardAction: function () {
            this.$page.html(scoreboardView.render().el);
        },
        gameAction: function () {

            var response = session.isLoggedIn();

            if (response === 'bad') {
                Backbone.history.navigate('login', { trigger: true });
            } else if (response === 'good') {
                this.$page.html(gameView.render().el);
            }
        },
        loginAction: function () {
            this.$page.html(loginView.render().el);
        },
        registrationAction: function () {
            this.$page.html(registrationView.render().el);
        },
        defaultActions: function() {
            // TODO: 404 or remove mainAction
            this.$page.html(mainView.render().el);
        }
    });

    return new Router();
});