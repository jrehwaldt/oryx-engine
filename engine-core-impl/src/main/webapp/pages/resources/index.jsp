<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
    <head>
        <script type="text/javascript" src="/javascripts/demo/resources.js"></script>
    </head>
    <body>
        <h1>Resources</h1>
        
        <h2>Create Participants</h2>
        <form action="" method="post">
            Name of the new participant: <input type="text" id ="newParticipant" name="newParticipant"/>
            <input type="button" id="createParticipant" value="Create Participant"/>
        </form>
        
        <h2>Create Roles</h2>
        <form action="" method="post">
            Name of the new role: <input type="text" id ="newRole" name="newRole"/>
            <input type="button" id="createRole" value="Create Role"/>
        </form>
        
        <h2>Relate participants to roles</h2>
        <div>
            Select a role: <br><select id="roles"></select>
            <form method="get" action="">
                
                Search for participants: <input type="text" id="searchParticipant" name="searchParticipant"/>
                <div>
                    <div><select id="resultParticipants" multiple="multiple" style="float: left;"></select></div>
                    <div style="float: left;">
                        <input type="button" id="removeParticipant" value="&lt;"/>
                        <br/>
                        <input type="button" id="addParticipant" value="&gt;"/>
                    </div>
                    <div><select id="relatedParticipants" multiple="multiple" style="float: left;"></select></div>
                </div>
                <input type="button" id="submitChanges" value="Submit Changes"/>
            </form>
        </div>
        
    </body>
    </html>
