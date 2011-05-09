$().ready(function() {
    var engine_url = "172.16.22.246:8380";

    // deploy the reference process without participants, as these will be created by the clients.
    $.ajax({
			type : 'POST',
			url : '/api/demo/reference-without-participants'
		});
});

