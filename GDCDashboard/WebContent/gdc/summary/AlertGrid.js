Ext.define('GDC.AlertGrid.AlertModel', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'id'
	}, {
		name : 'level'
	}, {
		name : 'date',
		type : 'date',
		dateFormat : 'n/j h:ia',
		defaultValue : undefined
	}, {
		name : 'type'
	}, {
		name : 'nodeId'
	}, {
		name : 'description'
	} ],
	idProperty : 'id'
});

var myData = [ [ 'WT-001', 'Severe', '9/1 12:00am', 'Data Center',
		'Data Center is Down' ] ];
myData
		.push([ 'ZZ-123', 'Warning', '9/1 12:00am', 'VM', 'VM will be migrated' ]);

GDC.AlertGrid.store = Ext.create('Ext.data.ArrayStore', {
	model : 'GDC.AlertGrid.AlertModel',
	data : myData
});

Ext.define('GDC.summary.AlertGrid', {
	extend : 'Ext.grid.Panel',
	title : 'Alert',
	xtype : 'alertGrid',
	store : GDC.AlertGrid.store,
	collapsible : true,
	multiSelect : true,
	stateId : 'stateGrid',
	columns : [ {
		text : 'Id',
		width : 60,
		sortable : false,
		dataIndex : 'id'
	}, {
		text : 'Level',
		width : 60,
		sortable : false,
		dataIndex : 'level'
	}, {
		text : 'Date',
		width : 75,
		renderer : Ext.util.Format.dateRenderer('m/d/Y'),
		sortable : false,
		dataIndex : 'date'
	}, {
		text : 'Type',
		width : 75,
		sortable : false,
		dataIndex : 'type'
	}, {
		text : 'Node Id',
		width : 75,
		sortable : false,
		dataIndex : 'nodeId'
	}, {
		text : 'Description',
		flex : 1,
		sortable : false,
		dataIndex : 'description'
	} ],
	viewConfig : {
		stripeRows : true,
		enableTextSelection : true
	},
	loadData : function(data) {
	}
});