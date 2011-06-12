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
        <table id="interrupted-instances-overview" class="interrupted-instances-overview" width="100%">
            <thead>
                <tr>
                    <th class="loading-data">Interrupted Instance</th>
                    <th class="loading-data">Matching Breakpoint</th>
                    <th class="loading-data">Instance Context Data</th>
                    <th class="loading-data">Controls</th>
                </tr>
            </thead>
            <tbody></tbody>
        </table>
        
        <a href="#" id="interrupted-instances-overview-refresh">Refresh table</a>
        
        <!-- Successful dialog -->
        <div class="dialog" id="control-successful-dialog" title="Interrupted instance released">
            Instance successfully <span class="commandResult"></span>.
        </div>
        
        <!-- Controls dialog -->
        <div class="dialog" id="control-process-dialog" title="Release interrupted instance">
            <a href="#" class="control-instance" control-type="continue" title="The interrupted instance will be released and continue until the next breakpoint matches.">Continue</a>
            <a href="#" class="control-instance" control-type="step-over" title="The interrupted instance will be released and continue to the next decision point.">Step Over</a>
            <a href="#" class="control-instance" control-type="resume" title="The interrupted instance will be released and continue to the process\' end.">Resume</a>
            <a href="#" class="control-instance" control-type="terminate" title="The interrupted instance will be terminated.">Terminate</a>
        </div>
        
        <!-- Full SVG Artifact Overlay -->
        <div class="overlay" id="svg-artifact-full-overlay">
            <div class="full-svg-artifact"></div>
        </div>
    </body>
</html>
