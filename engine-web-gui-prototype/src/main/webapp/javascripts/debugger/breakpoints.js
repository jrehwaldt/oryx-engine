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
    // register click handler for the svg bpmn elements (gateway, event, task)
    //
    $('g.me > g[id]', $('div.full-svg-artifact')).live('click', function(event) {
        var nodeContainer = $(this);
        var svgNodeId = nodeContainer.attr('id'); // sid-XXXX
        
        //
        // get the definition id
        //
        var el = nodeContainer;
        while (el.get(0).tagName != 'svg') {
            el = el.parent();
        }
        
        var svgDefinitionId = el.attr('id');
        var nodeFrame = nodeContainer.children('*[id=' + nodeId + 'bg_frame]');
        svgNodeClicked(svgNodeId, svgDefinitionId, nodeContainer, nodeFrame);
    });
    
    //
    // add the artifact data
    //
    var definitionTable = $('table#definitions-overview tbody');
    definitionTable.live('definition-table-entry:ready', function(event, definition, definitionId, definitionRow, tableBody) {
        
        $('td.extra-cell', definitionRow).append(
              '<a href="#" class="show-full-svg-artifact">'
                + '<img class="svg-artifact" src="/api/debugger/artifacts/' + definitionId + '/svg.svg?timestamp=' + new Date().getTime() + '" type="image/svg+xml" rel="#svg-artifact-full-overlay" />'
            + '</a>'
        );
        
        definitionRow.data('debugger-data', definition.attributes['extension-debugger-attribute']);
        definitionRow.data('definition', definition);
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
    
    //
    // register create-node-breakpoint submit handler
    //
    $("form#create-node-breakpoint").submit(function(event) {
        event.preventDefault();
        var form = this;
        
//        //
//        // create container data
//        //
//        var node = {};
//        node["@classifier"] = "org.jodaengine.process.structure.NodeImpl";
//        node["id"] = nodeId;
//        
//        var breakpoint = {};
//        breakpoint["@classifier"] = "org.jodaengine.ext.debugger.shared.BreakpointImpl";
//        breakpoint["node"] = node;
//        breakpoint["id"] = null;
//        breakpoint["state"] = $("input[name=breakpoint-activity-state]", form).val();
//        breakpoint["condition"] = $("input[name=breakpoint-condition]", form).val();
//        
//        if (breakpoint["condition"] == '') {
//            breakpoint["condition"] = null;
//        }
        
        var nodeId = $("input[name=breakpoint-node-id]", form).val();
        var definitionId = $("input[name=breakpoint-definition-id]", form).val();
        var condition = $("input[name=breakpoint-condition]", form).val();
        var activityState = $("select[name=breakpoint-activity-state]", form).val();
        
        //
        // create the breakpoint (post)
        //
        createNodeBreakpoint(definitionId, nodeId, activityState, condition, function(breakpoint) {
            form.reset();
            refreshBreakpoints();
        });
    });
});


/**
 * Shows a dialog, which provides the possibility to create a node breakpoint.
 * 
 * @param definitionId the definition, the breakpoint should be added to
 * @param nodeId the node, the breakpoint should be added to
 */
function showCreateNodeBreakpointForm(definitionId, nodeId) {
    var dialog = $('#create-node-breakpoint-dialog.dialog');
    var form = $('form#create-node-breakpoint', dialog);
    
    //
    // set the hidden fields
    //
    $("input[name=breakpoint-node-id]", form).val(nodeId);
    $("input[name=breakpoint-definition-id]", form).val(definitionId);
    
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
            text: 'Create node breakpoint',
            click: function() {
                $(this).dialog('close');
                form.submit();
            }
        }]
    });
};

/**
 * Click handler method for svg node elements.
 * 
 * @param svgNodeId the node's id (svg-based)
 * @param svgDefinitionId the definition's id (svg-based)
 * @param nodeContainer the node container
 * @param nodeFrame the node's graphical frame
 */
function svgNodeClicked(svgNodeId, svgDefinitionId, nodeContainer, nodeFrame) {
    nodeFrame.css('fill', 'red');
    var resolvedDefinitionId = resolveSvgDefinitionId(svgDefinitionId);
    var resolvedNodeId = resolveSvgNodeId(svgNodeId);
    showCreateNodeBreakpointForm(resolvedDefinitionId, resolvedNodeId);
}

/**
 * Refresh the breakpoint lists.
 */
function refreshBreakpoints() {
    alert('Refreshed.');
}

/**
 * Resolves the svg-based definition id to the original definition id.
 * 
 * @param svgDefinitionId the svg-based definition id
 * @return the original definition id
 */
function resolveSvgDefinitionId(svgDefinitionId) {
    return svgDefinitionId.replace('sid-', '');
}

/**
 * Resolves the svg-based node id to the original node id.
 * 
 * @param svgNodeId the svg-based node id
 * @return the original node id
 */
function resolveSvgNodeId(svgNodeId) {
    return svgNodeId.replace('sid-', '');
}