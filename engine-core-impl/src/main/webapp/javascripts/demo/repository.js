/**
/*  Enables the button to do his work.
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
                // be happy and do stuff (like morph button to start task or stuff like that)

            },
            error: function(jqXHR, textStatus, errorThrown) {
                // should be displayed in notification area or some like that
                // $('#participants').html(jqXHR.responseText).addClass('error');
            }
        });
    })
}


$().ready(function() {
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
            $('#participants').html(jqXHR.responseText).addClass('error');
        },
        dataType: "json" // we expect json
    });
});

