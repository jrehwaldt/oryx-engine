// This is the benchmark javascript which will simulate a participant

/*****************************************************
*													 *
*													 *
*					Function land					 *
*													 *
*		Here some general functions are provided	 *
*													 *
******************************************************/

// constants
JODA_ENGINE_ADRESS = 'localhost:8380';

// GET Land

// gets all the available roles and returns them
function getRoles(func) {
	$.ajax({
        type: 'GET',
        url: JODA_ENGINE_ADRESS + '/api/identity/roles',
        success: function(data) {
            func(data);
        },
        dataType: "json"
    });
}

// get all the participants and return them
function getParticipants(func) {
	$.ajax({
        type: 'GET',
        url: JODA_ENGINE_ADRESS + '/api/identity/participants',
        success: function(data) {
        	func(data);
        },
        dataType: "json"
    });
}

// get all the deployed process definitions and return them
function getProcessDefinitions(func) {
	$.ajax({
        type: 'GET',
        url: JODA_ENGINE_ADRESS + '/api/repository/process-definitions',
        success: function(data) {
        	func(data);
        },
        dataType: "json"
    });
}

// get all the running instances and return them and return them
function getRunningProcessInstances(func) {
	$.ajax({
        type: 'GET',
        url: JODA_ENGINE_ADRESS + '/api/navigator/status/running-instances',
        success: function(data) {
        	func(data);
        },
        dataType: "json"
    });
}

/*
*	Participant creation helpers!
*/

function generateParticipantname() {
	time = new Date.getTime();
	return "participant: " + time;
}

// create participant with the role
function createParticipantWithRole(name, roleID) {
	$.ajax({
		type: 'POST',
		url: '/api/identity/participants',
		data: name,
		success: function(data) {
			participantID = data;
			assignParticipantToRole(participantID, roleID);
		}
	});
}

// Creates some participants that will be used by the benchmark users
function createParticipants() {

}
