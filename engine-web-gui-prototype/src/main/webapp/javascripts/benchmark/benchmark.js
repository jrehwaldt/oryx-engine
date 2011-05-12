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
NUMBER_OF_INSTANCES = 10;
NUMBER_OF_TASKS_TO_EXECUTE = 5;
NUMBER_OF_ERRORS_TO_WAIT = 2;

ITEM_STATE = {
    "offered" : "OFFERED",
    "allocated" : "ALLOCATED",
    "executing" : "EXECUTING",
    "completed" : "COMPLETED"
};


// globals
// holds the UUID of the currently logged in participant
participantUUID = undefined;
numberOfRunningInstances = undefined;
errorCounter = undefined;
taskCounter = undefined;

/*
 * GET Land
 */

// gets all the available roles and returns them
function getRolesAndThen(func) {
	$.ajax({
        // this call is synchronous to ensure that
        // there are participants created for roles
        // before we start loggin in as one
        async: false,
        type: 'GET',
        url: JODA_ENGINE_ADRESS + '/api/identity/roles',
        success: function(data) {
            func(data);
        },
        dataType: "json"
    });
}

// get all the participants and return them
function getParticipantsAndThen(func) {
	$.ajax({
        type: 'GET',
        url: JODA_ENGINE_ADRESS + '/api/identity/participants',
        success: function(data) {
        	func(data);
        },
        dataType: "json",
        async: false
    });
}

// get all the deployed process definitions and return them
function getProcessDefinitionsAndThen(func) {
	$.ajax({
        type: 'GET',
        url: JODA_ENGINE_ADRESS + '/api/repository/process-definitions',
        success: function(data) {
        	func(data);
        },
        dataType: "json"
    });
}

// get all the running instances and return them
function setRunningProcessInstances() {
	$.ajax({
        type: 'GET',
        url: JODA_ENGINE_ADRESS + '/api/navigator/status/running-instances',
        success: function(data) {
        	numberOfRunningInstances = data.length;
        },
        dataType: "json"
    });
}

/*
 *  General Utility helpers
 */

// return a random element from a collection
function getRandomElementFrom(collection) {

     var itemNumber = Math.floor(Math.random() * collection.length);
     return collection[itemNumber];
}

/*
 *	Participant creation helpers!
 */

function generateParticipantname() {
	var time = new Date().getTime();
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

// Creates some participants that will be used by the benchmark users and log in as one of them
function createParticipantsFromRoles(roles) {
    var i;
    $.each(roles, function(i, role) {
        for (i = 0; i < PARTICIPANTS_PER_ROLE; i++) {
            createParticipantWithRole(role.id);
        }
    });
}


/*
 * Process creation helpers
 */

// start a process instance of the given definition
function startProcessInstance(definition) {
    $.ajax({
        async: false,
		type: 'POST',
		url: JODA_ENGINE_ADRESS + '/api/navigator/process-definitions/' + definition.id + '/start'
	});
}

// start a specific number of process instances for each definition
function startProcessInstancesFromDefinitions(definitions) {
    var i;
    $.each(definitions, function(i, definition) {
        for (i = 0; i < NUMBER_OF_INSTANCES; i++) {
            startProcessInstance(definition);
        }
    });
    setRunningProcessInstances();
}


/*
 *  Worklist helpers (many!)
 */

// change an item's state to a given state
function changeItemState(itemId, state, errorCounter) {

    $.ajax({
	    	type: 'PUT',
	    	url: '/api/worklist/items/' + itemId + '/state?participantId=' + participantUUID,
	    	data: state,
            contentType: 'text/plain',
	    	success: function(data) {
	    	    // TODO log succcesfull answers?!
	    	},
	    	error: function(jqXHR, textStatus, errorThrown) {
                errorCounter++;
            }
	});
}

// Begin the work on a random worklist item (it's a callback of the GetWorklistItems function)
function beginRandomWorklistItem(worklistItems) {
    var item = getRandomElementFrom(worklistItems);
    changeItemState(item.id, ITEM_STATE.executing);
}

function endRandomWorklistItem(worklistItems) {
    var item = getRandomElementFrom(worklistItems);
    changeItemState(item.id, ITEM_STATE.completed);
}

// get all offered worklist items and start to work on a random one
function workOnOfferedWorklistItems() {
    $.ajax({
        type: 'GET',
        url: '/api/worklist/items?id=' + participantUUID + '&itemState=' + ITEM_STATE.offered,
        success: function(data) {
            var worklistItems = data;
            while ((errorCounter < NUMBER_OF_ERRORS_TO_WAIT) && (taskCounter < NUMBER_OF_TASKS_TO_EXECUTE) && !($.isEmptyObject(worklistItems))) {
                beginRandomWorklistItem(worklistItems);
                taskCounter++;
            }
        },
        dataType: "json" // we expect json
    });
}

// get all executing worklist items and end some of them
function workOnExecutingWorklistItems() {
    $.ajax({
        type: 'GET',
        url: '/api/worklist/items?id=' + participantUUID + '&itemState=' + ITEM_STATE.executing,
        success: function(data) {
            var worklistItems = data;
            console.log($.isEmptyObject(worklistItems));
            while (errorCounter < NUMBER_OF_ERRORS_TO_WAIT && taskCounter < NUMBER_OF_TASKS_TO_EXECUTE && !($.isEmptyObject(worklistItems))) {
                endRandomWorklistItem(worklistItems);
                taskCounter++;
            }
        },
        dataType: "json" // we expect json
    });
}

// log in as a random participant
function logInAsRandomParticipant(participantList) {

    var participant = getRandomElementFrom(participantList);
    // we are well aware that this is a global variable (as we are logged in and want to use it elsewhere)
    console.log(participantUUID);
    participantUUID = participant.id;
    console.log("Login ready");
}

/**********************************
 *                                *
 *      DSL functions             *
 *                                *
 **********************************/

// start instances of the deployed process definitions
function startProcessInstances() {
    getProcessDefinitionsAndThen(startProcessInstancesFromDefinitions);
}

// creates participants and assigns them to the roles and log in as one of them
function createParticipants() {
    getRolesAndThen(createParticipantsFromRoles);
}

function logMeIn() {
    getParticipantsAndThen(logInAsRandomParticipant);
}

function workOnTasks() {
    taskCounter = 0;
    errorCounter = 0;
    var j = 0;
    logMeIn();
    while(errorCounter < NUMBER_OF_ERRORS_TO_WAIT && taskCounter < NUMBER_OF_TASKS_TO_EXECUTE && j < 5) {
        workOnExecutingWorklistItems();
        workOnOfferedWorklistItems();
        j++;
    }
}

/************************************
 *                                  *
 *     Main management code         *
 *           land                   *
 *                                  *
 ************************************/

function startBenchmark() {
    var i = 0;
    while(numberOfRunningInstances != 0 && i < 5){
        workOnTasks();
        setRunningProcessInstances();
        i++;
    }
}

$().ready(function() {
    createParticipants();
    startProcessInstances();
    console.log(numberOfRunningInstances);
    $('#benchmarkStart').click(startBenchmark);
});

