<style>
    table th a {
        text-transform: capitalize;
    }
</style>

<div class="starter-template">
    <h2> My Events </h2>
    <div class="eventTable"> </div>
    <div class="paginationContainer "></div>
</div>
<script src="js/awesomeTable.js" type="text/javascript"></script>
<script>
    $( document ).ready(function() {
        $.getJSON('/getEvents',function(json){
            if ( json.length == 0 ) {
                console.log("NO DATA!");
                $(".groupTable").text("No Events Found");
            }
            else {
                var tbl = new awesomeTableJs({
                    data:json,
                    tableWrapper:".eventTable",
                    paginationWrapper:".paginationContainer",
                    buildPageSize: false,
                    buildSearch: false,
                });
                tbl.createTable();
            }
        });

    });

</script>