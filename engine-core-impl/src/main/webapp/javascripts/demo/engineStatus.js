$().ready(function() {

    $.ajax({
        type: 'GET',
        url: '/api/navigator/status/running-instances',
        success: function(data) {
            console.log(data);
            var runningInstances = data;
            $.each(runningInstances, function(i, instance) {
                $("#runningInstances").append("<ul>LOL</ul>");
            });

        },
        dataType: "json" // we expect json
    });
});

