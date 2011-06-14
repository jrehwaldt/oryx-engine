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
    $('*[is-halted="true"]', $('div.full-svg-artifact')).live('click', function(event) {
        var nodeFrame = $(this);
        var interruptedInstanceId = nodeFrame.attr('interrupted-instance-id');
        showDebugControls(interruptedInstanceId);
    });
    
    //
    // add the artifact data
    //
    var instancesTable = $('table#interrupted-instances-overview tbody');
    instancesTable.live('interrupted-instance-table-entry:ready', function(event, interruptedInstance, interruptedInstanceId, interruptedInstanceRow, tableBody) {
        
        var instance = interruptedInstance.interruptedInstance;
        var definition = instance.definition;
        var definitionId = idToString(definition.id);
        
        //
        // create a matched breakpoints cell
        //    and
        // add the svg artifact and an anchor for breakpoint operations
        //
        interruptedInstanceRow.append(
              '<td class="matching-breakpoint-cell">'
                + _generateBreakpointHTML(interruptedInstance.causingBreakpoint)
            + '</td>'
            + '<td class="instance-context-data-cell">'
                + _generateInstanceContextHTML(instance)
            + '</td>'
            + '<td class="controls-cell">'
                + '<a href="#" class="control-instance" control-type="continue" title="The interrupted instance will be released and continue until the next breakpoint matches.">Continue</a><br/>'
                + '<a href="#" class="control-instance" control-type="step-over" title="The interrupted instance will be released and continue to the next decision point.">Step Over</a><br/>'
                + '<a href="#" class="control-instance" control-type="resume" title="The interrupted instance will be released and continue to the process\' end.">Resume</a><br/>'
                + '<a href="#" class="control-instance" control-type="terminate" title="The interrupted instance will be terminated.">Terminate</a><br/>'
            + '</td>'
        );
        
        //
        // store the svg id with the definition id
        //
        if (definition.attributes['idXml']) {
            interruptedInstanceRow.attr('definition-svg-id', definition.attributes['idXml'].toLowerCase());
        }
        
        //
        // store the definition and debugger data
        //
        interruptedInstanceRow.data('debugger-data', definition.attributes['extension-debugger-attribute']);
        interruptedInstanceRow.data('definition', definition);
        interruptedInstanceRow.data('interrupted-instance', interruptedInstance);
        interruptedInstanceRow.data('instance', instance);
    });
    
    //
    // add the control functions when the table is loaded
    //
    instancesTable.live('interrupted-instance-table:ready', function(event, tableBody) {
        
        $('a.show-full-svg-artifact', tableBody).click(function(event) {
            event.preventDefault();
            var row = $(event.currentTarget).parent().parent();
            var definitionId = row.attr('definition-id');
            var interruptedInstance = row.data('interrupted-instance');
            var options = {
                instanceId: row.attr('instance-id'),
                interruptedInstanceId: row.attr('interrupted-instance-id'),
                interruptedInstance: interruptedInstance
            };
            showFullSvg(definitionId, options);
        });
        
        if ($(document).overlay) {
            $('a.show-full-svg-artifact *[rel]', tableBody).overlay();
        }
        
        $('.controls-cell a.control-instance', tableBody).click(function(event) {
            event.preventDefault();
            var anchor = $(event.currentTarget);
            var row = anchor.parent().parent();
            var interruptedInstanceId = row.attr('interrupted-instance-id');
            var debugCommand = anchor.attr('control-type');
            _controlInterruptedInstance(interruptedInstanceId, debugCommand, function(interruptedInstanceId) {
                var dialog = $('#control-successful-dialog');
                
                var commandResult;
                switch (debugCommand) {
                    case 'terminate':
                    case 'continue':
                    case 'resume':
                        commandResult = debugCommand + 'd';
                        break;
                    case 'step-over':
                        commandResult = 'stepped over';
                        break;
                }
                $('.commandResult', dialog).text(commandResult);
                dialog.dialog({
                    modal: true,
                    buttons: {
                        Ok: function() {
                            $( this ).dialog( "close" );
                        }
                    }
                });
                
                loadInterruptedInstancesOverview();
            });
        });
    });
    
    //
    // show interrupted node
    //
    var svgFrame = $('#svg-artifact-full-overlay div.full-svg-artifact');
    svgFrame.live('full-svg-loaded:ready', function(event, definitionId, options) {
        
        var frame = $(this);
        var interruptedInstanceId = options.interruptedInstanceId;
        var interruptedInstance = options.interruptedInstance;
        
        var currentNode = interruptedInstance.interruptedToken.currentNode;
        
        highlightCurrentNode(interruptedInstanceId, currentNode, frame);
    });
});

/**
 * Click handler method for svg node elements.
 * 
 * @param interruptedInstanceId the interrupted instance's id
 */
function showDebugControls(interruptedInstanceId) {
//    $('#control-process-dialog').dialog({
//        model: true
//    });
}

/**
 * Highlights the current node in the svg artifact overlay.
 * 
 * @param interruptedInstanceId the interrupted instance's id
 * @param currentNode the current node to highlight
 * @param frame the frame with the svg overlay
 */
function highlightCurrentNode(interruptedInstanceId, currentNode, frame) {
    
    //
    // get the current frame element
    //
    var svgNodeId = currentNode.attributes['idXml'];
    var svgNodeContainer = $('g[id="svg-' + svgNodeId + '"]', frame);
    var svgFrameContainer = $('g.me > g[id]', svgNodeContainer);
    var svgElementId = svgFrameContainer.attr('id');
    var svgFrameElement = $('*[id="' + svgElementId + 'bg_frame"]', svgNodeContainer);
    
    //
    // color orange
    //
    svgFrameElement.attr('previous-fill', svgFrameElement.attr('fill'));
    svgFrameElement.attr('fill', 'orange');
    svgFrameElement.attr('is-halted', 'true');
    svgFrameElement.attr('interrupted-instance-id', interruptedInstanceId);
};