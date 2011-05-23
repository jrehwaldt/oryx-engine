<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
        <script type="text/javascript" src="/javascripts/management/navigator-crud.js"></script>
        <script type="text/javascript" src="/javascripts/management/repository-crud.js"></script>
        <script type="text/javascript" src="/javascripts/management/definitions.js"></script>
        
        <%-- optional 
        <script type="text/javascript" src="/javascripts/lib/jquery.timers-1.2.js"></script>
        --%>
        
        <title>JodaEngine | Management: Process Definition Explorer</title>
    </head>
    <body>
        <h1>Management: Process Definition Explorer</h1>
        
        <h2>Overview</h2>
        <table id="definitions-overview" width="100%">
            <thead>
                <tr>
                    <th class="loading-data">Name</th>
                    <th class="loading-data">Description</th>
                    <th class="loading-data">Action</th>
                </tr>
            </thead>
            <tbody></tbody>
        </table>
        
        <a href="#" id="definitions-overview-refresh">Refresh definitions</a>
        
        <div id="definitions-upload">
            <a name="upload"></a>
            <h2>Process Definition Upload</h2>
            <form id="definitions-upload-form" method="post" action="/api/repository/process-definitions/deploy" accept="application/xml" enctype="multipart/form-data">
                <input type="file" name="xmlRepresentation" size="40" />
                <input type="submit" value="Deploy" />
            </form>
        </div>
        <div id="archive-upload">
            <a name="archiveupload"></a>
            <h2>Archive Upload (.dar-File))</h2>
            <form id="archive-upload-form" method="post" action="/api/repository/deployments" enctype="multipart/form-data">
                <input type="file" name="filedata" size="40" />
                <input type="submit" value="Deploy" />
            </form>
        </div>
    </body>
</html>

