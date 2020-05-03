<html>
<head>
    <title>Cooper Playground</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="css/starter-template.css">
</head>
<body>

<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#"></a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="/loginSuccess">Home</a></li>
                <!--<li class="active"><a href="loginSuccess"></a></li>-->
                <li><a href="createGroup">Create Group</a></li>
                <li><a href="getMyGroup">My Groups</a></li>
                <li><a href="attendGroup">Attend Group</a></li>
                <li><a href="createEvent">Create Event</a></li>
                <li><a href="getMyEvent">My Events</a></li>
                <li><a href="attendEvent">Attend Event</a></li>
                <li class="active"><a href="/">Logout</a></li>

            </ul>
        </div><!--/.nav-collapse -->
    </div>
</div>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<div class="container">
    <#include "${templateName}">
</div>
</body>
</html>