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
    
//    //
//    // enable continues refresh, if available
//    //
//    if ($(document).everyTime) {
//        $(document).everyTime(2500, function(i) {
//            loadArtifactsOverview();
//        }, 0); // 0 = unbound
//    }
    
    //
    // register the refresh overview handler
    //
    $('#definitions-overview-refresh').click(function(event) {
        event.preventDefault();
        loadArtifactsOverview();
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
                                + '<img class="svg-artifact" src="/api/debugger/artifacts/' + definitionId + '/svg.svg?timestamp=' + new Date().getTime() + '" width="300" height="100" type="image/svg.svg+xml" rel="#svg-artifact-full-overlay" />'
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
            
            $('img.svg-artifact[rel]', tableBody).overlay();
            
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
    $('#svg-artifact-full-overlay .full-svg-artifact').attr('src', '/api/debugger/artifacts/' + definitionId + '/svg.svg?timestamp=' + new Date().getTime()');
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
                        var img = $('tr[definition-id=' + definitionId + '] img.svg-artifact');
                        img.prop('src', '/api/debugger/artifacts/' + definitionId + '/svg.svg?timestamp=' + new Date().getTime());
                    });
                }
            }
        }]
    });
};