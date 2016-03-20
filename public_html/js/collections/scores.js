define([
    'backbone',
    'models/score'
], function(
    Backbone,
    Score
){

    var ScoresCollection = Backbone.Collection.extend({
        model: Score,
        comparator: function( collection ) {
            return ( -collection.get('score') );
        }
    });

    //return ScoresCollection;

    var allScores = new ScoresCollection([
                {
                    name: 'good boy',
                    score: 26
                },
                {
                    name: 'bab bad not good',
                    score: 3
                },
                {
                    name: 'lala',
                    score: 1
                },
                {
                    name: 'kot',
                    score: 55
                },
                {
                    name: 'pes',
                    score: 66
                },
                {
                    name: 'bad boy',
                    score: 19
                },
                {
                    name: 'Jesus',
                    score: 998
                },
                {
                    name: 'Xbox',
                    score: 0
                },
                {
                    name: 'Dante',
                    score: 12
                },
                {
                    name: 'Admin Priveta',
                    score: 999
                }
            ]);

    return allScores;
});