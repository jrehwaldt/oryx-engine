$().ready(function() {
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
});

/**
 * Loads the roles table and clear any old entries.
 */
function loadOverviewTable() {
    loadRoles(function(roles) {
        var tableBody = $('table#roles-overview tbody');
        tableBody.empty();
        $(roles).each(function(index, role) {
            var roleRoles = '';
//            $(role.myRoles).map(function() {
//                return this.name; // this == role
//            }).get().join(", ");
            
            tableBody.append(
                '<tr role-id="' + role.id + '">'
                    + '<td>' + role.name + '</td>'
//                    + '<td>' + roleRoles + '</td>'
                    + '<td class="controls">'
//                        + '<a href="#" class="edit">Edit</a> '
                        + '<a href="#" class="delete">Delete</a>'
                    + '</td>'
                + '</tr>'
            );
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
    });
}

// ====================================================================
// ============================= CRUD =================================
// ====================================================================

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
        dataType: "json"
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
        dataType: "json"
    });
};

///**
// * Edits the specified role and calls the provided function.
// * 
// * @param role the role data
// * @param sucessHandler the anonymous function to call, gets the role as #1 parameter
// */
//function editRole(role, successHandler) {
//    alert('edit: ' + role);
//};
