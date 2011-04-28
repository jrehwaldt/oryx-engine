$().ready(function(){

	var itemId = $.getQueryParam("worklistitemId");
	var participantId = $.Storage.get("participantUUID");

	var options = {
        //target:        '#output1',   // target element(s) to be updated with server response
        beforeSubmit:  before,  // pre-submit callback
        success:       after  // post-submit callback

        // other available options:
        //url:       url         // override for form's 'action' attribute
        //type:      type        // 'get' or 'post', override for form's 'method' attribute
        //dataType:  null        // 'xml', 'script', or 'json' (expected server response type)
        //clearForm: true        // clear all form fields after successful submit
        //resetForm: true        // reset the form after successful submit

        // $.ajax options can be used here too, for example:
        //timeout:   3000
    };


	$.ajax({
        type: 'GET',
        url: '/api/worklist/items/' + itemId + '/form?participantId=' + participantId,
        success: function(data) {
        	$("#formContent").append(data);
        	console.log(window.location.pathname + window.location.search);
        	$("form").attr("action","/api/worklist/items/" + itemId + "/form/?participantId=" + participantId);
        	$("form").submit(function(){
	            alert('lool');
	            $(this).ajaxSubmit(options);
	            return false;
	        });
        }
	});





});

function before(formData, jqForm, options){

    boxes = $("input:checked")

    {
        newBox = new Object();
        newBox.name = box.attr("name");
        newBox.value =
    }
    console.log(formData);
    alert('About to submit: \n\n' + queryString);
    return true;
}

function after(responseText, statusText, xhr, $form){
    alert('status: ' + statusText + '\n\nresponseText: \n' + responseText);
}

