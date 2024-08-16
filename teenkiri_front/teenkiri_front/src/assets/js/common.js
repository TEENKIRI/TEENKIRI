$(function () {
	
	var swiper = new Swiper(".swiperLecture", {
		//effect: "fade",
		slidesPerView: 4,
		navigation: {
			nextEl: '.mainLecture .swiper-button-next',
			prevEl: '.mainLecture .swiper-button-prev',
		},
	});
	
	var swiper = new Swiper(".swiperLectureBest", {
		//effect: "fade",
		slidesPerView: 4,
		navigation: {
			nextEl: '.mainLectureBest .swiper-button-next',
			prevEl: '.mainLectureBest .swiper-button-prev',
		},
	});

	$(".keywordList li").click(function(){
		var text = $(this).text();
		$(".valchk").val(text);
		$(".modal_auto_sch").hide();
	});
	
	$(window).scroll(function () {
		if ($(this).scrollTop() > 100) {
			$('#top_btn').fadeIn();
		}else {
			$('#top_btn').fadeOut();
		}
	});  
    $("#top_btn").on("click", function() {
        $("html, body").animate({scrollTop:0}, '500');
        return false;
    });


});

