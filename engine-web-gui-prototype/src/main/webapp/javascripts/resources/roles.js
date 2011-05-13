var _participants = [];

$().ready(function() {
    //
    // load participants
    //
    loadParticipants(function(data) {
        _participants = data;
    });
    
    // 
    // refresh the roles table
    // 
    loadOverviewTable();
    
    //
    // register the refresh overview handler
    //
    $('#roles-overview-refresh').click(function(event) {
        event.preventDefault();
        loadOverviewTable();
    });
    
    //
    // register the create role handler
    //
    $("#roles-create").submit(function(event) {
        event.preventDefault();
        var form = this;
        $.ajax({
            type: 'POST',
            url: '/api/identity/roles',
            success: function(data) {
                form.reset();
                loadOverviewTable();
            },
            data: $("input[name=role-name]", this).val()
        });
    });
    
    //
    // register dialog search feature
    //
    $('.search-participants').keyup(function(event) {
        filterUnassignedParticipants($(this).val());
    });
});

/**
 * Loads the roles table and clear any old entries.
 */
function loadOverviewTable() {
    var tableBody = $('table#roles-overview tbody');
    
    if (tableBody.length != 0) {
        loadRoles(function(roles) {
            tableBody.empty();
            $(roles).each(function(index, role) {
                var roleRoles = '';
                
                tableBody.append(
                    '<tr role-id="' + role.id + '">'
                        + '<td>' + role.name + '</td>'
                        + '<td class="role-relationship loading-data"></td>'
                        + '<td class="controls">'
                            + '<a href="#" class="change-member">Change members</a> '
    //                        + '<a href="#" class="edit">Edit</a> '
                            + '<a href="#" class="delete">Delete</a>'
                        + '</td>'
                    + '</tr>'
                );
                
                loadParticipants(function(participants) {
                    updateParticipantRoleRelationship(role.id, participants);
                }, role.id);
            });
            $('.controls a.edit', tableBody).click(function(event) {
                event.preventDefault();
                var row = $(event.target).parent().parent();
                var roleId = row.attr('role-id');
                editRole(roleId);
            });
            $('.controls a.delete', tableBody).click(function(event) {
                event.preventDefault();
                var row = $(event.target).parent().parent();
                var roleId = row.attr('role-id');
                deleteRole(roleId, function(roleId) {
                    row.remove();
                });
            });
            $('.controls a.change-member', tableBody).click(function(event) {
                event.preventDefault();
                var row = $(event.target).parent().parent();
                var roleId = row.attr('role-id');
                showUpdateRoleMemberDialog(roleId);
            });
            tableBody.parent().find('th.loading-data').removeClass('loading-data');
        });
    }
};

/**
 * Loads the roles table and clear any old entries.
 * 
 * @param roleId the role id, which needs to be changed
 */
function showUpdateRoleMemberDialog(roleId) {
    var dialog = $('#roles-change-member-dialog.dialog');
    
    var form = $('form#roles-change-member', dialog);
    var unassigned = $('.unassigned-participants', form);
    var assigned = $('.assigned-participants', form);
    
    //
    // load the role's members
    //
    loadParticipants(function(assignedParticipants) {
        $('.changeset', dialog).empty();
        
        $.each(_participants, function(i, participant) {
            unassigned.append('<option value="' + participant.id + '">' + participant.name + '</option>');
        });
        
        // add each role to the select box
        $.each(assignedParticipants, function(i, assignedParticipant) {
            unassigned.removeOption(assignedParticipant.id);
            assigned.append('<option value="' + assignedParticipant.id + '">' + assignedParticipant.name + '</option>');
        });
    }, roleId);
    
    //
    // register add-handler: add the selected participants to the list of to-be-added participants for a role
    //
    $('.add-participants', form).click(function() {
        var selectedParticipants = $(':selected', unassigned);
        selectedParticipants.each(function(i, option) {
            $(option).remove();
            assigned.append(option);
        });
    });
    
    //
    // register remove-handler: add the selected participants to the list of to-be-removed participants for a role
    //
    $('.remove-participants', form).click(function() {
        var selectedParticipants = $(':selected', assigned);
        selectedParticipants.each(function(i, option) {
            $(option).remove();
            unassigned.append(option);
        });
    });
    
    //
    // show the dialog
    //
    dialog.dialog({
        width: 400,
        modal: true,
        buttons: [{
            text: 'Cancel',
            click: function() {
                $(this).dialog('close');
                $('.search-participants', this).val('');
            }
        }, {
            text: 'Apply changes',
            click: function() {
                $(this).dialog('close');
                $('.search-participants', this).val('');
                submitUpdateRoleMemberForm($(this), roleId);
            }
        }]
    });
};

/**
 * Submits the update participant-role relationship form.
 * 
 * @param form the form
 * @param roleId the role's id to apply the changes on
 */
function submitUpdateRoleMemberForm(form, roleId) {
    var unassigned = $('.unassigned-participants option', form);
    var assigned = $('.assigned-participants option', form);
    
    var additions = assigned.map(function() {
        return $(this).val(); // this == option
    }).get();
    var removals = unassigned.map(function() {
        return $(this).val(); // this == option
    }).get();
    updateRoleMember(roleId, additions, removals, function(roleId) {
        updateParticipantRoleRelationship(roleId, additions);
    });
};

/**
 * Updates the table information about role relationships.
 * 
 * @param roleId the role's id to update
 * @param participants the role's participant ids OR the whole participant objects
 */
function updateParticipantRoleRelationship(roleId, participants) {
    var roleRow = $('table#roles-overview tbody tr[role-id=' + roleId + ']');
    var targetColumn = roleRow.children('.role-relationship');
    
    var participantsString = $(_participants).map(function(index, _participant) {
        var participantName = null;
        
        $(participants).each(function(index, participant) {
            if ((participant.id && _participant.id == participant.id) || _participant.id == participant)
                participantName = _participant.name;
        });
        
        return participantName;
    }).get().join(", ");
    
    targetColumn.removeClass('loading-data').text(participantsString);
};

/**
 * Filters the participant list.
 * 
 * @param filter the filter to apply
 */
function filterUnassignedParticipants(filter) {
    var unassigned = $(".unassigned-participants");
    // instant search for participants that shall be added to a role
    unassigned.empty();
    
    $(_participants).each(function(index, _participant) {
        // add the participants that match to the 'unassigned-participants' box
        if (_participant.name.toLowerCase().indexOf(filter.toLowerCase()) != -1) {
            unassigned.append('<option value="' + _participant.id + '">' + _participant.name + '</option>');
        }
    });
};