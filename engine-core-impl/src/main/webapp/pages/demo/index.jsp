<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
    Demo data created
    </head>
    <body>
        <h1>Demodata got created!</h1>
        <script>
          $().ready(function() {
            $.ajax({
              type: 'POST',
              url: '/api/demo/generate'
            })
          });
        </script>

    </body>
</html>

