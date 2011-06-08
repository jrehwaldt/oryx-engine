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
    // add the artifact data
    //
    var definitionTable = $('table#definitions-overview tbody');
    definitionTable.live('definition-table-entry:ready', function(event, definition, definitionId, definitionRow, tableBody) {
        
        $('td.extra-cell', definitionRow).append(
              '<a href="#" class="show-full-svg-artifact">'
                + '<img class="svg-artifact" src="/api/debugger/artifacts/' + definitionId + '/svg.svg?timestamp=' + new Date().getTime() + '" type="image/svg+xml" rel="#svg-artifact-full-overlay" />'
            + '</a>'
            + '<br />'
            + '<a href="#" class="set-svg-artifact">Set svg artifact</a>'
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
        
        $('.extra-cell a.set-svg-artifact', tableBody).click(function(event) {
            event.preventDefault();
            var row = $(event.currentTarget).parent().parent();
            var definitionId = row.attr('definition-id');
            showSetSvgArtifactForm(definitionId);
        });
    });
});

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
