var rfqGrid;
var rfqView;
var rfqColums;
var dat = new wijmo.input.InputDate("#deadline");
var sdat = new wijmo.input.InputDate("#shipdate", {
    valueChanged: () => calDday()
});

function loadGridKomalRfqList(type, result) {
    if(type == "init"){
        rfqView = new wijmo.collections.CollectionView(result, {
            trackChanges : true,
            calculatedFields :{
                extendedPrice : ($) => $.qty * $.uPrice            }
        });

        rfqColums = [
            { binding: 'itemName', header: 'Item',  width: 150 , align:"center"},
            { binding: 'description', header: 'Description',  width: 150 , align:"center" },
            { binding: 'qty', header: 'QTY',  width: 80 , align:"center" },
            { binding: 'amount', header: 'Unit',  width: 150 , align:"center" },
            { binding: 'uPrice', header: 'U/PRICE',  width: 150 , align:"center" },
            { binding: 'extendedPrice', header: 'Extended Price', width: 150 , align:"center", aggregate: 'Sum', isReadOnly:true },
            { binding: 'remark', header: 'REMARK', width: 100, align:"center"},
        ];

        rfqGrid = new wijmo.grid.FlexGrid('#rfqGrid', {
            autoGenerateColumns: false,
            alternatingRowStep: 0,
            columns: rfqColums,
            itemsSource: rfqView,
            keyActionEnter: "MoveDown",
            imeEnabled: true,
            allowAddNew : true,
            allowDelete: true,
            newRowAtTop : true,
            scrollPositionChanged: function(){
                rfqGrid.select(new wijmo.grid.CellRange(rfqGrid.rows.length - 1,-1), true);
            },
            // loadedRows: function(s, e) {
            //     s.autoSizeColumns();
            // },
            // cellEditEnded: function(s, e) {
            //     s.autoSizeColumn(e.col);
            // },
            // rowEditEnded: function(s, e) {
            //     s.autoSizeColumns();
            // },
            cellEditEnding:function (s,e) {
                var col = s.columns[e.col];
                if(col.binding == 'qty') {
                    var value = wijmo.changeType(s.activeEditor.value, wijmo.DataType.Number, col.format);
                    if(!wijmo.isNumber(value)){
                        e.cancel = true;
                        e.stayInEditMode = false;
                        alert('숫자로만 입력 가능합니다.');
                        return false;
                    }
                }else if(col.binding == 'uPrice') {
                    var value = wijmo.changeType(s.activeEditor.value, wijmo.DataType.Number, col.format);
                    if(!wijmo.isNumber(value)){
                        e.cancel = true;
                        e.stayInEditMode = false;
                        alert('숫자로만 입력 가능합니다.');
                        return false;
                    }
                }
            },
            itemFormatter: function(p, r, c, cell) {
                if (p.cellType == wijmo.grid.CellType.RowHeader) {
                    //cell.textContent = (r+1).toString();
                    cell.textContent = (rfqView.pageSize * rfqView.pageIndex + r + 1).toString();
                }
            }
        });

        rfqGrid.columnFooters.rows.push(new wijmo.grid.GroupRow());
        rfqGrid.columnFooters.setCellData(0,4,'TOTAL');
        rfqGrid.mergeManager = new FooterMergeManager(rfqGrid);
    }else{
        rfqView = new wijmo.collections.CollectionView(result, {
            trackChanges: true,
            currentItem: null
        });
        rfqGrid.itemsSource = rfqView;
    }
}

class FooterMergeManager extends wijmo.grid.MergeManager {
    constructor(grid) {
        super(grid);
    }

    getMergedRange(panel, row, col, clip) {
        if (panel.cellType !== wijmo.grid.CellType.ColumnFooter) {
            return super.getMergedRange(panel, row, col, clip);
        }
        var rg = new wijmo.grid.CellRange(row, col);
        // expand left/right
        for (var i = rg.col; i < panel.columns.length - 1; i++) {
            if (
                panel.getCellData(rg.row, i, true) !=
                panel.getCellData(rg.row, i + 1, true)
            )
                break;
            rg.col2 = i + 1;
        }
        for (var i = rg.col; i > 0; i--) {
            if (
                panel.getCellData(rg.row, i, true) !=
                panel.getCellData(rg.row, i - 1, true)
            )
                break;
            rg.col = i - 1;
        }
        return rg;
    }
}

//현재날짜 입력
function CurDateInit() {
    var now = new Date();
    var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "June",
        "July", "Aug", "Sep", "Oct", "Nov", "Dec"
    ];


    var nYear = now.getFullYear();
    var nMonth = now.getMonth()+1;
    var nDay = now.getDate();

    if(nMonth < 10)
        nMonth = "0" + nMonth;
    if(nDay < 10)
        nDay = "0" + nDay;

    dat.isRequired = false;
    dat.format = "dd-MM-yyyy";

    sdat.isRequired = false;
    sdat.format = "dd-MM-yyyy";

    $('#today').val(nDay + "th," + monthNames[nMonth] + " " + nYear);
}

function calDday() {
    var now = new Date();
    let dday = new Date(wijmo.Globalize.format(sdat.value, "R"));
    var gap = dday.getTime() - now.getTime();
    var result = Math.ceil(gap/ (1000 * 60 * 60 * 24));
    $('#dday').val(result);
}

