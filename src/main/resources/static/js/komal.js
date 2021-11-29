$(document).ready(function() {

  // 파일업로드
  var inputs = document.querySelectorAll('.file_input')
  for (var i = 0, len = inputs.length; i < len; i++) {
    customInput(inputs[i])
  }

  function customInput (el) {
    const fileInput = el.querySelector('[type="file"]')
    const label = el.querySelector('[data-js-label]')

    fileInput.onchange =
        fileInput.onmouseout = function () {
          if (!fileInput.value) return

          var value = fileInput.value.replace(/^.*[\\\/]/, '')
          el.className += ' -chosen'
          label.innerText = value
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

  // // datepicker
  // $(".datepicker").datepicker();
  // $.datepicker.setDefaults({
  //   dateFormat: 'dd-mm-yy'
  // });


  // 모달창
  $(function() {
    $('.modal_trigger').on('click', function() {
      let modalTarget = $(this).data('modal-link');
      let modal = document.querySelector('.' + modalTarget);
      $(modal).toggleClass('is-show');
    });
    $('.modal .close').on('click',function() {
      $(this).parents('.modal').toggleClass('is-show');
    });
    $('.modal_bg').on('click', function() {
      $(this).parents('.modal').toggleClass('is-show');
    });
  });

  // 헤더
  $('.menu02').click(function(e) {
    e.stopPropagation();
    $(this).toggleClass('active');
  });
  $(document).click(function() {
    $('.menu02').removeClass('active');
  });

  $(window).scroll(function() {
    if ($(window).scrollTop() > 100) {
      $('.header').addClass('scroll');
    } else {
      $('.header').removeClass('scroll');
    }
  });


  // 메인
  if(location.href.indexOf('/') > -1){
    // section01
    const section01_swiper = new Swiper('.main .section01 .swiper', {
      // Optional parameters
      loop: true,

      // If we need pagination
      pagination: {
        el: '.section01 .swiper-pagination',
      },
    });

    // section02
    $('.section02 .num').each(function() {
      var $this = $(this),
        countTo = $this.attr('data-count');

      $({
        countNum: $this.text()
      }).animate({
        countNum: countTo
      }, {
        duration: 2000,
        easing: 'linear',
        step: function() {
          $this.text(Math.floor(this.countNum));
        },
        complete: function() {
          $this.text(this.countNum);
        }
      });
    });

    //section05
    var typelist = [
      {
        num: "1",
        txt: "견적내기"
      },
      {
        num: "2",
        txt: "견적피드백"
      },
      {
        num: "3",
        txt: "배송"
      },
      {
        num: "4",
        txt: "결제"
      }
    ];

    var section05_swiper = new Swiper('.main .section05 .swiper', {
      centeredSlides: true,
      slidesPerView: 1,
      pagination: {
        el: ".section05 .swiper-pagination",
        clickable: true,
        renderBullet: function(index, className) {
          return (
              '<div class="' + className + '">' +
              '<span class="step_num">' +
              typelist[index].num + '</span> <span class="step_txt">' + typelist[index].txt + "</span></div>"
          );
        },
      },
    });

    $('.main .section05 .swiper-pagination-bullet').click(function(){
      $(this).nextAll().removeClass('history');
      $(this).prevAll().addClass('history');
    });

    //section08
    $('.accordion_tit').click(function() {
      $('.accordion_tit').removeClass('active');
      $('.accordion_wrap').removeClass('active');
      $(this).toggleClass('active');
      $(this).parent().toggleClass('active');
    });
  }

  // 체크wrap
  $('.chk_wrap .chk_item input.chk_item_input').click(function() {
    $('.chk_wrap .chk_item').removeClass('chk');
    $('.chk_wrap .chk_item input.chk_item_input').attr('checked', false);
    $(this).parent().addClass('chk');
    $(this).attr('checked', true);
  });
  $('.supplier_cate_wrap .cate_toggle_btn').click(function(){
    $('.cate_toggle_cont').toggleClass('open');
    $(this).toggleClass('active');
  });


  // 회원가입
  $('.buyer_rfq .chk_item').click(function() {
    $('.buyer_rfq .chk_item').removeClass('chk');
    $(this).addClass('chk');
  });


  // 마이페이지
  $('.mypage .company_chg_btn').click(function() {
    $('.mypage').removeClass('pw_chg_open');
    $('.mypage').addClass('company_chg_open');
    $('.mypage .mypage_wrap button').attr('disabled', 'true');
  });

  $('.mypage .pw_chg_btn').click(function() {
    $('.mypage').removeClass('company_chg_open');
    $('.mypage').addClass('pw_chg_open');
    $('.mypage .mypage_wrap button').attr('disabled', 'true');
  });

  $('.mypage .cancel').click(function() {
    $('.mypage').removeClass('company_chg_open');
    $('.mypage').removeClass('pw_chg_open');
    $('.mypage .mypage_wrap button').removeAttr('disabled');
  });

  $('.mypage .btn_close').click(function() {
    $('.mypage').removeClass('company_chg_open');
    $('.mypage').removeClass('pw_chg_open');
    $('.mypage .mypage_wrap button').removeAttr('disabled');
  });

});
