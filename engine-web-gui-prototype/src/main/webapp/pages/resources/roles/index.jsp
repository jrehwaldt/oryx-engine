<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
    <head>
        <script type="text/javascript" src="/javascripts/resources/roles.js"></script>
        
        <title>JodaEngine | Resources: Roles</title>
    </head>
    <body>
        <h1>Resources: Roles</h1>
        
        <h2>Overview</h2>
        <table id="roles-overview" width="100%">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Controls</th>
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
        
    </body>
    </html>
