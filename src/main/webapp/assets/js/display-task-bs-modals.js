$('#displayTaskModal').on('shown.bs.modal', function () {
	$('#displayClose').focus()
})

$('#displayTaskModal').on('show.bs.modal', function (event) {
	var button = $(event.relatedTarget)
	var modal = $(this)
	
	var id = button.data('id')
	var dataHolder = $('#' + id + '.data-holder')
	
	var status = dataHolder.data('status')
	modal.find('#displayStatus').attr('class', 'glyphicon glyphicon-' + Icon.for_(status))
	
	var name = dataHolder.data('name')
	modal.find('#displayName').text(name)
	
	var description = dataHolder.data('description')
	modal.find('#displayDescription').text(description == undefined ? '' : description)
	
	var dueDate = dataHolder.data('duedate')
	var dueDateInfo = dataHolder.data('duedateinfo')
	modal.find('#displayDueDate').text(dueDate == undefined ? '' : ('Due: ' + dueDate + ' (' + dueDateInfo + ')'))
	
	var datePostponed = dataHolder.data('datepostponed')
	modal.find('#displayDatePostponed').text(status === 'POSTPONED' ? ('Postponed: ' + datePostponed) : '')
	
	var dateFinished = dataHolder.data('datefinished')
	modal.find('#displayDateFinished').text(status === 'COMPLETED' || status === 'FAILED'
			? ((status === 'COMPLETED' ? 'Completed' : 'Failed') + ': ' + dateFinished) : '')
})

var Icon = {};
Icon.for_ = function(status) {
	switch (status) {
		case 'IN_PROGRESS': return 'play';
		case 'POSTPONED': return 'pause';
		case 'COMPLETED': return 'ok';
		case 'FAILED': return 'remove';
	}
}