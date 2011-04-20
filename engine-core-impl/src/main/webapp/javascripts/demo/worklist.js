$().ready(function(){

    $("#welcomeMessage").append("Welcome " + $.Storage.get("participantName") + ", here is your worklist:");

    // AJAX request to get the worklist for the selected participant
    $.ajax({
        type: 'GET',
        url: '/api/worklist/items',
        data: 'id='+$.Storage.get("participantUUID"),
        success: function(data) {
            var worklist = data;
            $.each(worklist, function(i, worklistitem){

                $('#worklist').append("<tr><td>" + worklistitem.task.subject + "</td><td> " + worklistitem.task.description + "</td></tr>");
            });

        },
        error: function(jqXHR, textStatus, errorThrown) {
            $('#worklist').html(jqXHR.responseText).addClass('error');
        },
        contentType: 'application/json', // we send json
        dataType: "json" // we expect json
    });
})

