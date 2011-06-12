<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.jodaengine.node.activity.ActivityState"%>
<html>
    <head>
        <script type="text/javascript" src="/javascripts/lib/jquery.tools.1.2.5.min.js"></script>
        
        <script type="text/javascript" src="/javascripts/management/navigator-crud.js"></script>
        <script type="text/javascript" src="/javascripts/management/repository-crud.js"></script>
        
        <script type="text/javascript" src="/javascripts/debugger/debugger-helper.js"></script>
        <script type="text/javascript" src="/javascripts/debugger/svg-crud.js"></script>
        <script type="text/javascript" src="/javascripts/debugger/breakpoints-crud.js"></script>
        
        <script type="text/javascript" src="/javascripts/debugger/interrupted-instances-crud.js"></script>
        <script type="text/javascript" src="/javascripts/debugger/interrupted-instances.js"></script>
        
        <%-- optional --%>
        <script type="text/javascript" src="/javascripts/lib/jquery.timers-1.2.js"></script>
        
        
        <link rel="stylesheet" type="text/css" href="/stylesheets/jquery.tools/overlay.css">
        <link rel="stylesheet" type="text/css" href="/stylesheets/jquery.ui/ui-lightness/jquery-ui-1.8.12.custom.css">
        
        <title>JodaEngine | Debugger: Interrupted Instances Explorer</title>
    </head>
    <body>
        <h1>Debugger: Interrupted Instances Explorer</h1>
        
        <h2>Interrupted Instances</h2>
        <table id="definitions-overview" class="interrupted-instances-overview" width="100%">
            <thead>
                <tr>
                    <th class="loading-data">Process Definition</th>
                    <th class="loading-data">Breakpoints (SVG-view)</th>
                    <th class="loading-data">Breakpoints (Table-view)</th>
                </tr>
            </thead>
            <tbody></tbody>
        </table>
        
        <a href="#" id="definitions-overview-refresh">Refresh table</a>
        
        <div class="dialog" id="create-node-breakpoint-dialog" title="Create a node breakpoint">
            <form id="create-node-breakpoint" method="post" action="">
                
                <input type="hidden" name="breakpoint-node-id" value="" />
                <input type="hidden" name="breakpoint-definition-id" value="" />
                
                Activity state:
                <select name="breakpoint-activity-state">
                    <% for (ActivityState state: ActivityState.values()) { %>
                        <option value="<%=state.toString() %>"><%=state.toString() %></option>
                    <% } %>
                </select>
                <br />
                
                Condition:
                <input type="text" name="breakpoint-condition" value="" class="optional" />
                <br />
            </form>
        </div>
        
        <!-- Full SVG Artifact Overlay -->
        <div class="overlay" id="svg-artifact-full-overlay">
            <div class="full-svg-artifact"></div>
        </div>
    </body>
</html>
