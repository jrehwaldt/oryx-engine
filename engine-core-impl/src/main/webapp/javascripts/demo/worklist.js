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

                // TODO determine whether we have to claim a task or to start one
                $('#worklist').append("<tr><td class=\"id\">" + worklistitem.id + "</td><td>" + worklistitem.task.subject + "</td><td> " + worklistitem.task.description + "</td><td><button class=\"claim\">Claim</button></td></tr>");
            });

            // Now add the click handlers to the freshly created buttons
            // Click handlers shall claim an item
            $("button.claim").click(function() {

                var worklistItemId = $(this).parent().parent().find('.id').html();
				var wrapper = {};
				wrapper["participantId"] = $.Storage.get("participantUUID");
				wrapper["action"] = "CLAIM";
				wrapper["@classifier"] = "de.hpi.oryxengine.rest.WrapperObject";
				console.log(wrapper);

                $.ajax({
                    type: 'PUT',
                    url: '/api/worklist/items/' + worklistItemId + '/state',
                    data: JSON.stringify(wrapper), // maybe go back to queryparam (id= bla) for the participant UUID
                    success: function(data) {
                        console.log(data);
                        // be happy and do stuff (like morph button to start task or stuff like that)

                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        $('#participants').html(jqXHR.responseText).addClass('error');
                    },
                    contentType: 'application/json', // we send json
    });
            });

        },
        error: function(jqXHR, textStatus, errorThrown) {
            $('#worklist').html(jqXHR.responseText).addClass('error');
        },
        contentType: 'application/json', // we send json
        dataType: "json" // we expect json
    });
})

