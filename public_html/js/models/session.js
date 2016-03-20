define([
    'backbone'
], function (Backbone) {

    var SessionModel = Backbone.Model.extend({
        urlLogin: '/api/session/',
        urlRegistration: '/api/user/',

        login: function (username, password) {
            var ajaxResult = {
                'success': 'good',
                'error': 'bad'
            };
            var requestResult;
            $.ajax({
                type: 'PUT',
                async: false,
                url: this.urlLogin,
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify({
                    login: username,
                    password: password
                }),
                success: function (data) {
                    requestResult = ajaxResult['success']
                },
                error: function (xhr, str) {
                    requestResult = ajaxResult['error']
                }

            });
            return requestResult;
        },

        isLoggedIn: function () {
            var ajaxResult = {
                'success': 'good',
                'error': 'bad'
            };
            var requestResult;
            $.ajax({
                type: 'GET',
                async: false,
                url: this.urlLogin,
                success: function (data) {
                    requestResult = ajaxResult['success']
                },
                error: function (xhr, str) {
                    requestResult = ajaxResult['error']
                }

            });
            return requestResult;
        },

        logout: function () {
            $.ajax({
                type: 'DELETE',
                url: this.urlLogin,
                success: function () {
                    //    TODO
                },
                error: function () {
                    //    TODO
                }

            });
        },

        registration: function (username, password, email) {
            var ajaxResult = {
                'success': 'good',
                'error': 'bad'
            };
            var requestResult;
            $.ajax({
                type: 'PUT',
                url: this.urlRegistration,
                async: false,
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify({
                    login: username,
                    password: password,
                    email: email
                }),
                success: function (data) {
                    requestResult = ajaxResult['success']
                },
                error: function (xhr, str) {
                    requestResult = ajaxResult['error']
                }

            });
            return requestResult;
        },

        validateLogin: function (username, password) {
            if (username && password) {
            } else {
                return false
            }
            return true;
        },

        validateRegistration: function (email, username, password1, password2) {
            var errors = {
                'all': 'all',
                'passwords': 'passwords',
                'email_validation': 'bad_email',
                'None': 'None'
            };
            var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            if (!(email && username && password1 && password2)) {
                return errors['all']
            } else if (!(password1 === password2)) {
                return errors['passwords']
            } else if (!re.test(email)) {
                return errors['email_validation'];
            }
            return errors['None'];
        }

    });


    return new SessionModel();

});