Ext.define('GDC.node.VMModel', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'select',
		type : 'bool'
	}, {
		name : 'id'
	}, {
		name : 'name'
	}, {
		name : 'type'
	}, {
		name : 'desc'
	} ],
	idProperty : 'id'
});

Ext.define('GDC.node.VMGrid', {
	extend : 'Ext.grid.Panel',
	title : 'Virtual Machines',
	xtype : 'gdcVmGrid',
	store : Ext.create('Ext.data.ArrayStore', {
		model : 'GDC.node.VMModel'
	}),
	tbar : [ {
		text : 'Migrate VM',
		handler : function() {
			debugger;
			var gridPanel = this.up('gdcVmGrid');
//			gridPanel.get
		}
	}, {
		text : 'Create New VM',
		handler : function() {

		}
	} ],
	collapsible : true,
	multiSelect : true,
	columns : [ {
		text : 'Id',
		width : 60,
		sortable : false,
		dataIndex : 'id'
	}, {
		text : 'Level',
		width : 60,
		sortable : false,
		dataIndex : 'name'
	}, {
		text : 'Type',
		width : 75,
		sortable : false,
		dataIndex : 'type'
	}, {
		text : 'Description',
		flex : 1,
		sortable : false,
		dataIndex : 'desc'
	} ],
	selModel : Ext.create('Ext.selection.CheckboxModel'),
	viewConfig : {
		stripeRows : true,
		enableTextSelection : true
	}
});