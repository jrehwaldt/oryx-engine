// Adds the click handlers at the start for the claim,begin and end buttons.
function addButtonClickHandler(workListItem) {

	var button;
	button = $("#"+workListItem).find(".claim");
	if(button.length) {
		addClaimButtonClickHandler(button);
	}
	button = $("#"+workListItem).find(".begin");
	if(button.length) {
		addBeginButtonClickHandler(button);
	}
	button = $("#"+workListItem).find(".end");
	if(button.length) {
		addEndButtonClickHandler(button);
	}

}
// Adds the claim button click handler. If the button was pressed it is removed (=> we can do that, because the begin button is already there)
function addClaimButtonClickHandler(button) {

    button.click(function() {
        var worklistItemId = $(this).parents(".worklistitem").attr("id");
		var button = this;

        $.ajax({
            type: 'PUT',
            url: '/api/worklist/items/' + worklistItemId + '/state?participantId='+$.Storage.get("participantUUID"),
            data: 'ALLOCATED', // maybe go back to queryparam (id= bla) for the participant UUID
            success: function(data) {
                // be happy and do stuff (like morph button to start task or stuff like that)
				$(button).unbind();
				$(button).remove();

            },
            error: function(jqXHR, textStatus, errorThrown) {
				$("#"+worklistItemId).fadeOut(function() {
					$("#"+worklistItemId).remove()
				});
                $('#notice').html(jqXHR.responseText).addClass('error');
            },
            contentType: 'text/plain'
        });
    });
}

// Adds the begin button click handler. If the button was pressed, it will change to an end button and the claim button will be removed (if present).
function addBeginButtonClickHandler(button) {

    // Now add the click handlers to the freshly created buttons
    // Click handlers shall claim an item
    $(button).click(function() {
    	var button = this;
    	var worklistItemId = $(this).parents(".worklistitem").attr("id");

	    $.ajax({
	    	type: 'PUT',
	    	url: '/api/worklist/items/' + worklistItemId + '/state?participantId='+$.Storage.get("participantUUID"),
	    	data: 'EXECUTING',
	    	success: function(data) {
				$(button).unbind();
				$(button).removeClass("begin").addClass("end").html("End"); //TODO perhaps better to delete and then add a new button, instead of changing the old one
				addEndButtonClickHandler(button);

				//only remove the claim button if the button is still there
				var element = $(button).parent().find(".claim");
				if(element.length){
					element.unbind();
					element.remove();
				}

	    		// be happy and do stuff (like morph button to start task or stuff like that)

	    	},
	    	error: function(jqXHR, textStatus, errorThrown) {
	    		$('#notice').html(jqXHR.responseText).addClass('error');
	    	},
	    	contentType: 'text/plain'
	    });

    });
}

// Generate the appropriate button for the workitem, e.g. only a begin button if the status is allocated.
function generateButton(item) {
	var button;
	switch(item.status) {
		case "ALLOCATED": button = "<button class=\"begin\">Begin</button>"; break;
		case "OFFERED": button = "<button class=\"claim\">Claim</button><button class=\"begin\">Begin</button>"; break;
		case "EXECUTING": button = "<button class=\"end\">End</button>"; addFormLinkFor(button); break;
	}

	return button;
}

// Adds the form link before the end button in the worklist item row
function addFormLinkFor(handler) {
	var itemID = $(handler).parents(".worklistitem").attr("id");
	$(handler).parent().prepend("<a href=\"/worklist/items/form?worklistitemId=" + itemID + "\">Form</a>");
}

// Adds the end click handler to a given element. It then uses a put request with a JSON wrapper end the item.
function addEndButtonClickHandler(handler) {
	addFormLinkFor(handler);
	$(handler).click(function() {
    	var button = this;
    	var worklistItemId = $(this).parents(".worklistitem").attr("id");
		$.ajax({
			type: 'PUT',
			url: '/api/worklist/items/' + worklistItemId + '/state?participantId='+$.Storage.get("participantUUID"),
			data: 'COMPLETED', // maybe go back to queryparam (id= bla) for the participant UUID
			success: function(data) {
				$(button).unbind();
				// remove the data row, because the worklist item was finished
				//$(button).parents("#"+worklistItemId).remove();

				// refresh worklistitems, so the clerk sees the all new worklist items
		        getWorklistItems();
			},
			error: function(jqXHR, textStatus, errorThrown) {
				$('#notice').html(jqXHR.responseText).addClass('error');
			},
			contentType: 'text/plain'
		});

	});

}

// AJAX request to get the worklist for the selected participant
function getWorklistItems() {
    $.ajax({
        type: 'GET',
        url: '/api/worklist/items?id=' + $.Storage.get("participantUUID"),
        success: function(data) {
            $("#welcomeMessage").html("Welcome " + $.Storage.get("participantName") + ", here is your worklist:");
            var worklist = data;
            // delete all contents before loading new contents
            $('#worklist').empty();
            $.each(worklist, function(i, worklistitem){

				var button = generateButton(worklistitem);
                $('#worklist').append("<tr id=" + worklistitem.id + " class=\"worklistitem\">"
                					  + "<td>" + worklistitem.task.subject + "</td>"
                					  + "<td>" + worklistitem.task.description + "</td>"
                					  + "<td>" + button + "</td>"
                					  + "</tr>");

				addButtonClickHandler(worklistitem.id);
            })

        },
        error: function(jqXHR, textStatus, errorThrown) {
            // TODO more specific error
            $("#notice").html("You need to log in before you can see your worklist (or other server error).").addClass("error");

        },
        dataType: "json" // we expect json
    });
}

// At the beginning get all worklist items
$().ready(function(){
    getWorklistItems();
})

