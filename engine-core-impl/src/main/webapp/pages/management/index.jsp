<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
    <title>Management Explorer</title>
    <script type="text/javascript" src="/javascripts/demo/engineManagement.js"></script>
</head>
<body>
    <div id="engineStatus">
        <h1>Process Engine Status</h1>
        <div>
            Shows a list with all running process instances. A click on an instance's row provides more details.
        </div>
        <table style="width: 100%;">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Description</th>
                </tr>
            </thead>
            <tbody id="runningInstances">
            </tbody>
        </table>
    </div>
    
    
    <div id="definitionExplorer">
        <h1>Process Definition Explorer</h1>
        <div>
            Shows a list with all deployed process definitions.
        </div>
        <table style="width: 100%;">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody id="processDefinitionList"></tbody>
        </table>
    </div>
    
    <div id="processUpload">
        <h1>Process Upload</h1>
        <div>
            Provides an upload form for process definition files (accepts xml).
        </div>
        <form id="xmlUpload" method="post"
            action="/api/navigator/processdefinitions" accept="application/xml"
            enctype="multipart/form-data"><input type="file"
            name="xmlRepresentation" size="40" />
        <input type="submit" value="Deploy!" /></form>
    </div>
    
    <div>
        <h1>Reference Processes</h1>
        <div>
            Shows the deployed demo processes.
        </div>
        <a href="../../images/reference-process-short.png" target="_blank">
        <img src="../../images/reference-process-short.png" width="100%" /> </a>
        
        <img src="../../images/exampleProcess.png" />
    </div>
</body>
</html>

