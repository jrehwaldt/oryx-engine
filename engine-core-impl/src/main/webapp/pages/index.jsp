<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>Oryx Engine goes REST</title>
    </head>
    <body>
        <h1>REST-interface working ;-)</h1>
        <ul>
            <li>
                <h2>Navigator</h2>
                <ul>
                    <li><a href="api/navigator/statistic">Statistik</a></li>
                    <li><a href="api/navigator/start">Start the engine</a></li>
                    <li><a href="api/navigator/stop">Stop the engine</a></li>
                </ul>
             </li>
             <li>
                <h2>Worklist</h2>
                <ul>
                    <li><a href="api/worklist/todo">TODO</a></li>
                </ul>
             </li>
        </ul>

        <div id="result_1"></div>
		<p>Participants </p>
        <div id="participants"></div>
        <p> Names </p>
        <div id="participantnames"></div>
        <div id="result_3"></div>

        <div id="data"></div>

    </body>
</html>

