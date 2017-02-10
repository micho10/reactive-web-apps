/*
 * Wraps the entire configuration in a function to avoid polluting the global JavaScript namespace
 */
(function (requirejs) {
    'use strict';
    // Configures RequireJS
    requirejs.config({
        shim: {
            // Tells RequireJS about jsRoutes, which is generated on-the-fly in main.scala.html,
            // by telling it the name of the var that defines it.
            'jsRoutes': {
                deps: [],
                exports: 'jsRoutes'
            }
        },
        paths: {
            // Configures the path of the jQuery dependency; this is the resulting path for WebJar dependencies
            'jquery': ['../lib/jquery/jquery']
        }
    });

    // Configures an error handler in order to be made aware of problems
    requirejs.onError = function (err) {
        console.log(err);
    };

    // Uses RequireJS to depend on the jQuery dependency, and executes our initial code
    require(['jquery'], function ($) {
        $(document).ready(function () {
            $('#button').on('click', function () {
                jsRoutes.controllers.Application.text().ajax({
                    success: function (text) {
                        $('#text').text(text);
                    },
                    error: function () {
                        alert('Uh, oh');
                    }
                });
            });
        });
    });
})(requirejs);
