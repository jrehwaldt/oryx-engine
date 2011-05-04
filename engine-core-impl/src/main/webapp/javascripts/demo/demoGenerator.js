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

	// Creates a participant which can be used for uploading processes.
	$('#generate-xml-processGenerate').click(function() {
		$.ajax({
			type : 'POST',
			url : '/api/demo/generate-xml-process'
		});
		// redirect
		$(location).attr('href', '/management/')
	});
});
