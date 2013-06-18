Ext.define('GDC.common.MapPanel', {
	extend : 'Ext.panel.Panel',
	xtype : 'gdcMapPanel',
	title : 'Map',
	collapsible : true,
	listeners : {
		'afterrender' : function() {
			var mapOptions = {
				zoom : 4,
				center : new google.maps.LatLng(40.000, -90.000),
				mapTypeId : google.maps.MapTypeId.ROADMAP
			};
			map = new google.maps.Map(this.body.dom, mapOptions);
		}
	}
});