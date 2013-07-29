Ext.define('GDC.MigrationLogGrid.MigrationLogModel', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'id'
	}, {
		name : 'beginTime',
		type : 'date',
		dateFormat : 'M d, Y g:i:s A',
		defaultValue : undefined
	}, {
		name : 'endTime',
		type : 'date',
		dateFormat : 'M d, Y g:i:s A',
		defaultValue : undefined
	}, {
		name : 'fromMachine'
	}, {
		name : 'toMachine'
	}, {
		name : 'vmName'
	} ],
	idProperty : 'id'
});

Ext.define('GDC.summary.MigrationLogGrid', {
	extend : 'Ext.grid.Panel',
	title : 'Migration Logs',
	xtype : 'mlogGrid',
	store : Ext.create('Ext.data.ArrayStore', {
		model : 'GDC.MigrationLogGrid.MigrationLogModel'
	}),
	collapsible : true,
	multiSelect : true,
	stateId : 'stateGrid',
	columns : [ {
		text : 'Begin Time',
		width : 110,
		renderer : Ext.util.Format.dateRenderer('m/d/Y H:i'),
		sortable : false,
		dataIndex : 'beginTime'
	}, {
		text : 'End Time',
		width : 110,
		renderer : Ext.util.Format.dateRenderer('m/d/Y H:i'),
		sortable : false,
		dataIndex : 'endTime'
	}, {
		text : 'From Machine',
		width : 150,
		sortable : false,
		dataIndex : 'fromMachine'
	}, {
		text : 'To Machine',
		width : 150,
		sortable : false,
		dataIndex : 'toMachine'
	}, {
		text : 'VM Name',
		flex : 1,
		sortable : false,
		dataIndex : 'vmName'
	} ],
	viewConfig : {
		stripeRows : true,
		enableTextSelection : true
	}
});