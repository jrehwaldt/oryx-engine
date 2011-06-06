<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
        <script type="text/javascript" src="/javascripts/management/navigator-crud.js"></script>
        <script type="text/javascript" src="/javascripts/management/repository-crud.js"></script>
        <script type="text/javascript" src="/javascripts/management/definitions.js"></script>
        <script type="text/javascript" src="/javascripts/management/instances.js"></script>
        
        <%-- optional 
        <script type="text/javascript" src="/javascripts/lib/jquery.timers-1.2.js"></script>
        --%>
        
        <title>JodaEngine | Debugger</title>
    </head>
    <body>
        <h1>Debugger</h1>
        
        <h2>Process Instances</h2>
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
        
        <p>
            See a detailed overview on the 
            <a href="/management/instances" title="Process Definition">instances page</a>.
        </p>
        
        <h2>Process Definitions</h2>
        <table id="definitions-overview" width="100%">
            <thead>
                <tr>
                    <th class="loading-data">Name</th>
                    <th class="loading-data">Version</th>
                    <th class="loading-data">Description</th>
                    <th class="loading-data">Action</th>
                </tr>
            </thead>
            <tbody></tbody>
        </table>
        
        <a href="#" id="definitions-overview-refresh">Refresh definitions</a>
        
        <p>
            See a detailed overview and an 
            <a href="/management/definitions#upload" title="Process Definition Upload Form">process definition upload form</a>
            on the 
            <a href="/management/definitions" title="Process Definition">definitions page</a>.
        </p>
    </body>
</html>
