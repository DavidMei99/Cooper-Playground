 <style>
 table th a {
 	text-transform: capitalize;
 }
 </style>

   <div class="starter-template">
    	<h2> My Groups </h2>
    	<div class="groupTable"> </div>
		<div class="paginationContainer "></div>
    </div>	
 	<script src="js/awesomeTable.js" type="text/javascript"></script>
 	<script>
 		$( document ).ready(function() {
 			$.getJSON('/getGroups',function(json){
    			if ( json.length == 0 ) {
        			console.log("NO DATA!");
        			$(".groupTable").text("No Groups Found");
    			}
    			else {
    				var tbl = new awesomeTableJs({
						data:json,
						tableWrapper:".groupTable",
						paginationWrapper:".paginationContainer",
						buildPageSize: false,
						buildSearch: false,
					});
					tbl.createTable();	
    			}
			});
 			
		});
	
	</script>