//====================================================================
//================================ CRUD Debugger =====================
//====================================================================
//
// this is not exactly crud, but who cares... 
//
/**
 * Requests whether the DebuggerService is ready.
 * 
 * @param successHandler the anonymous function to call, gets the running status as #1 parameter
 */
function getDebuggerServiceIsReady(successHandler) {
    $.ajax({
        type: 'GET',
        url: '/api/debugger/status/is-running',
        success: function(runningStatus) {
            if (successHandler)
                successHandler.apply(null, [runningStatus]);
        },
        dataType: "json" // we expect json
    });
};
