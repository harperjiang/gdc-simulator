Ext.define('GDC.common.HtmlRenderer', {
	updateHtml : function(component, data) {
		var html = component.htmlContent;
		if (html == undefined) {
			component['htmlContent'] = Utils.loadhtml(component.baseHtml);
			html = component.htmlContent;
		}
		for ( var i in data) {
			html = html.replace('{' + i + '}', data[i]);
		}
		if (Ext.get(component.id) == null) {
			// Not created yet
			component.html = html;
		} else {
			Ext.get(component.id).dom.innerHTML = html;
		}
	}
});
Ext.namespace('GDC.common.HtmlRendererInst');
GDC.common.HtmlRendererInst = Ext.create('GDC.common.HtmlRenderer');