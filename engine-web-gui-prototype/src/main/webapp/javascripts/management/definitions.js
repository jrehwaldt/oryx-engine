//
// This script depends on
//   1) navigator-crud.js
//   2) repository-crud.js
//   3) jquery.timers-1.2.js (optional)
//

$().ready(function() {
    // 
    // refresh the running instances table
    // 
    loadProcessDefinitionsOverview();
    
    //
    // enable continues refresh, if available
    //
    if ($(document).everyTime) {
        $(document).everyTime(2500, function(i) {
            loadProcessDefinitionsOverview();
        }, 0); // 0 = unbound
    }
    
    //
    // register the refresh overview handler
    //
    $('#definitions-overview-refresh').click(function(event) {
        event.preventDefault();
        loadProcessDefinitionsOverview();
    });
});


/**
 * Loads the running process instances table and clear any old entries.
 */
function loadProcessDefinitionsOverview() {
    var tableBody = $('table#definitions-overview tbody');
    
    if (tableBody.length != 0) {
        loadProcessDefinitions(function(definitions) {
            tableBody.empty();
            $(definitions).each(function(index, definition) {
                tableBody.append(
                    '<tr definition-id="' + definition.id + '">'
                        + '<td>' + definition.name + '</td>'
                        + '<td>' + definition.description + '</td>'
                        + '<td class="controls">'
	                        + '<a href="#" class="start-instance">Start</a> '
	                    + '</td>'
                    + '</tr>'
                );
            });
            $('.controls a.start-instance', tableBody).click(function(event) {
                event.preventDefault();
                var row = $(event.target).parent().parent();
                var definitionId = row.attr('definition-id');
                startProcessInstance(definitionId, function() {
                    //
                    // refresh the process instance's view, if it exists
                    //
                    if (window.loadRunningProcessInstancesOverview) {
                        loadRunningProcessInstancesOverview();
                    }
                });
            });
            tableBody.parent().find('th.loading-data').removeClass('loading-data');
        });
    }
};
