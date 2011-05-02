$().ready(function(){

	var itemId = $.getQueryParam("worklistitemId");
	var participantId = $.Storage.get("participantUUID");

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

	            // somehow this is important
	            return false;
	        });
        },
        error: function(jqXHR, textStatus, errorThrown) {
        // TODO NOTIFICATION AREA
            $('#notice').html(jqXHR.responseText).addClass('error');
        }
	});

});

