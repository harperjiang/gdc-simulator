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
		name : 'status'
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
	listeners : {
		'selectionchange' : function(view, records) {
			this.down('#migrateButton').setDisabled(!records.length);
		}
	},
	tbar : [ {
		itemId : 'migrateButton',
		text : 'Migrate VM',
		handler : function() {
			var gridPanel = this.up('gdcVmGrid');
			var model = gridPanel.getSelectionModel().getSelection()[0];
			var vmName = model.get('name');
			var machinePanel = this.up('nodeMachinePanel');
			var srcId = machinePanel.datas.id;
			Ext.Msg.prompt('Destination', 'Enter Destination Machine:', function(btn, dest) {
			    debugger;
				if (btn == 'ok'){
					vmService.migrate(vmName, srcId, dest);
					// TODO handle error message
			    }
			});
		},
		disabled : true
	}, {
		text : 'Create New VM',
		handler : function() {
			Ext.Msg.alert('Warning','Not implemented yet');
		}
	}, {
		text : 'Refresh',
		handler : function() {
			this.up('gdcVmGrid').refresh();
		}
	} ],
	collapsible : true,
	multiSelect : false, // This seems not working?
	columns : [ {
		text : 'Id',
		width : 60,
		sortable : false,
		dataIndex : 'id'
	}, {
		text : 'Name',
		width : 120,
		sortable : false,
		dataIndex : 'name'
	}, {
		text : 'Status',
		width : 90,
		sortable : false,
		dataIndex : 'status'
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
	}, 
	refresh : function() {
		var machinePanel = this.up('nodeMachinePanel');
		var srcId = machinePanel.datas.id;
		vmService.list(srcId,function(vmBean) {
			var ownerId = vmBean.ownerId;
			var vmGrid = Ext.getCmp('tab'+ownerId).down('gdcVmGrid');
			var dataArray = new Array();
			for(var i = 0 ; i < vmBean.vms.length; i++) {
				dataArray[i] = new Array();
				dataArray[i][0] = false;
				dataArray[i][1] = vmBean.vms[i].id;
				dataArray[i][2] = vmBean.vms[i].name;
				dataArray[i][3] = vmBean.vms[i].attributes.status;
				dataArray[i][4] = ' ';
			}
			vmGrid.store.loadData(dataArray);
		});
	} 
});