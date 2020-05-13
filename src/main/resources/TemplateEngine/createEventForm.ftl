<h2>Create Event</h2>
<p id="status"></p>
<form action="" method="POST" role="form">
    <div class="form-group">
        <label for="ename">Event Name</label>
        <input type="text" class="form-control" id="ename" name="ename" placeholder="Enter Event Name">
    </div>
    <div class="form-group">
        <label for="etime">Enter Event Time</label>
        <input type="date" class="form-control" id="etime" name="etime">
    </div>
    <div class="form-group">
        <label for="location">Enter the Event Location</label>
        <input type="text" class="form-control" id="location" name="location">
    </div>
    <div class="form-group">
        <label for="gname">Enter the Group where the event happens</label>
        <select type="text" class="form-control" id="gname" name="gname" placeholder="Enter Group Name">
            <option value="">-- Select Group --</option>
        </select>
    </div>
    <button type="submit" class="btn btn-default">Submit</button>
</form>


<script>
    $.getJSON('/getGroups', function (json) {
        $.each(json, function (index, value) {
            // APPEND OR INSERT DATA TO SELECT ELEMENT.
            $('#gname').append('<option value="' + value.gname + '">' + value.gname + '</option>');
        });
    });
</script>

<!-- Simple JS Function to convert the data into JSON and Pass it as ajax Call -->
<script>
    $(function() {
        $('form').submit(function(e) {
            e.preventDefault();
            var this_ = $(this);
            var array = this_.serializeArray();
            var json = {};

            $.each(array, function() {
                json[this.name] = this.value || '';
            });
            json = JSON.stringify(json);

            // Ajax Call
            $.ajax({
                type: "POST",
                url: "createEvent",
                data: json,
                dataType: "json",
                success : function() {
                    $("#status").text("Event SuccesFully Added");
                    this_.find('input,select').val('');
                },
                error : function(e) {
                    console.log(e.responseText);
                    $("#status").text(e.responseText);
                }
            });
            $("html, body").animate({ scrollTop: 0 }, "slow");
            return false;
        });
    });

</script>