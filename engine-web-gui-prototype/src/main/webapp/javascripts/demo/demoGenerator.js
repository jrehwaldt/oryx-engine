$().ready(function() {
	// This Ajax request just generates the simple example process demo data.
	$('#demoGenerate').click(function() {
		$.ajax({
			type : 'POST',
			url : '/api/demo/generate'
		});
		// redirect
		$(location).prop('href', '/')
	});

    // AJAX request for generating the reference process data and participants
	$('#referenceGenerate').click(function() {
		$.ajax({
			type : 'POST',
			url : '/api/demo/reference'
		});
		// redirect
		$(location).prop('href', '/management/')
	});

	$('#generate-xml-processGenerate').click(function() {
		$.ajax({
			type : 'POST',
			url : '/api/demo/generate-xml-process'
		});
		// redirect
		$(location).prop('href', '/management/')
	});

	$('#process-start-emailGenerate').click(function() {
		$.ajax({
			type : 'POST',
			url : '/api/demo/generate-process-start-email'
		});
		// redirect
		$(location).attr('href', '/management/')
	});
	
});

