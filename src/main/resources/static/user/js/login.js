/**
 * 弹出层打开关闭
 */
$(function(){
    $('#close,#clickto-register').click(function(e){
        e.preventDefault();  //阻止默认单击事件
        $('#popup').toggleClass('show');
    });

    getValicodeImg();
    form_validate();
})

function getValicodeImg() {
    var valicodeImg = document.querySelector("#vali-code-img");
    valicodeImg.src = "/user/valicode?" + new Date();
    valicodeImg.addEventListener("click",function (e) {
        valicodeImg.src = "/user/valicode?" + new Date();
    });
}

function form_validate(){
    $("#login-form").validate({
        rules:{
            username:{
                required:true,
                rangelength:[11,11]
            },
            password:{
                required:true,
                minlength:8
            }
        },
        messages:{
            username:{
                required:"账号不能为空",
                rangelength:"请输入正确的手机号"
            },
            password:{
                required: "密码不能为空",
                minlength: "密码长度必须大于8个字符"
            }
        }
    });

    $("#regist-form").validate({
        rules:{
            username:{
                required:true,
                rangelength:[11,11],
                remote:{
                    url: PRO_NAME + "/user/userexists",
                    type: "post",
                    dataType: "json",
                    data: {
                        username: function () {
                            return $("#username").val();
                        }
                    }
                }
            },
            nickname:{
                required:true,
                maxlength:20,
                remote:{
                    url: PRO_NAME + "/user/nicknameexists",
                    type: "post",
                    dataType: "json",
                    data: {
                        username: function () {
                            return $("#nickname").val();
                        }
                    }
                }
            },
            password:{
                required:true,
                minlength:8
            },
            valicode:{
                required:true,
                remote:{
                    url: PRO_NAME + "/user/checkValicode",
                    type: "post",
                    dataType: "json",
                    data: {
                        valicode: function () {
                            return $("#vali-code").val();
                        }
                    }
                }
            }
        },
        messages:{
            username:{
                required:"账号不能为空",
                rangelength:"请输入正确的手机号",
                remote: "用户已存在，请直接登录"
            },
            nickname:{
                required:"昵称不能为空",
                maxlength:"昵称长度不能超过20",
                remote: "昵称已存在"
            },
            password:{
                required: "密码不能为空",
                minlength: "密码长度必须大于8个字符"
            },
            valicode:{
                required:"验证码不能为空",
                remote:"输入验证码错误"
            }
        }
    });
}