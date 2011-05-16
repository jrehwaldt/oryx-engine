//====================================================================
//================================ CRUD Navigator ====================
//====================================================================
//
// this is not exactly crud, but who cares... 
//
/**
 * Loads the running process instances and calls the provided function.
 * 
 * @param successHandler the anonymous function to call, gets the running instances as #1 parameter
 */
function loadRunningProcessInstances(successHandler) {
    $.ajax({
        type: 'GET',
        url: '/api/navigator/status/running-instances',
        success: function(runningInstances) {
            if (successHandler)
                successHandler.apply(null, [runningInstances]);
        },
        dataType: "json" // we expect json
    });
};

/**
 * Loads the finished process instances and calls the provided function.
 * 
 * @param successHandler the anonymous function to call, gets the finished instances as #1 parameter
 */
function loadFinishedProcessInstances(successHandler) {
    $.ajax({
        type: 'GET',
        url: '/api/navigator/status/finished-instances',
        success: function(finishedInstances) {
            if (successHandler)
                successHandler.apply(null, [finishedInstances]);
        },
        dataType: "json" // we expect json
    });
};

/**
 * Loads the process instances and calls the provided function.
 * 
 * @param definitionId the definition's id, which should be started
 * @param successHandler the anonymous function to call, gets the instanceId and definitionId as #1 and #2 parameter
 */
function startProcessInstance(definitionId, successHandler) {
    $.ajax({
        type: 'POST',
        url: '/api/navigator/process-definitions/' + definitionId + '/start',
        success: function(instanceId) {
            if (successHandler)
                successHandler.apply(null, [instanceId, definitionId]);
        }
    });
};

/**
 * Loads the navigator state (idle?) and calls the provided function.
 * 
 * @param successHandler the anonymous function to call, gets the idle state as #1 parameter
 */
function loadIsNavigatorIdle(successHandler) {
    $.ajax({
        type: 'GET',
        url: '/api/navigator/status/is-idle',
        success: function(isIdle) {
            if (successHandler)
                successHandler.apply(null, [isIdle]);
        }
    });
};

/**
 * Loads the navigator statistic and calls the provided function.
 * 
 * @param successHandler the anonymous function to call, gets the statistic as #1 parameter
 */
function loadNavigatorStatistic(successHandler) {
    $.ajax({
        type: 'GET',
        url: '/api/navigator/status/statistic',
        success: function(statistic) {
            if (successHandler)
                successHandler.apply(null, [statistic]);
        },
        dataType: "json" // we expect json
    });
};