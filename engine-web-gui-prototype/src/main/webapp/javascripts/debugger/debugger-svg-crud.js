//====================================================================
//================================ CRUD Debugger =====================
//====================================================================

/**
 * Sets a svg artifact for a certain process definition.
 * 
 * @param definitionId the definition's id, which should be started
 * @param svgArtifact the svg artifact to set
 * @param successHandler the anonymous function to call, gets the svgArtifact and definitionId as #1 and #2 parameter
 */
function setDebuggerSvgArtifact(definitionId, svgArtifact, successHandler) {
    $.ajax({
        type: 'POST',
        url: '/api/debugger/artifacts/' + definitionId + '/svg',
        success: function() {
            if (successHandler)
                successHandler.apply(null, [svgArtifact, definitionId]);
        }
    });
};

/**
 * Gets a svg artifact for a certain process definition.
 * 
 * @param definitionId the definition's id, which should be started
 * @param successHandler the anonymous function to call, gets the svgXmlArtifact and definitionId as #1 and #2 parameter
 */
function getDebuggerSvgArtifact(definitionId, successHandler) {
    $.ajax({
        type: 'GET',
        url: '/api/debugger/artifacts/' + definitionId + '/svg',
        success: function(svgXmlArtifact) {
            if (successHandler)
                successHandler.apply(null, [svgXmlArtifact, definitionId]);
        },
        dataType: "xml" // we expect a svg xml
    });
};
