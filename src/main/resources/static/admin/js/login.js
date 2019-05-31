$(function () {
    $('#close,#clickto-register').click(function (e) {
        e.preventDefault();  //阻止默认单击事件
        $('#popup').toggleClass('show');
    });

    form_validate();
})

function form_validate() {
    $("#login-form").validate({
        rules: {
            adminname: {
                required: true,
                rangelength: [11, 11]
            },
            password: {
                required: true,
                minlength: 8
            }
        },
        messages: {
            adminname: {
                required: "账号不能为空",
                rangelength: "请输入正确的手机号"
            },
            password: {
                required: "密码不能为空",
                minlength: "密码长度必须大于8个字符"
            }
        }
    });

    $("#regist-form").validate({
        rules: {
            adminname: {
                required: true,
                rangelength: [11, 11],
                remote: {
                    url: "/admin/adminexists",
                    type: "post",
                    dataType: "json",
                    data: {
                        adminname: function () {
                            return $("#adminname").val();
                        }
                    }
                }
            },
            nickname: {
                required: true,
                maxlength: 20,
                remote: {
                    url: "/admin/nicknameexists",
                    type: "post",
                    dataType: "json",
                    data: {
                        nickname: function () {
                            return $("#nickname").val();
                        }
                    }
                }
            },
            password: {
                required: true,
                minlength: 8
            },
            valicode: {
                required: true,
            }
        },
        messages: {
            adminname: {
                required: "账号不能为空",
                rangelength: "请输入正确的手机号",
                remote: "用户已存在，请直接登录"
            },
            nickname: {
                required: "昵称不能为空",
                maxlength: "昵称长度不能超过20",
                remote: "昵称已存在"
            },
            password: {
                required: "密码不能为空",
                minlength: "密码长度必须大于8个字符"
            },
            valicode: {
                required: "验证码不能为空",
            }
        }
    });
}