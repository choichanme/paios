var header = $("meta[name='_csrf_header']").attr("content");
var token = $("meta[name='_csrf']").attr("content");

$('.btn_style01').click(function () {

    if($('#company').hasClass('input_alert')) {
        $('#company').removeClass('input_alert');
        $('#companyvalid').remove();
    }
    if($('#address').hasClass('input_alert')) {
        $('#address').removeClass('input_alert');
        $('#addressvalid').remove();
    }
    if($('#cellnum').hasClass('input_alert')) {
        $('#cellnum').removeClass('input_alert');
        $('#cellnumvalid').remove();
    }
    if($('#email').hasClass('input_alert')) {
        $('#email').removeClass('input_alert');
        $('#emailvalid').remove();
    }
    if($('#ceoname').hasClass('input_alert')) {
        $('#ceoname').removeClass('input_alert');
        $('#ceonamevalid').remove();
    }
    if($('#name').hasClass('input_alert')) {
        $('#name').removeClass('input_alert');
        $('#namevalid').remove();
    }
    if($('#mngnum').hasClass('input_alert')) {
        $('#mngnum').removeClass('input_alert');
        $('#mngnumvalid').remove();
    }

    if($('#company').val() == '') {
        $('#company').addClass('input_alert');
        $('#companyvalid').addClass('input_alert');
        $('#companyvalid').text('Please Insert Company Name');
        $('#company').focus();
        return false;
    }
    if($('#address').val() == '') {
        $('#address').addClass('input_alert');
        $('#addressvalid').addClass('input_alert');
        $('#addressvalid').text('Please Insert Company Address');
        $('#address').focus();
        return false;
    }
    if($('#cellnum').val() == '') {
        $('#cellnum').addClass('input_alert');
        $('#cellnumvalid').addClass('input_alert');
        $('#cellnumvalid').text('Please Insert Company Phone Number');
        $('#cellnum').focus();
        return false;
    }
    if($('#email').val() == '') {
        $('#email').addClass('input_alert');
        $('#emailvalid').addClass('input_alert');
        $('#emailvalid').text('Please Insert Email');
        $('#email').focus();
        return false;
    }
    if($('#ceoname').val() == '') {
        $('#ceoname').addClass('input_alert');
        $('#ceonamevalid').addClass('input_alert');
        $('#ceonamevalid').text('Please Insert CEO Name');
        $('#ceoname').focus();
        return false;
    }
    if($('#name').val() == '') {
        $('#name').addClass('input_alert');
        $('#namevalid').addClass('input_alert');
        $('#namevalid').text('Please Insert Name');
        $('#name').focus();
        return false;
    }
    if($('#mngnum').val() == '') {
        $('#mngnum').addClass('input_alert');
        $('#mngnumvalid').addClass('input_alert');
        $('#mngnumvalid').text('Please Insert Contact Point');
        $('#mngnum').focus();
        return false;
    }
    if($('#pdf').val() == "" || $('#pdf').val() == null) {
        alert("Pdf파일을 등록해주세요.");
        return false;
    }

    var emailRule = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;

    if(!emailRule.test($('#email').val())){
        alert("Wrong Email.")
        $('#email').focus();
        return false;
    }
    var comInfo = {
        company : $('#company').val(),
        address : $('#address').val(),
        website : $('#website').val(),
        cellnum : $('#cellnum').val(),
        email : $('#email').val(),
        ceoname : $('#ceoname').val(),
        name : $('#name').val(),
        position : $('#position').val(),
        mngnum : $('#mngnum').val()
    }

    var form = new FormData();
    console.log($("#pdf")[0].files[0]);
    form.append("pdf", $("#pdf")[0].files[0]);
    form.append("rfqQno", $('#rfqno').val());

    $.ajax({
        url: "/buyer/uploadRfqPdf"
        , method : 'POST'
        , processData: false
        , enctype : 'multipart/form-data'
        , contentType: false
        , data: form
        , beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);	// 헤드의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
        }
        , success: function (response) {
            $.ajax({
                url : "/user/savecompany",
                async : false, // 비동기모드 : true, 동기식모드 : false
                cache : false,
                dataType : 'text',
                data : comInfo,
                success : function(data) {
                    alert("rfq등록이 완료되었습니다.");
                    location.href = "/buyer/allRfq"
                },
                error : function(request,status,error) {
                    alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
                }
            });
        }
        , error: function (request,status,error) {
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
        }
    });

})