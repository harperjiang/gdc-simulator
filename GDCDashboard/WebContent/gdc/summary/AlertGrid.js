Ext.define('GDC.AlertGrid.AlertModel', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'id'
	}, {
		name : 'level'
	}, {
		name : 'time',
		type : 'date',
		dateFormat : 'M d, Y g:i:s A',
		defaultValue : undefined
	}, {
		name : 'type'
	}, {
		name : 'nodeName'
	}, {
		name : 'description'
	} ],
	idProperty : 'id'
});

Ext.define('GDC.summary.AlertGrid', {
	extend : 'Ext.grid.Panel',
	title : 'Alert',
	xtype : 'alertGrid',
	store : Ext.create('Ext.data.ArrayStore', {
		model : 'GDC.AlertGrid.AlertModel'
	}),
	collapsible : true,
	multiSelect : true,
	stateId : 'stateGrid',
	columns : [ {
		text : 'Date',
		width : 110,
		renderer : Ext.util.Format.dateRenderer('m/d/Y H:i'),
		sortable : false,
		dataIndex : 'time'
	}, {
		text : 'Level',
		width : 80,
		sortable : false,
		dataIndex : 'level'
	}, {
		text : 'Type',
		width : 130,
		sortable : false,
		dataIndex : 'type'
	}, {
		text : 'Node',
		width : 160,
		sortable : false,
		dataIndex : 'nodeName'
	}, {
		text : 'Description',
		flex : 1,
		sortable : false,
		dataIndex : 'description'
	} ],
	viewConfig : {
		stripeRows : true,
		enableTextSelection : true
	}
});