//====================================================================
//================================ CRUD Debugger =====================
//====================================================================
//
// this is not exactly crud, but who cares... 
//
/**
* Fetches all interrupted instances and calls the provided function.
* 
* @param successHandler the anonymous function to call, gets the interrupted instances as #1 parameter
*/
function loadInterruptedInstances(successHandler) {
    $.ajax({
        type: 'GET',
        url: '/api/debugger/interrupted-instances',
        success: function(interruptedInstances) {
            if (successHandler)
                successHandler.apply(null, [interruptedInstances]);
        },
        dataType: 'json'
    });
};

/**
* Terminates an interrupted instance.
* 
* @param interruptedInstanceId the id of the interrupted instance object
* @param successHandler the anonymous function to call, gets the id of the terminates instance as #1 parameter
*/
function terminateInterruptedInstance(interruptedInstanceId, successHandler) {
    
    _controlInterruptedInstance(interruptedInstanceId, 'terminate', successHandler);
};

/**
* Steps over an interrupted instance.
* 
* @param interruptedInstanceId the id of the interrupted instance object
* @param successHandler the anonymous function to call, gets the id of the stepped over instance as #1 parameter
*/
function stepOverInterruptedInstance(interruptedInstanceId, successHandler) {
    
    _controlInterruptedInstance(interruptedInstanceId, 'step-over', successHandler);
};

/**
* Resumes an interrupted instance.
* 
* @param interruptedInstanceId the id of the interrupted instance object
* @param successHandler the anonymous function to call, gets the id of the resumed instance as #1 parameter
*/
function resumeInterruptedInstance(interruptedInstanceId, successHandler) {
    
    _controlInterruptedInstance(interruptedInstanceId, 'resume', successHandler);
};

/**
* Continues an interrupted instance.
* 
* @param interruptedInstanceId the id of the interrupted instance object
* @param successHandler the anonymous function to call, gets the id of the continued instance as #1 parameter
*/
function continueInterruptedInstance(interruptedInstanceId, successHandler) {
    
    _controlInterruptedInstance(interruptedInstanceId, 'continue', successHandler);
};



/**
* Controls an interrupted instance. Use the explicit helper methods instead.
* 
* @param interruptedInstanceId the id of the interrupted instance object
* @param debugCommand the debug command ('terminate', 'step-over', 'resume', 'continue')
* @param successHandler the anonymous function to call, gets the id of the terminates instance as #1 parameter
*/
function _controlInterruptedInstance(interruptedInstanceId, debugCommand, successHandler) {
    
    $.ajax({
        type: 'POST',
        url: '/api/debugger/interrupted-instances/' + interruptedInstanceId + '/' + debugCommand,
        success: function() {
            if (successHandler)
                successHandler.apply(null, [interruptedInstanceId]);
        },
        data: null
    });
};