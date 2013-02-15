Ext.Loader.setConfig({
	enabled : true,
	paths : {
		'GDC' : 'gdc'
	}
});
Ext.require([ 'Ext.data.TreeStore' ]);
Ext.require([ 'Ext.chart.*', 'Ext.chart.axis.Gauge', 'Ext.chart.series.*',
		'Ext.Window' ]);
Ext.require([ 'Ext.grid.*', 'Ext.data.*', 'Ext.util.*', 'Ext.state.*' ]);

Ext.require([ 'GDC.MainViewPort', 'GDC.StructurePanel' ]);

Ext.require([ 'GDC.common.GaugeChart' ]);

Ext.require([ 'GDC.summary.AlertGrid', 'GDC.summary.SummaryPanel' ]);

Ext.require([ 'GDC.node.DCViewPanel', 'GDC.node.MachineViewPanel' ]);

Ext.application({
	name : 'Kooobao Ecom System',
	launch : function() {
		Ext.create('GDC.MainViewPort');
	}
});