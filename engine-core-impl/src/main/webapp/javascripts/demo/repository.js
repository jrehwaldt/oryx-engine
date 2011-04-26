$().ready(function() {
    $.ajax({
        type: 'GET',
        url: '/api/repository/processdefinitions',
        success: function(data) {
            console.log(data);
            var processdefinitions = data;
            $.each(processdefinitions, function(i, definition) {
                $("#processDefinitionList").append("<tr><td>" + definition.name + "</td><td> " + definition.description + "</td><td><button class=\"start\">Start</button></td></tr>");
                //$("#loginBox").append("<option value=\"" + participant.id + "\">" + participant.name + "</option>");
            });

        },
        error: function(jqXHR, textStatus, errorThrown) {
            $('#participants').html(jqXHR.responseText).addClass('error');
        },
        dataType: "json" // we expect json
    });
});

