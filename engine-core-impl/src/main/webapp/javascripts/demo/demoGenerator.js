$().ready(function() {
	// this Ajax request just generates the demodata
	$('#demoGenerate').click(function() {
		$.ajax({
			type : 'POST',
			url : '/api/demo/generate'
		});
		// redirect
		$(location).attr('href', '/')
	});

    // AJAX request for generating the reference process data and participants
	$('#referenceGenerate').click(function() {
		$.ajax({
			type : 'POST',
			url : '/api/demo/reference'
		});
		// redirect
		$(location).attr('href', '/management/')
	});

    // generates just the participant needed to execute the simple Human task XML defined process
	$('#generate-xml-processGenerate').click(function() {
		$.ajax({
			type : 'POST',
			url : '/api/demo/generate-xml-process'
		});
		// redirect
		$(location).attr('href', '/management/')
	});
});

