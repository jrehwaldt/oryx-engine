$().ready(function() {
	var participants;
	$("#searchParticipant").val("");
	$.ajax({
        type: 'GET',
        url: '/api/identity/roles',
        success: function(data) {
            var roles = data;
            // add each role to the select box
            $.each(roles, function(i, role) {
                $("#roles").append("<option value=\"" + role.id + "\">" + role.name + "</option>");
            });
        },
        dataType: "json"
    });
	
	$.ajax({
        type: 'GET',
        url: '/api/identity/participants',
        success: function(data) {
        	participants = data;
            // add each role to the select box
            $.each(participants, function(i, participant) {
                $("#resultParticipants").append("<option value=\"" + participant.id + "\">" + participant.name + "</option>");
            });
        },
        dataType: "json"
    });
	
	$("#createParticipant").click(function() {
        $.ajax({
            type: 'POST',
            url: '/api/identity/participants',
            data: $("#newParticipant").val()
        });
    });
	
	$("#createRole").click(function() {
        $.ajax({
            type: 'POST',
            url: '/api/identity/roles',
            data: $("#newRole").val()
        });
    })
    
    $("#searchParticipant").keyup(function(event) {
    	$("#resultParticipants").empty();
    	var $textField = $(this);
    	console.log($("#resultParticipants"));
    	$.each(participants, function(i, participant) {
            if (participant.name.indexOf($textField.val())!= (-1)) {
            	$("#resultParticipants").append("<option value=\"" + participant.id + "\">" + participant.name + "</option>");
			}
        });
    	
    })
})