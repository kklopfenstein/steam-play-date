$("#recomForm").submit(function (event) {
	event.preventDefault();
	var posting = $.post('/getRecommendations', $('#recomForm').serialize());
	$('#myModal').modal({backdrop: 'static', show: true});
	$('.carousel').carousel({
        interval: 10000
        })
	posting.done(function(data) { 
		$('#mainContainer').empty().append(data);
		$('#myModal').modal('hide');
	});
});
