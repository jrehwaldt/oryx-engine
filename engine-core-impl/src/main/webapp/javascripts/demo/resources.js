$().ready(function() {
	var participants;
	var additions = [];
	var removals = [];
	
	// reset the search field on site load
	$("#searchParticipant").val("");
	
	// get all roles existing in the engine on site load
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
	
	// get all participants existing in the engine on site load
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
	
	// create the participant with the name given in the name field
	$("#createParticipant").click(function() {
        $.ajax({
            type: 'POST',
            url: '/api/identity/participants',
            data: $("#newParticipant").val()
        });
    });
	
	// create the participant with the name given in the name field
	$("#createRole").click(function() {
        $.ajax({
            type: 'POST',
            url: '/api/identity/roles',
            data: $("#newRole").val()
        });
    })
    
    // instant search for participants that shall be added to a role
    $("#searchParticipant").keyup(function(event) {
    	$("#resultParticipants").empty();
    	var $textField = $(this);
    	$.each(participants, function(i, participant) {
            if (participant.name.indexOf($textField.val())!= (-1)) {
            	$("#resultParticipants").append("<option value=\"" + participant.id + "\">" + participant.name + "</option>");
			}
        });
    	
    })
    
    // add the selected participants to the list of to-be-added participants for a role
    $("#addParticipant").click(function() {
    	$("#resultParticipants :selected").each(function(i, option) {
    		$.merge(additions,[option.value]);
    	})
    })
    
    // add the selected participants to the list of to-be-removed participants for a role
    $("#removeParticipant").click(function() {
    	$("#relatedParticipants :selected").each(function(i, option) {
    		$.merge(removals,[option.value]);
    	})
    })
    
    //
    $("#roles").change(function() {
    	var roleID = $(this + ":selected").val();
    	$.ajax({
            type: 'GET',
            url: '/api/identity/roles/' + roleID + '/participants',
            success: function(data) {
            	participants = data;
                // add each role to the select box
                $.each(participants, function(i, participant) {
                    $("#relatedParticipants").append("<option value=\"" + participant.id + "\">" + participant.name + "</option>");
                });
            },
            dataType: "json"
        });
	})
    
    // submit the two lists of to-be-added and to-be-removed participants
    $("#submitChanges").click(function() {
    	var changeSet = {};
    	changeSet["@classifier"] = "de.hpi.oryxengine.rest.PatchCollectionChangeset";
    	changeSet["additions"] = additions;
    	changeSet["removals"] = removals;
    	console.log(changeSet);
    	var roleID = $("#roles :selected").val();
    	$.ajax({
            type: 'PATCH',
            url: '/api/identity/roles/' + roleID + '/participants' ,
            data: JSON.stringify(changeSet),
            contentType: "application/json"
        })
    })
})