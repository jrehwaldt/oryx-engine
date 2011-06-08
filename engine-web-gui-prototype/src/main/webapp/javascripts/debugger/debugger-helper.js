//
// This script depends on
//   1) debugger-crud.js
//   
//   5) repository-crud.js
//   6) jquery.timers-1.2.js (optional)
//

$().ready(function() {
    
    // 
    // refresh the artifacts table
    // 
    loadProcessDefinitionsOverview();
    
    //
    // register the refresh overview handler
    //
    $('#definitions-overview-refresh').click(function(event) {
        event.preventDefault();
        loadProcessDefinitionsOverview();
    });
    
});

/**
 * Creates the id-param from a given JSON definitionID.
 * 
 * @param definitionID the id json object
 * @returns {String}
 */
function idToString(definitionID) {
    return definitionID.identifier + ':' + definitionID.version;
}

/**
 * Loads the running process instances table and clear any old entries.
 */
function loadProcessDefinitionsOverview() {
    var tableBody = $('table#definitions-overview tbody');
    
    if (tableBody.length != 0) {
        loadProcessDefinitions(function(definitions) {
            tableBody.empty();
            $(definitions).each(function(index, definition) {
                var definitionId = idToString(definition.id);
                var definitionRow = $(
                    '<tr definition-id="' + definitionId + '">'
                        + '<td>' + definition.name + '</td>'
                        + '<td>' + definition.id.version + '</td>'
                        + '<td>' + definition.description + '</td>'
                        + '<td class="extra-cell">'
                        + '</td>'
                    + '</tr>'
                );
                tableBody.append(definitionRow);
                tableBody.trigger('definition-table-entry:ready', [definition, definitionId, $(definitionRow), tableBody]);
            });
            
            tableBody.trigger('definition-table:ready', [tableBody]);
            tableBody.parent().find('th.loading-data').removeClass('loading-data');
        });
    }
};

/**
 * Shows the svg on full page.
 * 
 * @param definitionId the definition to show the svg for
 */
function showFullSvg(definitionId) {
    
    getDebuggerSvgArtifact(definitionId, function(artifact, definitionId) {
        //
        // see
        //    http://stackoverflow.com/questions/156133/loading-xhtml-fragments-over-ajax-with-jquery
        // and
        //    http://stackoverflow.com/questions/3679114/jquery-append-dom
        // for an explanation, why adding the svg is so complicated.
        //
        // .children() for FF
        // .clone() for Chrome
        //
        $('#svg-artifact-full-overlay div.full-svg-artifact').html($(artifact).children().clone());
    });
};
