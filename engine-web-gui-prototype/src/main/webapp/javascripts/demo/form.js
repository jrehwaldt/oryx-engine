// Gets the form associated with the the worklistitem (@QueryParam) of the user which is currently logged in (local storage)
function getTheForm() {
    var itemId = $.getQueryParam("worklistitemId");
    // TODO maybe make the storage keys constants somewhere
	var participantId = $.Storage.get("participantUUID");

	// Get the form data. If the submit button was pressed, the client will be redirected and data will be saved with an ajax request.
	$.ajax({
        type: 'GET',
        url: '/api/worklist/items/' + itemId + '/form?participantId=' + participantId,
        success: function(data) {
            // Get the form into the DOM and give it all the necessary functionality.
        	$("#formContent").append(data);
        	$("div form")
        	    .attr("action","/api/worklist/items/" + itemId + "/form/?participantId=" + participantId)
        	    .submit(function(){
	            $(this).ajaxSubmit();
	            // redirect to the worklist
	            $(location).prop('href', '/worklist/');

	            // don't follow the form action
	            return false;
	        });
        },
        error: function(jqXHR, textStatus, errorThrown) {
            $('#notice').append("There seems to be no formular, just go back and hit End :-)").addClass('error');
        }
	});
}


$().ready(function(){
    getTheForm();
});

