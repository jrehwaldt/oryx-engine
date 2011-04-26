$().ready(function() {

    $.ajax({
        type: 'GET',
        url: '/api/navigator/status/running-instances',
        success: function(data) {
            console.log(data);
            var runningInstances = data;
            $.each(runningInstances, function(i, instance) {
                $("#runningInstances").append("<tr id= " + instance.id + " class=\"instance\"><td> " + instance.id + "</td><td> " + instance.definition.name + "</td><td> " + instance.definition.description + "</td><td> " + instance.assignedTokens[0].currentActivityState + "</td></tr>");
            });

        },
        dataType: "json" // we expect json
    });
});