$('#payterm').change(function () {
    if($(this).val() == 'B' || $(this).val() == 'D'){
        $('#addTerm').show();
    }else {
        $('#addTerm').hide();
        $('#addTerm').val("");
    }
})

$('.btn_style01').click(function () {
    if($('#qutation').hasClass('input_alert')) {
        $('#qutation').removeClass('input_alert');
        $('#qutationvalid').remove();
    }
    if($('#location').hasClass('input_alert')) {
        $('#location').removeClass('input_alert');
        $('#locationvalid').remove();
    }
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


    if($('#qutation').val() == '') {
        $('#qutation').addClass('input_alert');
        $('#qutationvalid').addClass('input_alert');
        $('#qutationvalid').text('Please Insert Re Quotation of');
        $('#qutation').focus();
        return false;
    }
    if($('#location').val() == '') {
        $('#location').addClass('input_alert');
        $('#locationvalid').addClass('input_alert');
        $('#locationvalid').text('Please Insert Delivery Location');
        $('#location').focus();
        return false;
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

    var emailRule = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;

    if(!emailRule.test($('#email').val())){
        alert("Wrong Email.")
        $('#email').focus();
        return false;
    }

    if(rfqGrid.rows.length == 1) {
        alert("세부 아이템을 등록해주세요.")
        rfqGrid.select(new wijmo.grid.CellRange(rfqGrid.rows.length -1, 1), true);
        return false;
    }

    //추가행 validation
    for(var i=0; i<rfqView.itemsAdded.length; i++) {
        if(!validation(rfqView.itemsAdded[i])) {
            return false;
        }
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

    $.ajax({
        url : "/user/savecompany",
        async : false, // 비동기모드 : true, 동기식모드 : false
        cache : false,
        dataType : 'text',
        data : comInfo,
        success : function(data) {
        },
        error : function(request,status,error) {
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
        }
    });

    var rfqInfo = {
        today : $('#today').val(),
        rfqno : $('#rfqno').val(),
        rfqto : $('#rfqto').val(),
        attention : $('#attention').val(),
        qutation : $('#qutation').val(),
        deadline : wijmo.Globalize.format(dat.value,'yyyy-MM-dd'),
        incoterms : $('#incoterms').val(),
        payterm : $('#payterm').val(),
        addterm : $('#addterm').val(),
        currency : $('#currency').val(),
        location : $('#location').val(),
        shipdate : wijmo.Globalize.format(sdat.value,'yyyy-MM-dd'),
        dday : $('#dday').val()
    }

    $.ajax({
        url : "/buyer/saverfq",
        async : false, // 비동기모드 : true, 동기식모드 : false
        cache : false,
        dataType : 'text',
        data : rfqInfo,
        success : function(data) {
            saveRfq();
        },
        error : function(request,status,error) {
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
        }
    });
})

function saveRfq() {
    //추가행 저장
    for ( var i = 0; i <rfqView.itemsAdded.length; i ++) {
        console.debug("add:");
        console.debug(rfqView.itemsAdded[i]);

        var param = {
            rfqQno : $('#rfqno').val()
        }

        var params = Object.assign(rfqView.itemsAdded[i], param)

        $.ajax({
            url : "/buyer/saveRfqDetail",
            data : params,
            async: false,
            success : function(result) {
                alert("rfq등록이 완료되었습니다.");
                location.href = "/buyer/allRfq"
            },
            error: function(request, status, error) {
                alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            }
        });
    }
}

//그리드 validation
function validation(item) {
    if(item.itemName == null || wijmo.isEmpty(item.itemName) || item.itemName == ""){
        alert("[item 입력 해주세요.]");
        return false;
    }
    if(item.description == null || wijmo.isEmpty(item.description) || item.description == ""){
        alert("[description 입력 해주세요.]");
        return false;
    }
    if(item.qty == null || wijmo.isEmpty(item.qty) || item.qty == ""){
        alert("[qty 입력 해주세요.]");
        return false;
    }
    if(item.uPrice == null || wijmo.isEmpty(item.uPrice) || item.uPrice == ""){
        alert("[unit 입력 해주세요.]");
        return false;
    }if(item.amount == null || wijmo.isEmpty(item.amount) || item.amount == "") {
        alert("[price 입력 해주세요.]");
        return false;
    }

    return true;
}

var gridEvent = function(){
    //validation
    rfqView.getError = function(item, prop) {

        switch (prop) {
            case "itemName":
                if (item.itemName == null || wijmo.isEmpty(item.itemName) || item.itemName == "") {
                    return "[Item을 입력 해주세요.]";
                }
                return null;

            case "description":
                if (item.description == null || wijmo.isEmpty(item.description) || item.description == "") {
                    return "[Description을 입력 해주세요.]";
                }
                return null;

            case "qty":
                if (item.qty == null || wijmo.isEmpty(item.qty) || item.qty == "") {
                    return "[QTY을 입력 해주세요.]";
                }
                return null;

            case "amount":
                if (item.amount == null || wijmo.isEmpty(item.amount) || item.amount == "") {
                    return "[Unit을 입력 해주세요.]";
                }
                return null;

            case "uPrice":
                if (item.uPrice == null || wijmo.isEmpty(item.uPrice) || item.uPrice == "") {
                    return "[U/PRICE을 선택 해주세요.]";
                }
                return null;

        }
        return null;
    }
}


$(document.body).ready(function() {
    loadGridKomalRfqList('init');  	//그리드 초기화
    CurDateInit();
    $('#addTerm').hide();
    gridEvent();
});