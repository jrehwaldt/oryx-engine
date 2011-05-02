
/**
/*  Enables the button to do his work - start a process instance that is.
/*  This is called after all the elements and buttons have been added to the DOM.
*/
function enableButtonClickHandler() {
    $("button.start").click(function() {
        var definitionId = $(this).parents(".definition").attr("id");

        $.ajax({
            type: 'POST',
            url: '/api/navigator/processdefinitions/' + definitionId + '/instances',
            success: function(data) {
                console.log(data);

                // refresh the list of running process instances (since we just added one)
                getRunningProcessInstances();
            },
            error: function(jqXHR, textStatus, errorThrown) {
                 $('#notice').html(jqXHR.responseText).addClass('error');
            }
        });
    })
}

/**
/* Gets the running instances AJAX-style
*/
function getRunningProcessInstances() {
    $.ajax({
        type: 'GET',
        url: '/api/navigator/status/running-instances',
        success: function(data) {
            console.log(data);
            var runningInstances = data;
            // clean up before we load them
            // TODO maybe remember the opened trs/tokens before that
            $('#runningInstances').empty();
            $.each(runningInstances, function(i, instance) {
                $("#runningInstances").append("<tr id= " + instance.id + " class=\"instance clickable\"><td> " + instance.id + "</td><td> " + instance.definition.name + "</td><td> " + instance.definition.description + "</td></tr>");

			var include = "<tr style=\"display:none;\"  id=\""+instance.id+"-tokenTable\"><td colspan=\"3\"><div style=\"margin-left:30px;\"> <table style=\"width:100%;\"><tr><th>Token UUID</th><th>State</th><th>Activity</th><th>Node</th></tr><tr>";
//style=\"display:none;\"
			$.each(instance.assignedTokens, function(n, token) {
				include += "<td style=\"width:25%;\">"+token.id+"</td>";
				if(token.currentActivity) {
					include += "<td style=\"width:25%;\">"+token.currentActivityState+"</td><td style=\"width:25%;\">"+token.currentActivity['@classifier']+"</td>";
				}else{
					include += "<td style=\"width:25%;\">None</td><td style=\"width:25%;\">None</td>";
				}
				include += "<td style=\"width:25%;\">"+token.currentNode.id+"</td>";
			});
			include += "</tr></div></table></td></tr>"
			$("#"+instance.id).after(include);

			makeToggle(this);
            });

        },
       error: function(jqXHR, textStatus, errorThrown) {
            $('#notice').html(jqXHR.responseText).addClass('error');
        },
        dataType: "json" // we expect json
    });
}

/**
/* Gets the deployed Process definitions AJAX-style
*/
function getProcessDefinitions() {
    $.ajax({
        type: 'GET',
        url: '/api/repository/processdefinitions',
        success: function(data) {
            console.log(data);
            var processdefinitions = data;
            $.each(processdefinitions, function(i, definition) {
                $("#processDefinitionList").append("<tr id= " + definition.id + " class=\"definition\"><td>" + definition.name + "</td><td> " + definition.description + "</td><td><button class=\"start\">Start</button></td></tr>");
                //$("#loginBox").append("<option value=\"" + participant.id + "\">" + participant.name + "</option>");
            });
            enableButtonClickHandler();

        },
        error: function(jqXHR, textStatus, errorThrown) {
            $('#notice').html(jqXHR.responseText).addClass('error');
        },
        dataType: "json" // we expect json
    });
}


$().ready(function() {
    getRunningProcessInstances();
    getProcessDefinitions();
});

function makeToggle(runningInstance) {
	$("#"+runningInstance.id).click(function(){
		$("#"+runningInstance.id+"-tokenTable").toggle();
	});

}

function out(input) {
    console.log(input);
}

//<td> " + instance.assignedTokens[0].currentActivityState + "</td>

