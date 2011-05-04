// Gets the form associated with the the worklistitem (@QueryParam) of the user which is currently logged in (local storage)
function getTheForm() {
    var itemId = $.getQueryParam("worklistitemId");
    // TODO maybe make the storage keys constants somewhere
	var participantId = $.Storage.get("participantUUID");

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
	            $(location).attr('href', '/worklist/');

	            // somehow this is important (see jqueryForm plugin documentation for a reference)
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

