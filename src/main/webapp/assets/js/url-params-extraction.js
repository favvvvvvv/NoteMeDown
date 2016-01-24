function getURLParam(name) {
	if (window.location.search)
		var match = new RegExp('[?|&]' + name + '=' + '([^&]+)&?')
				.exec(window.location.search)
		if (match)
			return match[1]
	return null
}