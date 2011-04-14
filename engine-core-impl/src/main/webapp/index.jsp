<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html> 
    <head> 
        <link rel="SHORTCUT ICON" href="images/favicon/favicon.ico" type="image/x-icon">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <link rel="stylesheet" type="text/css" href="stylesheets/layout.css">
        
        <script type="text/javascript" src="javascripts/lib/jquery-1.5.2.js"></script>
        <script type="text/javascript" src="javascripts/workitem.js"></script>
        
        <title>Oryx Engine goes REST</title>
    </head>
    <body>
        <h1>REST-interface working ;-)</h1>
        <ul>
            <li>
                <h2>Navigator</h2>
                <ul>
                    <li><a href="api/navigator/statistic">Statistik</a></li>
                    <li><a href="api/navigator/start">Start the engine</a></li>
                    <li><a href="api/navigator/stop">Stop the engine</a></li>
                </ul>
             </li>
             <li>
                <h2>Worklist</h2>
                <ul>
                    <li><a href="api/worklist/todo">TODO</a></li>
                </ul>
             </li>
        </ul>
        
        <div id="demo"></div>
        
        <script type="text/javascript">
        $().ready(function() {
            /*
            $.ajax({
                method: 'POST',
                url: 'api/worklist/item/claim',
                data: {workItem: DEMO_WORKITEM, resource: null},
                success: function(data) {
                    $('#demo').html(data);
                },
                dataType: dataType
            });
            */
            
            $.post('api/worklist/item/claim', {workItem: DEMO_WORKITEM, resource: null},
                function(data) {
                    $('#demo').html(data);
                },
                "json"
            );
        });
        </script>
        
    </body>
</html>