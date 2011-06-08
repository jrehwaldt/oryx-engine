//
// This script depends on
//   1) debugger-helper.js
//   2) debugger-crud.js
//   3) debugger-svg-crud.js
//   4) jquery.tools.js (Overlay)
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
    
    //
    // register click handler for the svg bpmn elements (gateway, event, task)
    //
    $('g.me > g[id]', $('div.full-svg-artifact')).live('click', function(event) {
        var elementContainer = $(this);
        var elementId = elementContainer.attr('id'); // sid-XXXX
        var elementFrame = elementContainer.children('*[id=' + elementId + 'bg_frame]');
        svgNodeClicked(elementContainer, elementId, elementFrame);
    });
    
    //
    // add the artifact data
    //
    var definitionTable = $('table#definitions-overview tbody');
    definitionTable.live('definition-table-entry:ready', function(event, definitionId, definitionRow, tableBody) {
        
        $('td.extra-cell', definitionRow).append(
              '<a href="#" class="show-full-svg-artifact">'
                + '<img class="svg-artifact" src="/api/debugger/artifacts/' + definitionId + '/svg.svg?timestamp=' + new Date().getTime() + '" width="300" height="100" type="image/svg+xml" rel="#svg-artifact-full-overlay" />'
            + '</a>'
        );
    });
    
    //
    // add the control functions when the table is loaded
    //
    definitionTable.live('definition-table:ready', function(event, tableBody) {
        
        $('.extra-cell a.show-full-svg-artifact', tableBody).click(function(event) {
            event.preventDefault();
            var row = $(event.currentTarget).parent().parent();
            var definitionId = row.attr('definition-id');
            showFullSvg(definitionId);
        });
        
        if ($(document).overlay) {
            $('img.svg-artifact[rel]', tableBody).overlay();
        }
    });
});

/**
 * Click handler method for svg node elements.
 * 
 * @param elementContainer the node container
 * @param elementId the node's id
 * @param elementFrame the node's graphical frame
 */
function svgNodeClicked(elementContainer, elementId, elementFrame) {
    elementFrame.css('fill', 'red');
}