$(document).ready(function(){
    $('#contactForm').on('submit', function(e){
        e.preventDefault();
        e.stopPropagation();

        // get values from FORM
        var name = $("#name").val();
        var email = $("#email").val();
        var message = $("#message").val();
        var goodToGo = false;
        var messgaeError = 'Request can not be send';
        var pattern = new RegExp(/^(('[\w-\s]+')|([\w-]+(?:\.[\w-]+)*)|('[\w-\s]+')([\w-]+(?:\.[\w-]+)*))(@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$)|(@\[?((25[0-5]\.|2[0-4][0-9]\.|1[0-9]{2}\.|[0-9]{1,2}\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\]?$)/i);


        if (name && name.length > 0 && $.trim(name) !='' && message && message.length > 0 && $.trim(message) !='' && email && email.length > 0 && $.trim(email) !='') {
            if (pattern.test(email)) {
                goodToGo = true;
            } else {
                messgaeError = 'Please check your email address';
                goodToGo = false;
            }
        } else {
            messgaeError = 'You must fill all the form fields to proceed!';
            goodToGo = false;
        }


        if (goodToGo) {
            $.ajax({
                data: $('#contactForm').serialize(),
                beforeSend: function() {
                    $('#success').html('<div class="col-md-12 text-center"><img src="/static/images/spinner1.gif" alt="spinner" /></div>');
                },
                success:function(response){
                    if (response==1) {
                        $('#success').html('<div class="col-md-12 text-center">邮件发送成功</div>');
                        window.location.reload();
                    } else {
                        $('#success').html('<div class="col-md-12 text-center">邮件未发磅. 请重试!</div>');
                    }
                },
                error:function(e){
                    $('#success').html('<div class="col-md-12 text-center">无法从服务器获取数据. 请重试!</div>');
                },
                complete: function(done){
                    console.log('Finished');
                },
                type: 'POST',
                url: 'js/send_email.php',
            });
            return true;
        } else {
            $('#success').html('<div class="col-md-12 text-center">'+messgaeError+'</div>');
            return false;
        }
        return false;
    });
});