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

	// Creates a participant which can be used for uploading processes.
	$('#generate-xml-processGenerate').click(function() {
		$.ajax({
			type : 'POST',
			url : '/api/demo/generate-xml-process'
		});
		// redirect
		$(location).prop('href', '/management/')
	});
});

