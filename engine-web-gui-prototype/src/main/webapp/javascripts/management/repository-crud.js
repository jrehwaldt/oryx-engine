//====================================================================
//================================ CRUD Repository ===================
//====================================================================
//
// this is not exactly crud, but who cares... 
//
/**
 * Loads the process definitions and calls the provided function.
 * 
 * @param successHandler the anonymous function to call, gets the definitions as #1 parameter
 */
function loadProcessDefinitions(successHandler) {
    $.ajax({
        type: 'GET',
        url: '/api/repository/process-definitions',
        success: function(definitions) {
            if (successHandler)
                successHandler.apply(null, [definitions]);
        },
        dataType: "json" // we expect json
    });
};

function activateProcessDefinition(definitionId, successHandler) {
    $.ajax({
        type: 'POST',
        url: '/api/repository/process-definitions/' + definitionId + '/activate',
        success: function(definitions) {
            if (successHandler)
                successHandler.apply(null, [definitions]);
        }
    });
};