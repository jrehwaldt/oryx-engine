$().ready(function() {
      // this Ajay request just generates the demodata
      $('#demoGenerate').click(function() {
        $.ajax({
              type: 'POST',
              url: '/api/demo/generate'
        });
        // redirect
        $(location).attr('href', '/')
      });

      $('#referenceGenerate').click(function() {
        $.ajax({
              type: 'POST',
              url: '/api/demo/reference'
        });
        // redirect
	    $(location).attr('href', '/management/')
      });


});

