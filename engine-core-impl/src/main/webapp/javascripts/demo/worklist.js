/**
/*  Adds the click handlers for all the buttons.
*/
function addButtonClickHandler() {
    // Now add the click handlers to the freshly created buttons
    // Click handlers shall claim an item
	addBeginClickHandler("button.begin");

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
				$(button).removeClass("claim").addClass("begin").html("Begin");
				$(button).unbind();
				addBeginClickHandler(button);

            },
            error: function(jqXHR, textStatus, errorThrown) {
                $('#participants').html(jqXHR.responseText).addClass('error');
            },
            contentType: 'application/json'
        });
    });
}

function addBeginClickHandler(button) {
	$(button).click(function() {
		//TODO insert server logic here
		alert('haha gerardo muss arbeiten, :D');
	});
}


$().ready(function(){

    $("#welcomeMessage").append("Welcome " + $.Storage.get("participantName") + ", here is your worklist:");

    // AJAX request to get the worklist for the selected participant
    $.ajax({
        type: 'GET',
        url: '/api/worklist/items',
        data: 'id='+$.Storage.get("participantUUID"),
        success: function(data) {
            var worklist = data;
            $.each(worklist, function(i, worklistitem){

                // TODO determine whether we have to claim a task or to start one
				var button = generateButton(worklistitem.status);
                $('#worklist').append("<tr id=" + worklistitem.id + " class=\"worklistitem\"> <td>" + worklistitem.task.subject + "</td><td> " + worklistitem.task.description + "</td><td>"+button+"</td></tr>");
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

function generateButton(state) {
	var button;
	switch(state) {
		case "ALLOCATED": button = "<button class=\"begin\">Begin</button>";break;
		case "OFFERED": button = "<button class=\"claim\">Claim</button>";break;
	}
	return button;
}
