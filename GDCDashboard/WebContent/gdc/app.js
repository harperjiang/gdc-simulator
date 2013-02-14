Ext.Loader.setConfig({
	enabled : true,
	paths : {
		'GDC' : 'gdc'
	}
});
Ext.require([ 'Ext.data.TreeStore' ]);
Ext.require([ 'Ext.chart.*', 'Ext.chart.axis.Gauge', 'Ext.chart.series.*',
		'Ext.Window' ]);

Ext.require([ 'GDC.MainViewPort' ]);
Ext.require([ 'GDC.StructurePanel', 'GDC.SummaryPanel' ]);
Ext.require([ 'GDC.MachineViewPanel' ]);

Ext.application({
	name : 'Kooobao Ecom System',
	launch : function() {
		Ext.create('GDC.MainViewPort');
	}
});