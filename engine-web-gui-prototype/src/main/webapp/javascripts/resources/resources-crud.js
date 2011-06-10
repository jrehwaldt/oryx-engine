//====================================================================
//============================= CRUD Participants ====================
//====================================================================

/**
* Fetches all participants and calls the provided function.
* 
* @param successHandler the anonymous function to call, gets the participants as #1 parameter
* @param roleId can be null, if provided only the participants for this role are fetched
*/
function loadParticipants(successHandler, roleId) {
    $.ajax({
        type: 'GET',
        url: '/api/identity' + (roleId ? '/roles/' + roleId : '') + '/participants',
        success: function(participants) {
            if (successHandler)
                successHandler.apply(null, [participants]);
        },
        dataType: 'json'
    });
};

/**
* Deletes the specified participant and calls the provided function.
* 
* @param participantId the participant's, which should be deleted
* @param successHandler the anonymous function to call, gets the participantId as #1 parameter
*/
function deleteParticipant(participantId, successHandler) {
    $.ajax({
        type: 'DELETE',
        url: '/api/identity/participants/' + participantId,
        success: function() {
            if (successHandler)
                successHandler.apply(null, [participantId]);
        },
        dataType: 'json'
    });
};

///**
//* Edits the specified participant and calls the provided function.
//* 
//* @param participant the participant data
//* @param sucessHandler the anonymous function to call, gets the participant as #1 parameter
//*/
//function editParticipant(participant, successHandler) {
//    alert('edit: ' + participant);
//};


//====================================================================
//============================= CRUD Roles ===========================
//====================================================================

/**
* Fetches all roles and calls the provided function.
* 
* @param successHandler the anonymous function to call, gets the roles as #1 parameter
*/
function loadRoles(successHandler) {
    $.ajax({
        type: 'GET',
        url: '/api/identity/roles',
        success: function(roles) {
            if (successHandler)
                successHandler.apply(null, [roles]);
        },
        dataType: 'json'
    });
};

/**
* Deletes the specified role and calls the provided function.
* 
* @param roleId the role's, which should be deleted
* @param successHandler the anonymous function to call, gets the roleId as #1 parameter
*/
function deleteRole(roleId, successHandler) {
    $.ajax({
        type: 'DELETE',
        url: '/api/identity/roles/' + roleId,
        success: function() {
            if (successHandler)
                successHandler.apply(null, [roleId]);
        },
        dataType: 'json'
    });
};

///**
//* Edits the specified role and calls the provided function.
//* 
//* @param role the role data
//* @param sucessHandler the anonymous function to call, gets the role as #1 parameter
//*/
//function editRole(role, successHandler) {
//    alert('edit: ' + role);
//};

/**
* Updates the role membership and calls the provided function.
* 
* @param roleId the role's, which should be updated
* @param additions the participants[], which should be added
* @param removals the participants[], which should be removed
* @param successHandler the anonymous function to call, gets the roleId, additions and removals as param #1, #2 and #3
*/
function updateRoleMember(roleId, additions, removals, successHandler) {
    // build the JSON
    var changeSet = {};
    changeSet["@classifier"] = "org.jodaengine.rest.PatchCollectionChangeset";
    changeSet["additions"] = additions;
    changeSet["removals"] = removals;
    $.ajax({
        type: 'PATCH',
        url: '/api/identity/roles/' + roleId + '/participants' ,
        data: JSON.stringify(changeSet),
        success: function() {
            if (successHandler)
                successHandler.apply(null, [roleId, additions, removals]);
        },
        contentType: 'application/json'
    });
};