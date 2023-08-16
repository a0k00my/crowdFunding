console.log("Script file");

const toggleSidebar = () => {

    if($('.sidebar').is(":visible")){
        $(".sidebar").css("display","none");
        $(".content").css("margin-left","0%");
    }else{
        $(".sidebar").css("display","block");
        $(".content").css("margin-left","20%");
    }
}

function deleteProject(id){
    swal({
      title: "Are you sure?",
      text: "Once deleted, you will not be able to recover this project!",
      icon: "warning",
      buttons: true,
      dangerMode: true,
    })
    .then((willDelete) => {
      if (willDelete) {
//        window.location="/user/delete/"+id;
        $.ajax({
                url: "/user/delete/" + id,
                type: "DELETE",
                success: function(response) {
                   window.location="/user/project/0";
                },
                error: function(error) {
                   window.location="/user/project/0";
                }
            });
      } else {
        swal("Your project is safe!");
      }
    });
}