<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>Clarkson GDC</title>
<link rel="stylesheet" type="text/css" href="style/main.css" />
</head>
<body>
	<?php include 'header.php';?>
	<?php include 'menu.php'; ?>
	<div id="content" class="content">
		<div class="subtitle" id="part1">Green Power Study</div>
		<div class="text">The interaction of the system components, especially
			the UCs, to the rest of the system that deliver DC power to the POD
			is critical for the proposed model and this interaction must be
			tested under controlled conditions. The objectives of this task are
			(1) development of power input variability curves based on historical
			wind data and turbine specifications, and (2) characterization of the
			system components including the OMU, PPS, EMU and the two PODs under
			controlled input power conditions, and (3) demonstration of
			ride-through capability by powering a POD directly with UCs .</div>
		<div class="subtitle" id="part2">Wind Power Downtime</div>
		<div class="subtitle2">Wind Power Profile</div>
		<div class="text">AWS offers a historical simulation of power output
			from hypothetical wind farms. It was generated using AWS mesoscale
			numerical weather prediction models and simulates the energy
			generated from hypothetical (non-existent) wind farms in 10 minute
			intervals. The data spans 3 years 2004-2006 and covers the eastern
			half of the lower 48 states. We have obtained AWS wind power profile
			for 2006, it includes 1300 sites around NY Main River Area with both
			the hourly observed wind power and the hourly-predicted wind power
			for next 4 hours.</div>
		<div class="tabletitle">Wind Profile Specs</div>
		<table class="spectable">
			<tr>
				<td>Coverage</td>
				<td>Main River Area around NY State</td>
			</tr>
			<tr>
				<td>Number of Sites</td>
				<td>1365 Sites</td>
			</tr>
			<tr>
				<td>Power Range</td>
				<td>varies from 100, 200, 500, to 1000</td>
			</tr>
		</table>
		<div class="text">
			A sample of the wind power profile from one site is <a href="">here</a>
		</div>
		<div class="subtitle">Downtime Pattern</div>
		<div class="text">We simplify the GDC downtime model by setting up a
			threshold to the observed wind power without considering battery
			backup, server and services bootup time, etc. Different threshold
			will affect the total GDC downtime over a year and also the GDC
			downtime periods. Detailed analyzed data is shown in Tables below.</div>
		<div class="pic">
			<img src="image/thres_gdc.jpg" width="500px" />
		</div>
		<div class="text">The table above indicates setting up the threshold
			to 0.1% of maximum wind power output over a year, GDC can gain a
			substantial high uptime to 98% of a year. Apparently when the
			threshold is set up 10% of maximum wind power over a year, GDC still
			can remain uptime to 83% of a year.</div>
		<div class="text">GDC downtime threshold is a critical parameter
			deciding GDC's availability and sustainable power usage efficiency.</div>
		<div class="pic">
			<img src="image/thres_dp.jpg" width="500px" />
		</div>
		<div class="text">From this table above, it says when threshold is at
			low bar, GDC will be more suffering frequent short downtime periods.
			However, when threshold is set at a high bar to 10%, GDC will
			experience the downtime by the daily weather conditions plus the
			short downtime periods.</div>
		<div class="text">Depending on the threshold, GDC may come up with
			different strategies for different downtime patterns. Accurate wind
			forecast is one of our mechanical group's focus in GDC project.</div>
	</div>
	<?php include 'footer.php'?>
</body>
</html>
