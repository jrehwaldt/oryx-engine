$().ready(function() {
	// This Ajax request just generates the simple example process demo data.
	$('#demoGenerate').click(function() {
		$.ajax({
			type : 'POST',
			url : '/api/demo/generate'
		});
		// redirect
		$(location).attr('href', '/')
	});

	// This Ajax request deploys the reference process.
	$('#referenceGenerate').click(function() {
		$.ajax({
			type : 'POST',
			url : '/api/demo/reference'
		});
		// redirect
		$(location).attr('href', '/management/')
	});

	$('#xml-processGenerate').click(function() {
		$.ajax({
			type : 'POST',
			url : '/api/demo/generate-xml-process'
		});
		// redirect
		$(location).attr('href', '/management/')
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
