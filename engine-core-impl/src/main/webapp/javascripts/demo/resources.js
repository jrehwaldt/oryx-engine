var participants;
var unassignedParticipants;
var assignedParticipants;

$().ready(function() {
	var additions = [];
	var removals = [];	
	
	// reset the search field on site load
	$("#searchParticipant").val("");
	
	// fill the boxes for roles and participants
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
	
	// on click, create the participant with the name given in the name field
	$("#createRole").click(function() {
        $.ajax({
            type: 'POST',
            url: '/api/identity/roles',
            data: $("#newRole").val()
        });
    })
    

	/* 
	 * kind of buggy ATM, shows too much participants, should only 
     * show the unassigned participants for the selected role
     * 
     */
    // instant search for participants that shall be added to a role
    $("#searchParticipant").keyup(function(event) {
    	$("#unassignedParticipants").empty();
    	var $textField = $(this);
    	$.each(participants, function(i, participant) {
    		// add the participants that match to the 'unassignedParticipants' box
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
    
    // if the role changes, the assigned and unassigned participants need to be adjusted
    $("#roles").change(function() {
    	$("#assignedParticipants").empty();
    	fillBoxes(participants);
	})
    
    // submit the two lists of to-be-added and to-be-removed participants to the server
    $("#submitChanges").click(function() {
    	// build the JSON
    	var changeSet = {};
    	changeSet["@classifier"] = "de.hpi.oryxengine.rest.PatchCollectionChangeset";
    	changeSet["additions"] = additions;
    	changeSet["removals"] = removals;
    	// clear the list of participants, that shall be deleted from and added to roles
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

/**
 * gets the participants via AJAX GET from the server and
 * fills the boxes by the help of {@link #fillBoxes(allParticipants)}
 */
function getParticipantsAndFillBoxes() {
	$.ajax({
        type: 'GET',
        url: '/api/identity/participants',
        success: function(data) {
        	participants = data;
            // add each role to the select box
            fillBoxes(participants);
        },
        dataType: "json"
    });
}

/**
 * fills the boxes of assigned and unassigned participants
 * according to the selected role
 * @param allParticipants all participants that exist
 */
function fillBoxes(allParticipants) {
	var selectedRoleID = $("#roles :selected").val();
	$.ajax({
        type: 'GET',
        url: '/api/identity/roles/' + selectedRoleID + '/participants',
        success: function(data) {
        	assignedParticipants = data;
        	
        	$("#unassignedParticipants").empty();
        	$.each(participants, function(i, participant) {
                $("#unassignedParticipants").append("<option value=\"" + participant.id + "\">" + participant.name + "</option>");
            });
        	
            // add each role to the select box
            $.each(assignedParticipants, function(i, assignedParticipant) {
            	$("#unassignedParticipants").removeOption(assignedParticipant.id);
                $("#assignedParticipants").append("<option value=\"" + assignedParticipant.id + "\">" + assignedParticipant.name + "</option>");
            });
        },
        dataType: "json"
    });
}

/**
 * fills the roles box according to the roles that exist in the engine
 */
function fillRolesBox() {
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
}