<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
        <script type="text/javascript" src="/javascripts/management/navigator-crud.js"></script>
        <script type="text/javascript" src="/javascripts/management/instances.js"></script>
        
        <%-- optional 
        <script type="text/javascript" src="/javascripts/lib/jquery.timers-1.2.js"></script>
        --%>
        
        <title>JodaEngine | Management: Process Engine Status</title>
    </head>
    <body>
        <h1>Management: Process Engine Status</h1>
        
        <h2>Overview</h2>
        <table id="running-instances-overview" width="100%">
            <thead>
                <tr>
                    <th class="loading-data">ID</th>
                    <th class="loading-data">Name</th>
                    <th class="loading-data">Description</th>
                </tr>
            </thead>
            <tbody></tbody>
        </table>
        
        <a href="#" id="running-instances-overview-refresh">Refresh running instances</a>
    </body>
</html>

