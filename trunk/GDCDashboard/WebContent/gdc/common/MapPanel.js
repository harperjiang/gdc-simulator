Ext.define('GDC.common.MapPanel', {
	extend : 'Ext.panel.Panel',
	xtype : 'gdcMapPanel',
	title : 'Map',
	collapsible : true,
	listeners : {
		'afterrender' : function() {
			var mapOptions = {
				zoom : this.zoom,
				center : new google.maps.LatLng(this.center.x, this.center.y),
				mapTypeId : google.maps.MapTypeId.ROADMAP
			};
			map = new google.maps.Map(this.body.dom, mapOptions);

			if (this.markers != undefined) {
				for ( var i = 0; i < this.markers.length; i++) {
					var marker = this.markers[i];
					new google.maps.Marker({
						position : new google.maps.LatLng(marker.x, marker.y),
						map : map,
						title : marker.title
					});
				}
			}
		}
	}
});