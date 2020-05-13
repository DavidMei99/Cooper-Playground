<h2>Invite New Member</h2>
<p id="status"></p>
<form action="" method="POST" role="form">


    <div class="form-group">
        <label for="gname">Select the Group you want to invite member to</label>
        <select type="text" class="form-control" id="gname" name="gname" placeholder="Enter Group Name">
            <option value="">-- Select Group --</option>
        </select>
    </div>
    <div class="form-group">
        <label for="uname">Enter the User you want to invite</label>
        <input type="text" class="form-control" id="uname" name="uname" placeholder="Enter User Name">
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
                url: "inviteGroup",
                data: json,
                dataType: "json",
                success : function() {
                    $("#status").text("Group SuccesFully Added");
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