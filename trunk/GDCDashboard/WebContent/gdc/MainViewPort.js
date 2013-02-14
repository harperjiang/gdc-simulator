Ext.define("GDC.MainViewPort", {
	extend : 'Ext.container.Viewport',
	layout : {
		type : 'border'
	},
	items : [ {
		xtype : 'strutpanel',
		region : 'west'
	}, {
		xtype : 'tabpanel',
		id : 'maintab',
		region : 'center',
		items : [ {
			xtype : 'summaryPanel'
		} ]
	} ]
});