Ext.define("GDC.MainViewPort", {
	extend : 'Ext.container.Viewport',
	id : 'mainpanel',
	layout : {
		type : 'border'
	},
	items : [ {
		xtype : 'strutpanel',
		region : 'west'
	}, {
		xtype : 'tabpanel',
		id : 'maintab',
		closeAction : 'hide',
		region : 'center',
		items : [ {
			xtype : 'summaryPanel'
		} ],
		listeners : {
			tabchange : function(tabPanel, newTab, oldTab, index) {
				if (newTab.id.substring(0, 7) === 'summary') {

				} else {
					var id = newTab.id.substring(3);
					refreshNode(id);
				}
			}
		}
	} ]
});