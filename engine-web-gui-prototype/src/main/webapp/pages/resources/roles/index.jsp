<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
    <head>
        <script type="text/javascript" src="/javascripts/resources/resources-crud.js"></script>
        <script type="text/javascript" src="/javascripts/resources/roles.js"></script>
        
        <link rel="stylesheet" type="text/css" href="/stylesheets/jquery.ui/ui-lightness/jquery-ui-1.8.12.custom.css">
        
        <title>JodaEngine | Resources: Roles</title>
    </head>
    <body>
        <h1>Resources: Roles</h1>
        
        <h2>Overview</h2>
        <table id="roles-overview" width="100%">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Contains participants</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%-- content here --%>
            </tbody>
        </table>
        <a href="#" id="roles-overview-refresh">Refresh roles</a>
        
        <h2>Create a new Role</h2>
        <form action="" method="post" id="roles-create">
            Name: <input type="text" name="role-name" />
            <input type="submit" value="Create" />
        </form>
        
        <div class="dialog" id="roles-change-member-dialog" title="Change participant-to-role relationship">
            <form method="post" action="" id="roles-change-member">
                <div class="filter">
                    Filter participants: <input type="text" class="search-participants" />
                </div>
                <br />
                <div>
                    <select multiple="multiple" class="unassigned-participants changeset left"></select>
                    <div style="float: left;">
                        <input type="button" class="remove-participants" value="&lt;"/>
                        <br/>
                        <input type="button" class="add-participants" value="&gt;"/>
                    </div>
                    <select multiple="multiple" class="assigned-participants changeset right"></select>
                </div>
            </form>
        </div>
    </body>
    </html>
