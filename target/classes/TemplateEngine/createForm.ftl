<h2>Create User</h2>
   <p id="status"></p>
  <form action="" method="POST" role="form">
    <div class="form-group">
      <label for="uname">Username</label>
      <input type="text" class="form-control" id="uname" name="uname" placeholder="Enter Username">
    </div>
    <div class="form-group">
      <label for="pwd">Password</label>
      <input type="password" class="form-control" id="pwd" name="pwd">
    </div>
    <div class="form-group">
      <label for="email">Email</label>
      <input type="text" class="form-control" id="email" name="email">
    </div>
    <button type="submit" class="btn btn-default">Submit</button>
  </form>


<!-- Simple JS Function to convert the data into JSON and Pass it as ajax Call --!>
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
            url: "createUser",
            data: json,
            dataType: "json",
            success : function() {
                $("#status").text("User SuccesFully Added");
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