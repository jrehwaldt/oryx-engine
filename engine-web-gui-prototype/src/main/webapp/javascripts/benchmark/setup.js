$().ready(function {
    var engine_url = "172.16.22.246:8380";

    // deploy the reference process
    $.ajax({
			type : 'POST',
			url : '/api/demo/reference'
		})
});

