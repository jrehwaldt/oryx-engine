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
                .submit(function() {
                    // set the content-type to force jQuery to send it even for empty forms
                    $(this).ajaxSubmit({contentType: 'application/x-www-form-urlencoded'});
                    
                    // redirect
                    $(location).attr('href', '/worklist/');
                    
                    // prevents reloading the webpage (browser standard behaviour)
                    return false;
                });
        },
        error: function(jqXHR, textStatus, errorThrown) {
            $('#notice').append("There seems to be no formular, just go back and hit End :-)").addClass('error');
            //$('#notice').html(jqXHR.responseText).addClass('error');
        }
    });

});

