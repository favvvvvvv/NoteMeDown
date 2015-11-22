$('#addEditModal').on('shown.bs.modal', function () {
	$('#groupName').focus()
})

$('#addEditModal').on('show.bs.modal', function (event) {
	var button = $(event.relatedTarget)
	var mode = button.data('mode')
	var modal = $(this)
	
	var title = modal.find('#addEditModalLabel')
	title.text(title.data(mode))
	
	var form = modal.find('#addEditForm')
	form.attr('action', form.data(mode))
	
	var submit = modal.find('#addEditSubmit')
	submit.text(submit.data(mode))
	
	if (mode === 'edit') {
		var id = button.data('id')
		form.attr('action', form.attr('action').replace(/\d+/, id))
		
		var name = button.data('name')
		form.find('#groupName').val(name)
	} else if (mode === 'add') {
		form.find('#groupName').val('')
	}
})

$('#deleteModal').on('shown.bs.modal', function () {
	$('#no').focus()
})

$('#deleteModal').on('show.bs.modal', function (event) {
	var button = $(event.relatedTarget)
	var modal = $(this)
	
	var deleteForm = modal.find('#deleteForm')
	var id = button.data('id')
	var action = deleteForm.attr('action')
	deleteForm.attr('action', action.replace(/\d+/, id))
	
	var groupName = modal.find('#confirmation .group-name')
	var name = button.data('name')
	groupName.text(name)
})