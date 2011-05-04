// Adds the click handlers at the start for the claim,begin and end buttons.
function addButtonClickHandler() {

	addClaimButtonClickHandler();
	addBeginButtonClickHandler();
	// TODO Better to assign functions here instead of elements?
	addEndButtonClickHandler("button.end");
}
// Adds the claim button click handler. If the button was pressed it is removed (=> we can do that, because the begin button is already there)
function addClaimButtonClickHandler() {

    $("button.claim").click(function() {

        var worklistItemId = $(this).parents(".worklistitem").attr("id");
	    var wrapper = {};
	    wrapper["participantId"] = $.Storage.get("participantUUID");
	    wrapper["action"] = "CLAIM";
	    wrapper["@classifier"] = "de.hpi.oryxengine.rest.WorklistActionWrapper";
		var button = this;

        $.ajax({
            type: 'PUT',
            url: '/api/worklist/items/' + worklistItemId + '/state',
            data: JSON.stringify(wrapper), // maybe go back to queryparam (id= bla) for the participant UUID
            success: function(data) {
                // be happy and do stuff (like morph button to start task or stuff like that)
				$(button).unbind();
				$(button).remove();

            },
            error: function(jqXHR, textStatus, errorThrown) {
                $('#notice').html(jqXHR.responseText).addClass('error');
            },
            contentType: 'application/json'
        });
    });
}

// Adds the begin button click handler. If the button was pressed, it will change to an end button and the claim button will be removed (if present).
function addBeginButtonClickHandler() {

    // Now add the click handlers to the freshly created buttons
    // Click handlers shall claim an item
    $("button.begin").click(function() {
    	var button = this;
    	var worklistItemId = $(this).parents(".worklistitem").attr("id");
	    var wrapper = {};
	    wrapper["participantId"] = $.Storage.get("participantUUID");
	    wrapper["action"] = "BEGIN";
	    wrapper["@classifier"] = "de.hpi.oryxengine.rest.WorklistActionWrapper";

	    $.ajax({
	    	type: 'PUT',
	    	url: '/api/worklist/items/' + worklistItemId + '/state',
	    	data: JSON.stringify(wrapper), // TODO maybe go back to queryparam (id= bla) for the participant UUID
	    	success: function(data) {
				$(button).unbind();
				$(button).removeClass("begin").addClass("end").html("End");
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
	    	contentType: 'application/json'
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
		// here a wrapper object is used, to have the possibility to send multiple variables in one request.
	    var wrapper = {};
	    wrapper["participantId"] = $.Storage.get("participantUUID");
	    wrapper["action"] = "END";
	    wrapper["@classifier"] = "de.hpi.oryxengine.rest.WorklistActionWrapper";
		$.ajax({
			type: 'PUT',
			url: '/api/worklist/items/' + worklistItemId + '/state',
			data: JSON.stringify(wrapper), // maybe go back to queryparam (id= bla) for the participant UUID
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
			contentType: 'application/json'
		});

	});

}

// AJAX request to get the worklist for the selected participant
function getWorklistItems() {
    $.ajax({
        type: 'GET',
        url: '/api/worklist/items',
        data: 'id=' + $.Storage.get("participantUUID"),
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
            })
            addButtonClickHandler();
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // TODO more specific error
            $("#notice").html("You need to log in before you can see your worklist (or other server error).").addClass("error");

        },
        contentType: 'application/json', // we send json
        dataType: "json" // we expect json
    });
}

// at the beginning get all worklist items
$().ready(function(){
    getWorklistItems();
})

