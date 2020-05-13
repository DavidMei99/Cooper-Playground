<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">

    <title>Cooper Playground</title>

    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="css/starter-template.css">
    <link rel="stylesheet" href="css/navbar.css">
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
                <li class="active"><a href="/loginGroup">Group</a></li>
                <li><a href="createEvent">Create Event</a></li>
                <li><a href="getMyEvent">My Events</a></li>
                <li><a href="attendEvent">Attend Event</a></li>
                <li><a href="inviteEvent">Invite</a></li>
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