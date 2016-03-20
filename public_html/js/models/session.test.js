define(function (require) {
    QUnit.module("models/session");

    QUnit.test('Check login validation', function(assert) {
        var session = require('models/session');

        var empty = session.validateLogin('', '');
        var onlyUsername = session.validateLogin('12', '');
        var onlyPassword = session.validateLogin('', '12');
        var allData = session.validateLogin('12', '12');

        assert.equal( empty, false );
        assert.equal( onlyUsername, false );
        assert.equal( onlyPassword, false );
        assert.equal( allData, true );
    });

    QUnit.test('Check registration validation', function(assert) {
        var session = require('models/session');

        var empty = session.validateRegistration('', '', '', '');
        var onlyEmail = session.validateRegistration('12@mail.ru', '', '', '');
        var badEmail1 = session.validateRegistration('12mail.ru', '1', '1', '1');
        var badEmail2 = session.validateRegistration('12@.ru', '1', '1', '1');
        var badEmail3 = session.validateRegistration('12@mail', '1', '1', '1');
        var badEmail4 = session.validateRegistration('@mail.ru', '1', '1', '1');
        var badEmail5 = session.validateRegistration('mail.ru', '1', '1', '1');
        var onlyUsername = session.validateRegistration('', '12', '', '');
        var onlyFirstPassword = session.validateRegistration('', '', '12', '');
        var onlySecondPassword = session.validateRegistration('', '', '', '12');
        var email_Username = session.validateRegistration('12@mail.ru', '12', '', '');
        var email_Username_firstPassword = session.validateRegistration('12@mail.ru', '12', '12', '');
        var email_Username_SecondPassword = session.validateRegistration('12@mail.ru', '12', '', '12');
        var differentPasswords = session.validateRegistration('12@mail.ru', '12', '1', '12');
        var allData = session.validateRegistration('12@mail.ru', '12', '12', '12');

        assert.equal( empty, 'all' );
        assert.equal( onlyEmail, 'all' );
        assert.equal( badEmail1, 'bad_email' );
        assert.equal( badEmail2, 'bad_email' );
        assert.equal( badEmail3, 'bad_email' );
        assert.equal( badEmail4, 'bad_email' );
        assert.equal( badEmail5, 'bad_email' );
        assert.equal( onlyUsername, 'all' );
        assert.equal( onlyFirstPassword, 'all' );
        assert.equal( onlySecondPassword, 'all' );
        assert.equal( email_Username, 'all' );
        assert.equal( email_Username_firstPassword, 'all' );
        assert.equal( email_Username_SecondPassword, 'all' );
        assert.equal( differentPasswords, 'passwords' );
        assert.equal( allData, 'None' );
    })
});