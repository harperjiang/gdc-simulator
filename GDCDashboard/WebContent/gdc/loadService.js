window.Utils = {};
Utils.loadjs = function(jsurl) {
	var request = new XMLHttpRequest();
	request.open("GET", jsurl, false);
	request.send();
	if (request.status == 200) {
		eval(request.responseText);
	}
};

Utils.loadhtml = function (htmlurl) {
	var request = new XMLHttpRequest();
	request.open("GET", htmlurl, false);
	request.send();
	if(request.status == 200)
		return request.responseText;
	return '';
};

Utils.loadjs('dwr/engine.js');
Utils.loadjs('dwr/interface/structureService.js');
Utils.loadjs('dwr/interface/nodeService.js');
Utils.loadjs('dwr/interface/vmService.js');