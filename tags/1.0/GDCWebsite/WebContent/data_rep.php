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
		<div class="subtitle">Data Replication</div>
		<div class="text">
			Students in CS644 course <a
				href="http://people.clarkson.edu/class/cs644/cs644.fa2012/">
				Advanced Operating System</a> have particuliarly looked into the
			data coherence issues in distributed system. This issue also applies
			to GDC if the data resident inside GDC is updated frequently and
			needs to be synced across GDCs.
		</div>
		<div class="subtitle2">CAP theorem</div>
		<div class="text">
			In a distributed system, replication is a conventional strategy to
			prevent node failover or system crash. But replication across
			different nodes also introduces the consistency and synchronization
			problem. In the 2000 Symposium on Principles of Distributed Computing
			(PODC), Scientist Eric Brewer from UC Berkeley first proposed as a
			conjecture that it is impossible to provide
			<ul>
				<li>Consistency</li>
				<li>Availability</li>
				<li>VNetwork Partitions</li>
			</ul>
		</div>
		<div class="text">simultaneously in a distributed system, which is the
			well-known CAP theorem and has been theoretically proved by Seth
			Gilbert and Nancy Lynch of MIT in 2012.</div>
		<div class="subtitle2">Study the existing DC Applications</div>
		<div class="text">Following the CAP theorem, since GDC has to face the
			power outage, which is an extreme case of Network partition, if a
			distributed system is hosted across GDCs and data have multiple
			copies and are kept updated across GDCs, then we cannot provide
			data's strong consistency and high availability at the same time.</div>
		<div class="pic">
			<img src="image/cap.jpg" width="500px" />
		</div>
		<div class="text">There are still a lot of successful solutions to CAP
			among current popular distributed systems. While still persistent to
			high availability, some of them customize the applications to
			maintain weak consistency without hurting the user experience by the
			special characteristics of their applications, some of them tries to
			maintain causal consistency even in the face of inconsistency to some
			extend.</div>
		<div class="text">
			Here is the readling list of those distributed systems and reviews:
			<table class="spectable">
				<tr>
					<td>PAPER</td>
					<td>NAME</td>
					<td>LINKS</td>
				</tr>
				<tr>
					<td width="500px">The Google File System</td>
					<td width="150px">Brian Devins <br />Ronny Bulls
					</td>
					<td><a
						href="http://people.clarkson.edu/~jmatthew/cs644/students/devinsba/PaperRereads-GFS.pdf">[link]</a>
						<br /> <a
						href="http://people.clarkson.edu/~jmatthew/cs644/students/bullrl/bullrl_GDC_GFS.pdf">[link]</a>
					</td>
				</tr>
				<tr>
					<td>Scale and Performance in a Distributed File System</td>
					<td>Ronny Bulls</td>
					<td><a
						href="http://people.clarkson.edu/~jmatthew/cs644/students/bullrl/bullrl_GDC_AFS.pdf">[link]</a>
					</td>
				</tr>
				<tr>
					<td>Windows Azure Storage</td>
					<td>Vinay Soni <br />Long Zhang
					</td>
					<td><a
						href="http://people.clarkson.edu/~jmatthew/cs644/students/sonivr/New%20reactions%20specific%20for%20GDC/Windows%20Azure%20Storage%20New%20Questions.pdf">[link]</a></br>
						<a
						href="http://people.clarkson.edu/~jmatthew/cs644/students/sonivr/New%20reactions%20specific%20for%20GDC/Azure--PaperAssignment.pdf">[link]</a>
					</td>
				</tr>
				<tr>
					<td>Dynamo: Amazon's Highly Available Key-Value Store</td>
					<td>Wenjin Hu <br />Andrew Hicks
					</td>
					<td><a
						href="https://docs.google.com/document/pub?id=1IQW8MpEjAozyE_6m0k0yyVnJvep3JP8uhS8ftI6jIUI">[link]</a>
						<br /> <a
						href="http://people.clarkson.edu/~jmatthew/cs644/students/hicksac/GDC-Dynamo.pdf">[link]</a>
						<br /></td>
				</tr>
				<tr>
					<td>PNUTS: Yahoo!'s hosted data serving platform</td>
					<td>Vinay Soni <br />Brian Hudson
					</td>
					<td><a
						href="http://people.clarkson.edu/~jmatthew/cs644/students/sonivr/New%20reactions%20specific%20for%20GDC/PNUTS.pdf">[link]</a>
						<br /> <a
						href="http://people.clarkson.edu/~jmatthew/cs644/students/sonivr/Pnuts--PaperAssignment.pdf">[link]</a>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<?php include 'footer.php'?>
</body>
</html>
