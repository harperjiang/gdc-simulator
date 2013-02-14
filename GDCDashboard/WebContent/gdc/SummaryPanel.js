Ext.define('GDC.SummaryPanel', {
	extend : 'Ext.panel.Panel',
	xtype : 'summaryPanel',
	closable : true,
	title : 'Summary',
	items : [ {
		xtype : 'panel',
		width : '100%',
		height : 40,
		styleHtmlContent : true,
		styleHtmlCls : 'banner',
		html : 'Summary'
	} ]
});