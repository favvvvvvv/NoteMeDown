$('#addEditTaskModal').on('shown.bs.modal', function () {
	$('#taskName').focus()
})

$('#addEditTaskModal').on('show.bs.modal', function (event) {
	var button = $(event.relatedTarget)
	if (button[0].tagName == 'BUTTON') { // event not caused by Bootstrap Datepicker
		var mode = button.data('mode')
		var modal = $(this)
		
		var title = modal.find('#addEditTaskModalLabel')
		title.text(title.data(mode))
		
		var form = modal.find('#addEditTaskForm')
		form.attr('action', form.data(mode))
		
		var submit = modal.find('#addEditTaskSubmit')
		submit.text(submit.data(mode))
		
		var folderId = button.data('folderid')
		form.find('#taskFolderId').attr('value', folderId);
		
		if (mode === 'edit') {
			var id = button.data('id')
			form.attr('action', form.attr('action').replace(/\d+/, id))
			
			var dataHolder = $('#' + id + '.data-holder')
			
			var name = dataHolder.data('name')
			form.find('#taskName').val(name)
			
			var description = dataHolder.data('description')
			form.find('#taskDescription').val(description)
			
			var dueDate = dataHolder.data('duedate')
			form.find('#taskDueDate').val(dueDate)
			
			var status = dataHolder.data('status')
			form.find('#taskStatus').val(status)
			
			var datePostponed = dataHolder.data('datepostponed')
			form.find('#taskDatePostponed').val(datePostponed)
			
			var dateFinished = dataHolder.data('datefinished')
			form.find('#taskDateFinished').val(dateFinished)
		} else if (mode === 'add') {
			form.find('#taskName').val('')
			form.find('#taskDescription').val('')
			form.find('#taskDueDate').val('')
			form.find('#taskStatus').val('')
			form.find('#taskDatePostponed').val('')
			form.find('#taskDateFinished').val('')
		}
	}
})

$('#deleteTaskModal').on('shown.bs.modal', function () {
	$('#taskNo').focus()
})

$('#deleteTaskModal').on('show.bs.modal', function (event) {
	var button = $(event.relatedTarget)
	var modal = $(this)
	
	var deleteForm = modal.find('#deleteTaskForm')
	var id = button.data('id')
	var action = deleteForm.attr('action')
	deleteForm.attr('action', action.replace(/\d+/, id))
	
	var taskName = modal.find('#taskConfirmation .task-name')
	var name = button.data('name')
	taskName.text(name)
})