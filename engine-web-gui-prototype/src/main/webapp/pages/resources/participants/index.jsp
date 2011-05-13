<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
        <script type="text/javascript" src="/javascripts/resources/resources-crud.js"></script>
        <script type="text/javascript" src="/javascripts/resources/participants.js"></script>
        
        <title>JodaEngine | Resources: Participants</title>
    </head>
    <body>
        <h1>Resources: Participants</h1>
        
        <h2>Overview</h2>
        <table id="participants-overview" width="100%">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Belongs to role</th>
                    <th>Controls</th>
                </tr>
            </thead>
            <tbody>
                <%-- content here --%>
            </tbody>
        </table>
        <a href="#" id="participants-overview-refresh">Refresh participants</a>
        
        <h2>Create a new Participant</h2>
        <form action="" method="post" id="participants-create">
            Name: <input type="text" name="participant-name" />
            <input type="submit" value="Create" />
        </form>
        
    </body>
</html>
