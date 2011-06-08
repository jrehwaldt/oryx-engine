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

/**
* Creates a new node breakpoint.
* 
* @param breakpoint the breakpoint, which should be created
* @param successHandler the anonymous function to call, gets the created breakpoint as #1 parameter
*/
function createNodeBreakpoint(processDefinitionId, nodeId, activityState, juelCondition, successHandler) {
    
    $.ajax({
        type: 'POST',
        url: '/api/debugger/breakpoints/create?definitionId=' + processDefinitionId
                                           + '&nodeId=' + nodeId
                                           + '&activityState=' + activityState
                                           + '&juelCondition=' + juelCondition,
        success: function(breakpoint) {
            if (successHandler)
                successHandler.apply(null, [breakpoint]);
        },
        data: null
    });
}