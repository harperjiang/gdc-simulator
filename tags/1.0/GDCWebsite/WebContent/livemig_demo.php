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
		<div class="subtitle">Live Migration Demo</div>
		<div class="text">To get the live migration performance more real,
			below is a video demo that VM hosts a video server and the VM is kept
			migrating between two servers back and forth.</div>
		<div class="text">
			To play the video on your local machine. it requires the software:
			<ul>
				<li>32bit Firefox/Chrome</li>
				<li>32bit VLC Media Player
					<ul>
						<li>If you are using mac, please install VLC from <a
							href="http://www.videolan.org/vlc/download-macosx.html">here</a>
						</li>
						<li>If you are using windows, please install VLC player from <a
							href="http://www.videolan.org/vlc/download-windows.html">here</a>
						</li>
					</ul>
				</li>
			</ul>
		</div>
		<div class="text">
			The minimal technical requirement to play the video on your media
			player are:
			<ul>
				<li>Support rtsp protocol</li>
				<li>Support H.264 Codecs</li>
			</ul>
		</div>
		<div class="text">
			<a href="http://people.clarkson.edu/~huwj/gdc.html">[Video Migration
				Demo]</a>
		</div>
		<div class="text">This video demostrates that user who is watching a
			video from this VM video server is not impacted by live migration
			downtime at all. The reason is that migration downtime is so trivial
			that lost video frames are hardly detected or noticed by the user’s
			eyes.</div>
	</div>
	<?php include 'footer.php'?>
</body>
</html>
