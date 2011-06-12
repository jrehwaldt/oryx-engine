//====================================================================
//================================ CRUD Debugger =====================
//====================================================================
//
// this is not exactly crud, but who cares... 
//
/**
* Fetches all breakpoints and calls the provided function.
* 
* @param successHandler the anonymous function to call, gets the breakpoints as #1 parameter
*/
function loadAllBreakpoints(successHandler) {
    $.ajax({
        type: 'GET',
        url: '/api/debugger/breakpoints',
        success: function(breakpoints) {
            if (successHandler)
                successHandler.apply(null, [breakpoints]);
        },
        dataType: 'json'
    });
};

/**
* Fetches all breakpoints for a certain instance and calls the provided function.
* 
* @param instanceId only the breakpoints for this instance are fetched
* @param successHandler the anonymous function to call, gets the breakpoints and instanceId as #1 and #2 parameter
*/
function loadInstanceBreakpoints(instanceId, successHandler) {
    $.ajax({
        type: 'GET',
        url: '/api/debugger/instances/' + instanceId + '/breakpoints',
        success: function(breakpoints) {
            if (successHandler)
                successHandler.apply(null, [breakpoints, instanceId]);
        },
        dataType: 'json'
    });
};

/**
* Fetches all breakpoints for a certain definition and calls the provided function.
* 
* @param definitionId only the breakpoints for this definition are fetched
* @param successHandler the anonymous function to call, gets the breakpoints and definitionId as #1 and #2 parameter
*/
function loadDefinitionBreakpoints(definitionId, successHandler) {
    $.ajax({
        type: 'GET',
        url: '/api/debugger/definitions/' + definitionId + '/breakpoints',
        success: function(breakpoints) {
            if (successHandler)
                successHandler.apply(null, [breakpoints, definitionId]);
        },
        dataType: 'json'
    });
};

/**
* Creates a new node breakpoint.
* 
* @param processDefinitionId the definition id, which the node belongs to
* @param activityState the state, which will trigger this breakpoint
* @param juelCondition a condition, which will be evaluated before the breakpoint triggers
* @param successHandler the anonymous function to call, gets the created breakpoint as #1 parameter
*/
function createNodeBreakpoint(processDefinitionId, nodeId, activityState, juelCondition, successHandler) {
    
    var queryObject = {
        definitionId: processDefinitionId,
        nodeId: nodeId,
        activityState: activityState,
        juelCondition: juelCondition
    };
    
    $.ajax({
        type: 'POST',
        url: '/api/debugger/breakpoints/create?' + jQuery.param(queryObject),
        success: function(breakpoint) {
            if (successHandler)
                successHandler.apply(null, [breakpoint]);
        },
        data: null
    });
}