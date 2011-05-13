$().ready(function() {
    // 
    // refresh the participants table
    // 
    loadOverviewTable();
    
    //
    // register the refresh overview handler
    //
    $('#participants-overview-refresh').click(function(event) {
        event.preventDefault();
        loadOverviewTable();
    });
    
    //
    // register the create participant handler
    //
    $("#participants-create").submit(function(event) {
        event.preventDefault();
        var form = this;
        $.ajax({
            type: 'POST',
            url: '/api/identity/participants',
            success: function(data) {
                form.reset();
                loadOverviewTable();
            },
            data: $("input[name=participant-name]", this).val()
        });
    });
});

/**
 * Loads the participants table and clear any old entries.
 */
function loadOverviewTable() {
    loadParticipants(function(participants) {
        var tableBody = $('table#participants-overview tbody');
        tableBody.empty();
        $(participants).each(function(index, participant) {
            var participantRoles = $(participant.myRoles).map(function() {
                return this.name; // this == role
            }).get().join(", ");
            
            tableBody.append(
                '<tr participant-id="' + participant.id + '">'
                    + '<td>' + participant.name + '</td>'
                    + '<td>' + participantRoles + '</td>'
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
            var participantId = row.attr('participant-id');
            editParticipant(participantId);
        });
        $('.controls a.delete', tableBody).click(function(event) {
            event.preventDefault();
            var row = $(event.target).parent().parent();
            var participantId = row.attr('participant-id');
            deleteParticipant(participantId, function(participantId) {
                row.remove();
            });
        });
    });
};