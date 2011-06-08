//====================================================================
//================================ CRUD Debugger =====================
//====================================================================
//
// this is not exactly crud, but who cares... 
//
/**
* Fetches all breakpoints and calls the provided function.
* 
* @param successHandler the anonymous function to call, gets the breakpoints and instanceId as #1 and #2 parameter
* @param instanceId can be null, if provided only the breakpoints for this instance are fetched
*/
function loadBreakpoints(successHandler, instanceId) {
    $.ajax({
        type: 'GET',
        url: '/api/debugger/breakpoints' + (instanceId ? '/' + instanceId : ''),
        success: function(breakpoints) {
            if (successHandler)
                successHandler.apply(null, [breakpoints, instanceId]);
        },
        dataType: 'json'
    });
};