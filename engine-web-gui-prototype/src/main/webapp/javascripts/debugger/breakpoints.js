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
        
        //
        // ignore those nasty comments!
        //
        if ($(this).filter(':has(defs)').length == 0) {
            return;
        }
        
        var svgNodeId = nodeContainer.parent().parent().parent().attr('id').replace('svg-', ''); // svg-sid-XXXX
        
        //
        // get the definition id
        //
        var el = nodeContainer;
        while (el.get(0).tagName != 'svg') {
            el = el.parent();
        }
        
        var svgDefinitionId = el.attr('id');
        var nodeFrame = nodeContainer.children('*[id=' + nodeContainer.attr('id') + 'bg_frame]');
        svgNodeClicked(svgNodeId, svgDefinitionId, nodeContainer, nodeFrame);
    });
    
    //
    // add the artifact data
    //
    var definitionTable = $('table#definitions-overview tbody');
    definitionTable.live('definition-table-entry:ready', function(event, definition, definitionId, definitionRow, tableBody) {
        
        //
        // create a breakpoints row
        //    and
        // add the svg artifact and an anchor for breakpoint operations
        //
        definitionRow.append(
                '<td class="artifact-cell">'
                + '<a href="#" class="show-full-svg-artifact">'
                    + '<img class="svg-artifact" src="/api/debugger/artifacts/' + definitionId + '/svg.svg?timestamp=' + new Date().getTime() + '" type="image/svg+xml" rel="#svg-artifact-full-overlay" />'
                + '</a>'
            + '</td>'
            + '<td class="breakpoint-cell">'
                + 'Loading breakpoint data'
            + '</td>'
        );
        
        //
        // load the breakpoint list for this definition
        //
        refreshDefinitionBreakpoints(definitionId);
        
        //
        // store the svg id with the definition id
        //
        if (definition.attributes['idXml']) {
            definitionRow.attr('definition-svg-id', definition.attributes['idXml'].toLowerCase());
        }
        
        //
        // store the definition and debugger data
        //
        definitionRow.data('debugger-data', definition.attributes['extension-debugger-attribute']);
        definitionRow.data('definition', definition);
    });
    
    //
    // add the control functions when the table is loaded
    //
    definitionTable.live('definition-table:ready', function(event, tableBody) {
        
        $('.artifact-cell a.show-full-svg-artifact', tableBody).click(function(event) {
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
            refreshDefinitionBreakpoints(definitionId);
        });
    });
});


/**
 * Shows a dialog, which provides the possibility to create a node breakpoint.
 * 
 * @param definitionId the definition, the breakpoint should be added to
 * @param nodeId the node, the breakpoint should be added to
 */
function showCreateNodeBreakpointForm(definitionId, nodeId, nodeFrame) {
    
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
                nodeFrame.attr('fill', nodeFrame.prop('previous-fill'));
            }
        }, {
            text: 'Create node breakpoint',
            click: function() {
                $(this).dialog('close');
                form.submit();
                nodeFrame.attr('fill', nodeFrame.prop('previous-fill'));
            }
        }],
        close: function(event, ui) { 
            nodeFrame.attr('fill', nodeFrame.prop('previous-fill'));
        }
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
    nodeFrame.prop('previous-fill', nodeFrame.attr('fill'));
    nodeFrame.attr('fill', 'red');
    var resolvedDefinitionId = resolveSvgDefinitionId(svgDefinitionId);
    var resolvedNodeId = resolveSvgNodeId(resolvedDefinitionId, svgNodeId);
    showCreateNodeBreakpointForm(resolvedDefinitionId, resolvedNodeId, nodeFrame);
}

/**
 * Refresh the breakpoint lists for a certain definition.
 * 
 * @param definitionId the definition to be refreshed
 */
function refreshDefinitionBreakpoints(definitionId) {
    var breakpointsCell = $('tr[definition-id="' + definitionId + '"] td.breakpoint-cell');
    breakpointsCell.addClass('loading-data');
    
    loadDefinitionBreakpoints(definitionId, function(breakpoints) {
        
        var breakpointData = null;
        
        //
        // no breakpoints available
        //
        if (breakpoints.length == 0) {
            breakpointData = 'No breakpoints available';
            
        //
        // breakpoints found
        //
        } else {
            
            breakpointData = '<table width="100%">';
            breakpointData +=
                '<thead>'
                    + '<tr>'
                        + '<th>Breakpoint-ID</th>'
                        + '<th>Activating Node</th>'
                        + '<th>Activating State</th>'
                        + '<th>Condition</th>'
                    + '</tr>'
                + '</thead>';
            
            $(breakpoints).each(function(index, breakpoint) {
                breakpointData += 
                    '<tr>'
                        + '<td>' + breakpoint.id + '</td>'
                        + '<td>' + breakpoint.node.id + '</td>'
                        + '<td>' + breakpoint.state + '</td>'
                        + '<td>' + breakpoint.condition.expression + '</td>'
                    + '</tr>';
            });
            
            breakpointData += '</table>'
        }
        
        breakpointData = breakpointsCell.html(breakpointData);
        breakpointsCell.removeClass('loading-data');
    });
}

/**
 * Resolves the svg-based definition id to the original definition id.
 * 
 * @param svgDefinitionId the svg-based definition id
 * @return the original definition id
 */
function resolveSvgDefinitionId(svgDefinitionId) {
    
    var definitionId = $('#svg-artifact-full-overlay div.full-svg-artifact').filter(':has(svg[id="' + svgDefinitionId + '"])').attr('definition-id');
    return definitionId;
}

/**
 * Resolves the svg-based node id to the original node id.
 * 
 * @param definitionId the original definition id
 * @param svgNodeId the svg-based node id
 * @return the original node id
 */
function resolveSvgNodeId(definitionId, svgNodeId) {
    
    var definition = $('tr[definition-id="' + definitionId + '"]').data('definition');
    svgNodeId = svgNodeId.toLowerCase();
    
    //
    // resolve all start node's graphs
    //
    var nodeId = null;
    $(definition.startNodes).each(function(index, node) {
        
        if (nodeId != null) {
            return;
        }
        
        nodeId = _resolveSvgNodeIdFromGraph(node, svgNodeId);
    });
    
    return nodeId;
}

function _resolveSvgNodeIdFromGraph(node, svgNodeId) {
    
    //
    // is it this node?
    //
    if (node.attributes["idXml"].toLowerCase() == svgNodeId) {
        return node.id;
    }
    
    //
    // is it one of it's destinations?
    //
    var nodeId = null;
    $(node.outgoingControlFlows).each(function(index, controlFlow) {
        
        if (nodeId != null) {
            return;
        }
        
        nodeId = _resolveSvgNodeIdFromGraph(controlFlow.destination, svgNodeId);
    });
    
    return nodeId;
}