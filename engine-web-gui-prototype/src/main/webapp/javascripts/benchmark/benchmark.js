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
JODA_ENGINE_ADRESS = 'localhost';

// GET Land

// gets all the available roles and returns them
function getRoles() {
	$.ajax({
        type: 'GET',
        url: JODA_ENGINE_ADRESS + '/api/identity/roles',
        success: function(data) {
            return data;
        },
        dataType: "json"
    });
}

// get all the participants and return them
function getParticipants() {
	$.ajax({
        type: 'GET',
        url: JODA_ENGINE_ADRESS + '/api/identity/participants',
        success: function(data) {
        	return data;
        },
        dataType: "json"
    });
}

// get all the deployed process definitions and return them
function getProcessDefinitions() {
	$.ajax({
        type: 'GET',
        url: JODA_ENGINE_ADRESS + '/api/repository/process-definitions',
        success: function(data) {
        	return data;
        },
        dataType: "json"
    });
}

// get all the running instances and return them and return them
function getRunningProcessInstances() {
	$.ajax({
        type: 'GET',
        url: JODA_ENGINE_ADRESS + '/api/navigator/status/running-instances',
        success: function(data) {
        	return data;
        },
        dataType: "json"
    });
}
