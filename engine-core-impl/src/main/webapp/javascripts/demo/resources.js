var participants;
var unassignedParticipants;
var assignedParticipants;
$().ready(function() {
	var additions = [];
	var removals = [];	
	
	// reset the search field on site load
	$("#searchParticipant").val("");
	
	console.log([1,2,3]);
	getParticipantsAndFillBoxes();
	fillRolesBox();
	
	
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
    	console.log(participants);
    	$("#unassignedParticipants").empty();
    	var $textField = $(this);
    	$.each(participants, function(i, participant) {
    		
            if (participant.name.indexOf($textField.val())!= (-1)) {
            	$("#unassignedParticipants").append("<option value=\"" + participant.id + "\">" + participant.name + "</option>");
			}
        });
    	
    })
    
    // add the selected participants to the list of to-be-added participants for a role
    $("#addParticipant").click(function() {
    	var selectedParticipants = $("#unassignedParticipants :selected");
    	selectedParticipants.each(function(i, option) {
    		$.merge(additions,[option.value]);
    		$(option).remove();
    		$("#assignedParticipants").append(option);
    	})
    })
    
    // add the selected participants to the list of to-be-removed participants for a role
    $("#removeParticipant").click(function() {
    	var selectedParticipants = $("#assignedParticipants :selected");
    	selectedParticipants.each(function(i, option) {
    		$.merge(removals,[option.value]);
    		$(option).remove();
    		$("#unassignedParticipants").append(option);
    	})
    })
    
    //
    $("#roles").change(function() {
    	$("#assignedParticipants").empty();
    	fillBoxes(participants);
	})
    
	
	
    // submit the two lists of to-be-added and to-be-removed participants
    $("#submitChanges").click(function() {
    	var changeSet = {};
    	changeSet["@classifier"] = "de.hpi.oryxengine.rest.PatchCollectionChangeset";
    	changeSet["additions"] = additions;
    	changeSet["removals"] = removals;
    	console.log(changeSet);
    	additions = [];
    	removals = [];
    	var roleID = $("#roles :selected").val();
    	$.ajax({
            type: 'PATCH',
            url: '/api/identity/roles/' + roleID + '/participants' ,
            data: JSON.stringify(changeSet),
            contentType: "application/json"
        })
    })
})

function getParticipantsAndFillBoxes() {
	$.ajax({
        type: 'GET',
        url: '/api/identity/participants',
        success: function(data) {
        	participants = data;
//            // add each role to the select box
            fillBoxes(participants);
        },
        dataType: "json"
    });
}

function fillBoxes(allParticipants) {
	var selectedRoleID = $("#roles :selected").val();
	$.ajax({
        type: 'GET',
        url: '/api/identity/roles/' + selectedRoleID + '/participants',
        success: function(data) {
        	assignedParticipants = data;
//        	$.merge(unassignedParticipants, participants);
        	
        	$("#unassignedParticipants").empty();
        	$.each(participants, function(i, participant) {
                $("#unassignedParticipants").append("<option value=\"" + participant.id + "\">" + participant.name + "</option>");
            });
        	
//            // add each role to the select box
            $.each(roleParticipants, function(i, roleParticipant) {
            	console.log(roleParticipant.id);
            	$("#unassignedParticipants").removeOption(roleParticipant.id);
                $("#assignedParticipants").append("<option value=\"" + roleParticipant.id + "\">" + roleParticipant.name + "</option>");
            });
        },
        dataType: "json"
    });
}

function fillRolesBox() {
	$.ajax({
        type: 'GET',
        url: '/api/identity/roles',
        success: function(data) {
            var roles = data;
//            // add each role to the select box
            $.each(roles, function(i, role) {
                $("#roles").append("<option value=\"" + role.id + "\">" + role.name + "</option>");
            });
        },
        dataType: "json"
    });
}