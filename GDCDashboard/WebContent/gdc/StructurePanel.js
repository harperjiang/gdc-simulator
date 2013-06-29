Ext.namespace("GDC");
GDC.structureStore = Ext.create('Ext.data.TreeStore');
GDC.dataCenters = new Array();
GDC.machinesUnderDs = {};
structureService.getDataCenters(function(dcs) {
	var root = {};
	root.expanded = true;
	root.children = new Array();
	for ( var dcCount = 0; dcCount < dcs.length; dcCount++) {
		var dc = dcs[dcCount];
		var dcNode = {
			text : dc.name,
			id : dc.id,
			leaf : dc.batteries.length === 0 && dc.powerSource === undefined,
			children : new Array()
		};
		GDC.dataCenters.push(dcNode);
		GDC.machinesUnderDs[dcNode.id] = new Array();
		if (dc.powerSource != null) {
			var powerNode = {
				text : dc.powerSource.name,
				id : dc.powerSource.id,
				leaf : true
			};
			dcNode.children.push(powerNode);
		}
		for ( var btyCount = 0; btyCount < dc.batteries.length; btyCount++) {
			var bty = dc.batteries[btyCount];
			var btyNode = {
				text : bty.name,
				id : bty.id,
				leaf : bty.machines.length === 0,
				children : new Array()
			};
			for ( var mcCount = 0; mcCount < bty.machines.length; mcCount++) {
				var mc = bty.machines[mcCount];
				var mcNode = {
					text : mc.name,
					id : mc.id,
					leaf : mc.vms.length === 0,
					children : new Array()
				};
				GDC.machinesUnderDs[dcNode.id].push(mcNode);

				btyNode.children.push(mcNode);
			}
			dcNode.children.push(btyNode);
		}
		root.children.push(dcNode);
	}
	GDC.structureStore.setRootNode(root);
});

function updateId(existed, dataid, newid) {
	existed.id = newid;
	existed.dataId = dataid;
	Ext.ComponentManager.register(existed);
}

function refreshNode(id) {
	nodeService.getData(id, {
		callback : function(reply) {
			var data = JSON.parse(reply);
			var newid = "tab" + data.id;
			var tab = Ext.getCmp(newid);
			data.image = data.STATUS ? 'green_light.png' : 'red_light.png';
			tab.loadData(data);
		},
		errorHandler : function(errorString, exception) {
			console.log(errorString);
		}
	});
}

function displayNode(id) {
	nodeService.getData(id, function(reply) {
		var data = JSON.parse(reply);
		var newid = "tab" + data.id;
		var mainTabPanel = Ext.getCmp('maintab');
		var existed = Ext.getCmp(newid);
		if (existed != null && existed.items.length == 0) {
			existed.destroy();
			existed = undefined;
		}
		if (undefined === existed || null == existed) {
			switch (data.type) {
			case 'dc':
				existed = Ext.create('GDC.node.DCViewPanel');
				break;
			case 'battery':
				existed = Ext.create('GDC.node.UPSViewPanel');
				break;
			case 'machine':
				existed = Ext.create('GDC.node.MachineViewPanel');
				break;
			case 'power':
				existed = Ext.create('GDC.node.PowerViewPanel');
				break;
			default:
				alert('Unrecognized type:' + data.type);
				break;
			}
			updateId(existed, data.id, newid);
			existed.title = data.name;
			mainTabPanel.add(existed);
		}
		// Refresh Data
		// Preprocess data
		data.image = data.STATUS ? 'green_light.png' : 'red_light.png';
		existed.loadData(data);

		// Set it as the active one
		// disable the listener
		var tabchangelistener = mainTabPanel.events.tabchange;
		mainTabPanel.events.tabchange = true;
		mainTabPanel.setActiveTab(newid);
		mainTabPanel.events.tabchange = tabchangelistener;
	});
}

Ext.define("GDC.StructurePanel", {
	extend : 'Ext.tree.Panel',
	title : 'Machine List',
	xtype : 'strutpanel',
	store : GDC.structureStore,
	rootVisible : false,
	width : 200,
	listeners : {
		select : function(tree, record, row, opt) {
			displayNode(record.raw.id);
		},
		afterrender : function() {
			Ext.TaskManager.start({
				run : updateData,
				interval : 5000
			});
		}
	}
});

function updateData() {
	var mainTabPanel = Ext.getCmp('maintab');
	var activeTab = mainTabPanel.getActiveTab();
	if (activeTab.id.substring(0, 7) == 'summary') {
		// Refresh summary
		activeTab.refreshAlert();
	} else {
		var id = activeTab.id.substring(3);
		refreshNode(id);
	}
}