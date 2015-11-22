$('#addEditFolderModal').on('shown.bs.modal', function () {
	$('#folderName').focus()
})

$('#addEditFolderModal').on('show.bs.modal', function (event) {
	var button = $(event.relatedTarget)
	var mode = button.data('mode')
	var modal = $(this)
	
	var title = modal.find('#addEditFolderModalLabel')
	title.text(title.data(mode))
	
	var form = modal.find('#addEditFolderForm')
	form.attr('action', form.data(mode))
	
	var submit = modal.find('#addEditFolderSubmit')
	submit.text(submit.data(mode))
	
	var isRoot = button.data('isroot');
	form.find('#folderIsRoot').val(isRoot);
	
	var parentId = button.data('parentid');
	form.find('#folderParentId').attr('value', parentId);
	
	if (mode === 'edit') {
		var id = button.data('id')
		form.attr('action', form.attr('action').replace(/\d+/, id))
		
		var name = button.data('name')
		form.find('#folderName').val(name)
		
	} else if (mode === 'add') {
		form.find('#folderName').val('')
	}
})

$('#deleteFolderModal').on('shown.bs.modal', function () {
	$('#folderNo').focus()
})

$('#deleteFolderModal').on('show.bs.modal', function (event) {
	var button = $(event.relatedTarget)
	var modal = $(this)
	
	var deleteForm = modal.find('#deleteFolderForm')
	var id = button.data('id')
	var action = deleteForm.attr('action')
	deleteForm.attr('action', action.replace(/\d+/, id))
	
	var folderName = modal.find('#folderConfirmation .folder-name')
	var name = button.data('name')
	folderName.text(name)
})