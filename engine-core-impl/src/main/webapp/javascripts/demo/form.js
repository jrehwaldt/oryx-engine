$().ready(function(){

	var itemId = $.getQueryParam("worklistitemId");
	var participantId = $.Storage.get("participantUUID");

	// Get the form data. If the submit button was pressed, the client will be redirected and data will be saved with an ajax request.
	$.ajax({
        type: 'GET',
        url: '/api/worklist/items/' + itemId + '/form?participantId=' + participantId,
        success: function(data) {
        	$("#formContent").append(data);
        	$("div form")
        	    .attr("action","/api/worklist/items/" + itemId + "/form/?participantId=" + participantId)
        	    .submit(function(){
	            $(this).ajaxSubmit();

	            // redirect
	            $(location).attr('href', '/worklist/');

	            // don't follow the form action
	            return false;
	        });
        },
        error: function(jqXHR, textStatus, errorThrown) {
            $('#notice').append("There seems to be no formular, just go back and hit End :-)").addClass('error');
            //$('#notice').html(jqXHR.responseText).addClass('error');
        }
	});

});

