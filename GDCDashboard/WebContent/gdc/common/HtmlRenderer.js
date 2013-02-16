Ext.define('GDC.common.HtmlRenderer', {
	updateHtml : function(component, data) {
		debugger;
		var html = component.html;
		if (html == undefined) {
			html = Ext.get(component.id).dom.innerHTML;
		}
		for ( var i in data) {
			html = html.replace('{' + i + '}', data[i]);
		}
		component.html = html;
	}
});
Ext.namespace('GDC.common.HtmlRendererInst');
GDC.common.HtmlRendererInst = Ext.create('GDC.common.HtmlRenderer');