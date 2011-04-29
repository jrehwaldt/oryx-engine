$().ready(function() {
      // this Ajay request just generates the demodata
      $('#demoGenerate').click(function() {
        $.ajax({
              type: 'POST',
              url: '/api/demo/generate'
        })
      });

      $('#referenceGenerate').click(function() {
        $.ajax({
              type: 'POST',
              url: '/api/demo/reference'
        })
      });


});

