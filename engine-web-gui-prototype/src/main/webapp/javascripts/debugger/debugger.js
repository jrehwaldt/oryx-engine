//
// This script depends on
//   1) debugger-crud.js
//   2) breakpoint-crud.js
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
    loadArtifactsOverview();
    
    //
    // register the refresh overview handler
    //
    $('#definitions-overview-refresh').click(function(event) {
        event.preventDefault();
        loadArtifactsOverview();
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
function loadArtifactsOverview() {
    var tableBody = $('table#definitions-overview tbody');
    
    if (tableBody.length != 0) {
        loadProcessDefinitions(function(definitions) {
            tableBody.empty();
            $(definitions).each(function(index, definition) {
                var definitionId = idToString(definition.id);
                tableBody.append(
                    '<tr definition-id="' + definitionId + '">'
                        + '<td>' + definition.name + '</td>'
                        + '<td>' + definition.id.version + '</td>'
                        + '<td>' + definition.description + '</td>'
                        + '<td class="artifact-cell">'
                            + '<a href="#" class="show-full-svg-artifact">'
                                + '<img class="svg-artifact" src="/api/debugger/artifacts/' + definitionId + '/svg.svg?timestamp=' + new Date().getTime() + '" width="300" height="100" type="image/svg+xml" rel="#svg-artifact-full-overlay" />'
                            + '</a> '
                            + '<a href="#" class="set-svg-artifact">Set</a> '
                        + '</td>'
                    + '</tr>'
                );
            });
            
            $('.artifact-cell a.show-full-svg-artifact', tableBody).click(function(event) {
                event.preventDefault();
                var row = $(event.currentTarget).parent().parent();
                var definitionId = row.attr('definition-id');
                showFullSvg(definitionId);
            });
            
            if ($(document).overlay) {
                $('img.svg-artifact[rel]', tableBody).overlay();
            }
            
            $('.artifact-cell a.set-svg-artifact', tableBody).click(function(event) {
                event.preventDefault();
                var row = $(event.currentTarget).parent().parent();
                var definitionId = row.attr('definition-id');
                showSetSvgArtifactForm(definitionId);
            });
            
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
//    $('#svg-artifact-full-overlay .full-svg-artifact').prop('src', '/api/debugger/artifacts/' + definitionId + '/svg.svg?timestamp=' + new Date().getTime());
    
    getDebuggerSvgArtifact(definitionId, function(artifact, definitionId) {
        //
        // see
        //    http://stackoverflow.com/questions/156133/loading-xhtml-fragments-over-ajax-with-jquery
        // and
        //    http://stackoverflow.com/questions/3679114/jquery-append-dom
        // for an explanation, why adding the svg is so complicated.
        //
        $('#svg-artifact-full-overlay div.full-svg-artifact').html($(artifact).children().clone());
    });
    
};

/**
 * Shows a dialog, which provides the possibility to set a svg artifact.
 * 
 * @param definitionId the definition, the artifact should be set for
 */
function showSetSvgArtifactForm(definitionId) {
    var dialog = $('#set-svg-artifact-dialog.dialog');
    var form = $('form#set-svg-artifact', dialog);
    
    //
    // show the dialog
    //
    dialog.dialog({
        width: 400,
        modal: true,
        buttons: [{
            text: 'Cancel',
            click: function() {
                $(this).dialog('close');
            }
        }, {
            text: 'Set svg artifact',
            click: function() {
                $(this).dialog('close');
                form.prop('action', '/api/debugger/artifacts/' + definitionId + '/svg');
                form.submit();
                
                //
                // force svg image reload
                //
                if ($(document).oneTime) {
                    $(document).oneTime(100, function() {
                        var overlay = $('tr[definition-id=' + definitionId + '] .svg-artifact');
                        overlay.prop('src', '/api/debugger/artifacts/' + definitionId + '/svg.svg?timestamp=' + new Date().getTime());
                    });
                }
            }
        }]
    });
};

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