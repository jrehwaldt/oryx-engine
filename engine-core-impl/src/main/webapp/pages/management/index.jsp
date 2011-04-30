<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
      Process Definitions Explorer
      <script type="text/javascript" src="/javascripts/demo/engineManagement.js"></script>
    </head>
    <body>
      <div id="engineStatus">
        <h1>Process Engine Status </h1>
        <table id="runningInstances" style="width:100%;">
          <tr>
           <th>ID</th>
           <th>Name</th>
           <th>Description</th>
         </tr>
        </table>
      </div>
      <div id="definitionExplorer">
        <h1>Process Definition Explorer</h1>
        <table id="processDefinitionList" style="width:100%;">
          <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Action</th>
          </tr>
        </table>
		<h2>Demoprocess</h2>
		<img src="../../images/exampleProcess.png" />
      </div>
    </body>
</html>

