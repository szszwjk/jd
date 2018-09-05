var TT = TAOTAO = {
    checkLogin : function(){
        var _ticket = $.cookie("COOKIE_TOKEN_KEY");
        if(!_ticket){
            return ;
        }
        $.ajax({
            url : "http://localhost:8088/user/token/" + _ticket,
            dataType : "jsonp",
            type : "GET",
            success : function(data){
                if(data.status == 200){
                    console.log(data)
                    var userName = data.data.userName;
                    var html = userName + "，欢迎来到淘淘！<a href=\"http://www.taotao.com/user/logout.html\" class=\"link-logout\">[退出]</a>";
                    $("#loginbar").html(html);
                }
            }
        });
    }
}

$(function(){
    // 查看是否已经登录，如果已经登录查询登录信息
    TT.checkLogin();
});