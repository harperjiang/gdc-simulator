Ext.namespace("GDC");
GDC.structureStore = Ext.create('Ext.data.TreeStore');
structureService.getDataCenters(function(dcs) {
	var root = {};
	root.expanded = true;
	root.children = new Array();
	for ( var dcCount = 0; dcCount < dcs.length; dcCount++) {
		var dc = dcs[dcCount];
		var dcNode = {
			text : dc.name,
			id : dc.id,
			leaf : dc.batteries.length === 0,
			children : new Array()
		};
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
				for ( var vmCount = 0; vmCount < mc.vms.length; vmCount++) {
					var vm = mc.vms[vmCount];
					var vmNode = {
						text : vm.name,
						id : vm.id,
						leaf : true
					};
					mcNode.children.push(vmNode);
				}
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
				existed = Ext.create('GDC.node.PowerViewPanel');
				break;
			case 'machine':
				existed = Ext.create('GDC.node.MachineViewPanel');
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
		mainTabPanel.setActiveTab(newid);
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
		}
	}
});