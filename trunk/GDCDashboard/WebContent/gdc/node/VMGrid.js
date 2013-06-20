Ext.define('GDC.node.VMModel', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'select',
		type : 'bool'
	}, {
		name : 'name'
	}, {
		name : 'status'
	}, {
		name : 'desc'
	} ],
	idProperty : 'name'
});

Ext.define('GDC.node.VMGrid', {
	extend : 'Ext.grid.Panel',
	title : 'Virtual Machines',
	xtype : 'gdcVmGrid',
	listeners : {
		'selectionchange' : function(view, records) {
			this.down('#migrateButton').setDisabled(!records.length);
			this.down('#restartButton').setDisabled(!records.length);
			this.down('#startButton').setDisabled(!records.length);
			this.down('#stopButton').setDisabled(!records.length);
		}
	},
	tbar : [ {
		itemId : 'migrateButton',
		text : 'Migrate VM',
		handler : function() {
			var gridPanel = this.up('gdcVmGrid');
			var model = gridPanel.getSelectionModel()
					.getSelection()[0];
			var vmName = model.get('name');
			var machinePanel = this.up('nodeMachinePanel');
			var srcId = machinePanel.datas.id;
			Ext.Msg.prompt('Destination',
					'Enter Destination Machine:', function(btn,
							dest) {
						if (btn == 'ok') {
							vmService.migrate(vmName, srcId,
									dest);
							// TODO handle error message
						}
					});
		},
		disabled : true
	}, {
		text : 'Create New VM',
		handler : function() {
			Ext.Msg.alert('Warning', 'Not implemented yet');
		}
	}, {
		text : 'Refresh',
		handler : function() {
			this.up('gdcVmGrid').refresh();
		}
	}, {
		text : 'Start',
		itemId : 'startButton',
		icon : 'resource/icon/start_small.png',
		handler : function() {
			this.up('gdcVmGrid').operate('START');
		},
		disabled : true
	}, {
		text : 'Stop',
		itemId : 'stopButton',
		icon : 'resource/icon/stop_small.png',
		handler : function() {
			this.up('gdcVmGrid').operate('STOP');
		},
		disabled : true
	}, {
		text : 'Restart',
		icon : 'resource/icon/reset_small.png',
		itemId : 'restartButton',
		handler : function() {
			this.up('gdcVmGrid').operate('RESTART');
		},
		disabled : true
	} ],
	collapsible : true,
	multiSelect : false, // This seems not working?
	columns : [ {
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
	initComponent : function() {
		this.store = Ext.create('Ext.data.ArrayStore', {
			model : 'GDC.node.VMModel'
		});
		this.callParent();
	},
	refresh : function() {
		var machinePanel = this.up('nodeMachinePanel');
		var srcId = machinePanel.datas.id;
		vmService.list(srcId, function(vmBean) {
			var ownerId = vmBean.ownerId;
			var vmGrid = Ext.getCmp('tab' + ownerId).down('gdcVmGrid');
			var dataArray = new Array();
			for ( var i = 0; i < vmBean.vms.length; i++) {
				dataArray[i] = new Array();
				dataArray[i][0] = false;
				dataArray[i][1] = vmBean.vms[i].name;
				dataArray[i][2] = vmBean.vms[i].attributes.status;
				dataArray[i][3] = ' ';
			}
			vmGrid.store.loadData(dataArray);
		});
	},
	operate : function(operation) {
		var gridPanel = this.up('gdcVmGrid');
		var model = gridPanel.getSelectionModel()
				.getSelection()[0];
		var vmName = model.get('name');
		var machinePanel = this.up('nodeMachinePanel');
		var srcId = machinePanel.datas.id;
		vmService.operate(vmName, srcId, operation, function() {
			
		});
	}
});