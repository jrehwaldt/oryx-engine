//
// This script depends on
//   1) debugger-crud.js
//   
//   5) repository-crud.js
//   6) jquery.timers-1.2.js (optional)
//

$().ready(function() {
    
    // 
    // load available tables
    // 
    loadProcessDefinitionsOverview();
    loadInterruptedInstancesOverview();
    
    //
    // register the refresh overview handlers
    //
    $('#definitions-overview-refresh').click(function(event) {
        event.preventDefault();
        loadProcessDefinitionsOverview();
    });
    
    $('#interrupted-instances-overview-refresh').click(function(event) {
        event.preventDefault();
        loadInterruptedInstancesOverview();
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
 * Loads the process definitions table and clear any old entries.
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
                        + '<td>' + _generateDefinitionHTML(definition) + '</td>'
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
 * Loads the interrupted instances table and clear any old entries.
 */
function loadInterruptedInstancesOverview() {
    var tableBody = $('table#interrupted-instances-overview tbody');
    
    if (tableBody.length != 0) {
        loadInterruptedInstances(function(interruptedInstances) {
            tableBody.empty();
            $(interruptedInstances).each(function(index, interruptedInstance) {
                var interruptedInstanceId = interruptedInstance.id;
                var instanceId = interruptedInstance.interruptedInstance;
                var definitionId = idToString(interruptedInstance.interruptedInstance.definition.id);
                var interruptedInstanceRow = $(
                    '<tr interrupted-instance-id="' + interruptedInstanceId + '" instance-id="' + instanceId + '" definition-id="' + definitionId + '">'
                        + '<td>' + _generateInterruptedInstanceHTML(interruptedInstance) + '</td>'
                    + '</tr>'
                );
                tableBody.append(interruptedInstanceRow);
                tableBody.trigger('interrupted-instance-table-entry:ready', [interruptedInstance, interruptedInstanceId, $(interruptedInstanceRow), tableBody]);
            });
            
            tableBody.trigger('interrupted-instance-table:ready', [tableBody]);
            tableBody.parent().find('th.loading-data').removeClass('loading-data');
        });
    }
};

/**
 * Shows the svg on full page.
 * 
 * @param definitionId the definition to show the svg for
 * @param options any additional data
 */
function showFullSvg(definitionId, options) {
    
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
        var frame = $('#svg-artifact-full-overlay div.full-svg-artifact');
        frame.attr('definition-id', definitionId);
        frame.html($(artifact).children().clone());
        frame.trigger('full-svg-loaded:ready', [definitionId, options]);
    });
};

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

/**
 * Generates definition html.
 * 
 * @param definition the definition object
 */
function _generateDefinitionHTML(definition) {
    return definition.name + '; Version: '
         + definition.id.version
         + (definition.description ? '<br/>' + definition.description : '');
}

/**
 * Generates interrupted instance html.
 * 
 * @param instance the instance object
 */
function _generateInterruptedInstanceHTML(interruptedInstance) {
    
    var instance = interruptedInstance.interruptedInstance;
    var definition = instance.definition;
    
    var result = 'Definition ' + definition.name + ' [Version ' + definition.id.version + ']<br/>'
               + 'Instance-ID ' + instance.id + '<br/>'
               + '<a href="#" class="show-full-svg-artifact">'
                   + '<div rel="#svg-artifact-full-overlay">Open as SVG</div>'
               + '</a>';

    return result;
}

/**
 * Generates breakpoint html.
 * 
 * @param breakpoint the breakpoint object
 */
function _generateBreakpointHTML(breakpoint) {
    
    if (!breakpoint) {
        return '';
    }
    
    var nodeDesc = '';
    if (breakpoint.node) {
        var node = breakpoint.node;
        nodeDesc = 'Node ' + (node.attributes['name'] ? node.attributes['name'] : breakpoint.node.id) + ' '
                 + (node.attributes['type'] ? '[' + node.attributes['type'] + ']' : '');
    }
    
    var result = (breakpoint.state ? 'State: ' + breakpoint.state + '<br/>' : '')
               + nodeDesc
               + (breakpoint.condition ? 'Condition: ' + breakpoint.condition.expression + '<br/>' : '')
               + '<a href="#" class="show-full-svg-artifact">'
                   + '<div rel="#svg-artifact-full-overlay">Show in process definition</div>'
               + '</a>';
    
    return result;
}

/**
 * Generates process instance html.
 * 
 * @param processInstance the process instance object
 */
function _generateInstanceContextHTML(processInstance) {
    var result = '';
    
    $.each(processInstance.context.variableMap, function(key, value) {
        result += '<div class="context-data-entry">'
               +      '<span class="key">' + key + '</span> = <span class="value">' + value + '</span>'
               +  '</div>';
    });
    
    return result;
}