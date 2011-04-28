$().ready(function() {

    $.ajax({
        type: 'GET',
        url: '/api/navigator/status/running-instances',
        success: function(data) {
            console.log(data);
            var runningInstances = data;
            $.each(runningInstances, function(i, instance) {
                $("#runningInstances").append("<tr id= " + instance.id + " class=\"instance\"><td> " + instance.id + "</td><td> " + instance.definition.name + "</td><td> " + instance.definition.description + "</td></tr>");

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
        dataType: "json" // we expect json
    });
});

function makeToggle(runningInstance) {
	$("#"+runningInstance.id).click(function(){
		$("#"+runningInstance.id+"-tokenTable").toggle();
	});
	
}

//<td> " + instance.assignedTokens[0].currentActivityState + "</td>
