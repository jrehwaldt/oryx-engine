//
// This script depends on
//   1) debugger-crud.js
//

$().ready(function() {
    //
    // show the debugger menu, if the service is available
    //
    activateDebuggerMenuIfDebuggerAvailable();
});


/**
 * Activates the debugger menu, in case the DebuggerService is available.
 */
function activateDebuggerMenuIfDebuggerAvailable() {
    getDebuggerServiceIsReady(function(runningStatus) {
        if (runningStatus) {
            $('#menu-level-1 li.debugger').toggle();
        }
    });
};
