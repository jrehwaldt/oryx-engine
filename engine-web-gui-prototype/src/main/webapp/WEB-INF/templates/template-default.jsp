<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="de.hpi.oryxengine.web.NavigationEntry"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String current_user = null;

String class_for_selected_item = null;
String main_path = null;
String[] path = null;
String uri = request.getRequestURI();
if (uri != null) {
    path = uri.replaceAll("/pages/", "").split("/");
    
    if (path.length > 0 && !"".equals(path[0]) && !path[0].endsWith(".jsp")) {
        main_path = path[0].toLowerCase();
        class_for_selected_item = String.format("item_%s", main_path);
    }
}

Map<String, NavigationEntry[]> navigation = new HashMap<String, NavigationEntry[]>();
navigation.put("dashboard", new NavigationEntry[] {});
navigation.put("management", new NavigationEntry[] {
    new NavigationEntry("instances", "Process Instances"),
    new NavigationEntry("definitions", "Process Definitions"),
    new NavigationEntry("examples", "Examples")
});
navigation.put("worklist", new NavigationEntry[] {});
navigation.put("resources", new NavigationEntry[] {
    new NavigationEntry("participants", "Participants"),
    new NavigationEntry("roles", "Roles")
});
navigation.put("settings", new NavigationEntry[] {});
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <link rel="SHORTCUT ICON" href="/images/favicon/favicon.ico" type="image/x-icon">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <link rel="stylesheet" type="text/css" href="/stylesheets/layout.css">
        <link rel="stylesheet" type="text/css" href="/stylesheets/form.css">
        
        <%--begin-development-only--%>
        <link rel="stylesheet" type="text/css" href="/stylesheets/dev/fauxconsole.css">
        <script type="text/javascript" src="/javascripts/dev/fauxconsole.js"></script>
        <%--end-development-only--%>
        
        <script type="text/javascript" src="/javascripts/lib/jquery-1.6.js"></script>
        <script type="text/javascript" src="/javascripts/lib/jquery.Storage.js"></script>
        <script type="text/javascript" src="/javascripts/lib/jquery.getQueryParam.1.0.0.min.js"></script>
        <script type="text/javascript" src="/javascripts/lib/jquery.form.js"></script>
        <script type="text/javascript" src="/javascripts/lib/jquery.selectboxes.js"></script>
        
        <script type="text/javascript" src="/javascripts/lib/jquery.ui.core.js"></script>
        <script type="text/javascript" src="/javascripts/lib/jquery.ui.position.js"></script>
        <script type="text/javascript" src="/javascripts/lib/jquery.ui.widget.js"></script>
        <script type="text/javascript" src="/javascripts/lib/jquery.ui.dialog.js"></script>
        
        <title><decorator:title default="JodaEngine" /></title>
        <decorator:head />
        
    </head>
    <body>

        <div id="spacer-top">
        </div>
        <div class="colmask threecol">
          <div id="cheater">
          </div>
          <div class="colmid">
            <div class="colleft">
              <div class="col1">
                <div id="header">
                  <div id="logo">
                    <a href="/" alt="JodaEngine home">
                        <img src="/images/jodaengine.png" height="69" width="185" border="0" alt="Joda Engine home" />
                    </a>
                  </div>
                  <div id="menu-level-1">
                    <!--begin nav-level-1-->
                    <ul>
                        <li><a class="dashboard" href="/dashboard">Dashboard</a></li>
                        <li><a class="management" href="/management">Engine Management</a></li>
                        <li><a class="worklist" href="/worklist">Worklist Management</a></li>
                        <!--<li><a class="reports" href="/reports">Reports</a></li>-->
                        <li><a class="resources" href="/resources">Resource Management</a></li>
                        <li><a class="settings" href="/settings">Settings</a></li>
                    </ul>
                    <!--end nav-level-1-->
                  </div>
                  <% if (class_for_selected_item != null) { %>
                    <div id="tab-level-1" class="<%= class_for_selected_item %>">
                    </div>
                  <% } %>
                </div>
                <div id="main-container">
                  <div id="main-frame-top">
                    <div id="menu-level-2">
                      <!--begin nav-level-2-->
                      <% if (navigation.containsKey(main_path)) { %>
                        <ul>
                          <% for (NavigationEntry entry: navigation.get(main_path)) { %>
                            <li><a class="<%=entry.getPath() %>" href="/<%=main_path %>/<%=entry.getPath() %>"><%=entry.getName() %></a></li>
                          <% } %>
                        </ul>
                      <% } %>
                      <!--end nav-level-2-->
                    </div>
                    <div id="logged-in-statement">
                      <% if (current_user != null) { %>
                        <span class="name"><%= current_user %></span>
                        <a href="/logout">Logout</a>
                      <% } else { %>
                        <a href="/">Login</a>
                      <% } %>
                    </div>
                  </div>
                  <div id="whitespace">
                <%--
                    <% unless SimpleNavigation.active_item_container_for(3).blank? %>
                      <div id="navi-level-3">
                        <div id="navi-level-3-content" class="box-shadow">
                          <!--begin nav-level-3-->
                          <ul>
                            <li><a class="ex1" href="/ex1">Example 1</a></li>
                            <li><a class="ex2" href="/ex2">Example 2</a></li>
                          </ul>
                          <!--end nav-level-3-->
                        </div>
                      </div>
                    <% end %>
                --%>
                    <div id="main-content">
                      <%-- leave #notice empty (no spaces even), if it should not be displayed --%>
                      <%-- possible status types are: success, error, and, warning --%>
                      <div id="notice" class=""></div>
                <%--
                      <% flash.each do |key, msg| %>
                        <% unless key == :product_params %>
                          <%= content_tag :div, msg, :id => 'notice', :class => key %>
                        <% end %>
                      <% end %>
                --%>
                      <!--begin content-->
                      <decorator:body />
                      <!--end content-->
                    </div>
                  </div>
                  <div id ="border-bottom">
                    <div id="frame-left-bottom"></div>
                    <div id="frame-right-bottom"></div>
                  </div>
                </div>
              </div>
              <div class="col2">
                <img src="/images/layout/frame-left-top.png" height="705" width="18" border="0" />
              </div>
              <div class="col3">
                <img src="/images/layout/frame-right-top.png" height="705" width="17" border="0" />
              </div>
            </div>
          </div>
        </div>
        <div id="footer">
          <div id="footer-extender-left"></div>
          <div id="footer-extender-right"></div>
        </div>


    </body>
</html>

