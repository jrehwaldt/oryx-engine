// Gets all Participants that the engine knows.
function getAllParticipants() {
    $.ajax({
        type: 'GET',
        url: 'api/identity/participants',
        success: function(data) {
            var participants = data;
            // add each participant to the loginbox
            $.each(participants, function(i, participant) {
                $("#loginBox").append("<option value=\"" + participant.id + "\">" + participant.name + "</option>");
            });
        },
        error: function(jqXHR, textStatus, errorThrown) {
            $('#notice').html(jqXHR.responseText).addClass('error');
        },
        dataType: "json" // we expect json
    });
}

// the loginbutton checks which participant is selected and then stores this data using the jQuery storage plugin and changes the site
function assignLoginButtonFunctionality() {
    $("#loginButton").click(function(){
        var participantUUID = $('#loginBox option:selected').attr('value');
        var participantName = $('#loginBox option:selected').html();
        $.Storage.set("participantUUID", participantUUID);
        $.Storage.set("participantName", participantName);
        // redirect the usert to the worklist so he may start working
        $(location).attr('href', '/worklist/');
    });
}


$().ready(function() {
    getAllParticipants();
    assignLoginButtonFunctionality();
});

