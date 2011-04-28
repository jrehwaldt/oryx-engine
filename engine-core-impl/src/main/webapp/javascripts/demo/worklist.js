function addButtonClickHandler() {

	addClaimButtonClickHandler();
	addBeginButtonClickHandler();
	addEndButtonClickHandler("button.end");
}

function addClaimButtonClickHandler() {
	
	//TODO make a method out of this shit
    $("button.claim").click(function() {

        var worklistItemId = $(this).parents(".worklistitem").attr("id");
	    var wrapper = {};
	    wrapper["participantId"] = $.Storage.get("participantUUID");
	    wrapper["action"] = "CLAIM";
	    wrapper["@classifier"] = "de.hpi.oryxengine.rest.WorklistActionWrapper";
	    console.log(wrapper);
		var button = this;

        $.ajax({
            type: 'PUT',
            url: '/api/worklist/items/' + worklistItemId + '/state',
            data: JSON.stringify(wrapper), // maybe go back to queryparam (id= bla) for the participant UUID
            success: function(data) {
                console.log(data);
                // be happy and do stuff (like morph button to start task or stuff like that)
				$(button).unbind();
				$(button).remove();

            },
            error: function(jqXHR, textStatus, errorThrown) {
                $('#participants').html(jqXHR.responseText).addClass('error');
            },
            contentType: 'application/json'
        });
    });
}

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
	    console.log(wrapper);
	    
	    $.ajax({
	    	type: 'PUT',
	    	url: '/api/worklist/items/' + worklistItemId + '/state',
	    	data: JSON.stringify(wrapper), // maybe go back to queryparam (id= bla) for the participant UUID
	    	success: function(data) {
	    		console.log(data);
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
	    		$('#participants').html(jqXHR.responseText).addClass('error');
	    	},
	    	contentType: 'application/json'
	    });

    });
}

function addEndButtonClickHandler(handler) {
	addFormularLinkFor(handler);
	$(handler).click(function() {
    	var button = this;
    	var worklistItemId = $(this).parents(".worklistitem").attr("id");
	    var wrapper = {};
	    wrapper["participantId"] = $.Storage.get("participantUUID");
	    wrapper["action"] = "END";
	    wrapper["@classifier"] = "de.hpi.oryxengine.rest.WorklistActionWrapper";
	    console.log(wrapper);
		$.ajax({
			type: 'PUT',
			url: '/api/worklist/items/' + worklistItemId + '/state',
			data: JSON.stringify(wrapper), // maybe go back to queryparam (id= bla) for the participant UUID
			success: function(data) {
				console.log(data);
				//TODO implement mechanism to refresh items for other clients;
				$(button).unbind();
				$(button).parent().parent().remove();	

				// be happy and do stuff (like morph button to start task or stuff like that)
				
			},
			error: function(jqXHR, textStatus, errorThrown) {
				$('#participants').html(jqXHR.responseText).addClass('error');
			},
			contentType: 'application/json'
		});

		});
		
}


$().ready(function(){

    $("#welcomeMessage").append("Welcome " + $.Storage.get("participantName") + ", here is your worklist:");

    // AJAX request to get the worklist for the selected participant
    $.ajax({
        type: 'GET',
        url: '/api/worklist/items',
        data: 'id=' + $.Storage.get("participantUUID"),
        success: function(data) {
            var worklist = data;
            $.each(worklist, function(i, worklistitem){

                // TODO determine whether we have to claim a task or to start one
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
            $('#worklist').html(jqXHR.responseText).addClass('error');
        },
        contentType: 'application/json', // we send json
        dataType: "json" // we expect json
    });

})

function generateButton(item) {
	var button;
	switch(item.status) {
		case "ALLOCATED": button = "<button class=\"begin\">Begin</button>"; break;
		case "OFFERED": button = "<button class=\"claim\">Claim</button><button class=\"begin\">Begin</button>"; break;
		case "EXECUTING": button = "<button class=\"end\">End</button>"; addFormularLinkFor(button); break;
	}
	return button;
}

function addFormularLinkFor(handler) {
	var itemID = $(handler).parents(".worklistitem").attr("id");
	$(handler).parent().prepend("<a href=\"/worklist/form/index.jsp?=worklistitemId=" + itemID + "\">Formular</a>");
}
