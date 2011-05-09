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
JODA_ENGINE_ADRESS = '';
PARTICIPANTS_PER_ROLE = 5;
NUMBER_OF_INSTANCES = 1;

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
	time = new Date().getTime();
	return "participant: " + time;
}

// create participant with the role
function createParticipantWithRole(roleId) {
	$.ajax({
		type: 'POST',
		url: JODA_ENGINE_ADRESS + '/api/identity/roles/' + roleId + '/participants',
		data: generateParticipantname()
	});
}

// Creates some participants that will be used by the benchmark users
function createParticipantsFromRoles(roles) {
    $.each(roles, function(i, role) {
        for (var i = 0; i < PARTICIPANTS_PER_ROLE; i++) {
            createParticipantWithRole(role.id);
        }
    });
}

//
function startProcessInstancesFromDefinitions(definitions) {
    $.each(definitions, function(i, definition) {
        for (var i = 0; i < NUMBER_OF_INSTANCES; i++) {
            startProcessInstance(definition);
        }
    });
}

// starts a process instance of the given definition
function startProcessInstance(definition) {
    $.ajax({
		type: 'POST',
		url: JODA_ENGINE_ADRESS + '/api/navigator/process-definitions/' + definition.id + '/start'
	});
}

/**********************************
 *      DSL functions             *
 *                                *
 **********************************/

// start instances of the deployed process definitions
function startProcessInstances() {
    getProcessDefinitions(startProcessInstancesFromDefinitions);
}

// creates participants and assigns them to the roles
function createParticipants() {
    getRoles(createParticipantsFromRoles);
}

