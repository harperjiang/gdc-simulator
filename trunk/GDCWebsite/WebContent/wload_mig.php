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
		<div class="subtitle">Workload Migration</div>
		<div class="text">With the time constraint of GDC node power outage,
			migrating the computation out over gigabyte network is relatively
			feasible. The computations will unlikely get interrupted and better
			than node failover and reboot. To migrate the whole data out of GDC
			is more likely unrealistic considering the migration time it needs to
			take. Moreoever, computation resource is relatively small-scale and
			easy to migrate but as disk space is cheap and easy to expand to
			Terabytes or Perabytes, user data is hard to move. So when we
			consider about the workload hosting in GDC, computation and data need
			to be treated separately. Our task is to perform a thorough
			evaluation of these migration technologies in a wide range of
			environments – between two machines in the same rack sharing a NAS,
			between two machines in the same rack not sharing a NAS, between two
			machines located in two different labs on campus with and without NAS
			and finally between an on-campus machine and an off-campus machine
			with and without NAS.</div>
	</div>
	<?php include 'footer.php'?>
</body>
</html>
