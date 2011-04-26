<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
      Process Definitions Explorer
      <script type="text/javascript" src="/javascripts/demo/repository.js"></script>
      <script type="text/javascript" src="/javascripts/demo/engineStatus.js"></script>
    </head>
    <body>
      <div id="engineStatus">
        <h1>Process Engine Status </h1>
        <table id="runningInstances">
          <tr>
           <th>ID</th>
           <th>Name</th>
           <th>Description</th>
           <th>Token Status</th>
         </tr>
        </table>
      </div>
      <div id="definitionExplorer">
        <h1>Process Definition Explorer</h1>
        <table id="processDefinitionList">
          <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Action</th>
          </tr>
        </table>
      </div>
    </body>
</html>

