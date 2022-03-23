$(function (){
    $(document).keydown(function(objEvent){
        if (objEvent.keyCode == 13 ){
            $("#btnSubmit").click();
        }
    });
});