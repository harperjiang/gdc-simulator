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
		<?php if($_POST["pass"] != NULL 
				&& md5($_POST["pass"]) == '9d2497db63c470282f7b3a2d67644421'){	// ClarksonGdc2013?>
		<div class="subtitle">Progress Reports</div>
		<div class="resource_file">
			<img src="image/pdf_icon.jpg" />
			<div class="linkholder">
				<a href="report/quarter_1.pdf">NYSERDA PON1772 first progress report
					April 27 2012.pdf</a>
			</div>
			<div class="cleaner"></div>
		</div>
		<div class="resource_file">
			<img src="image/pdf_icon.jpg" />
			<div class="linkholder">
				<a href="report/quarter_2.pdf">NYSERDA PON1772 second progress
					report July 27 2012.pdf</a>
			</div>
			<div class="cleaner"></div>
		</div>
		<div class="resource_file">
			<img src="image/pdf_icon.jpg" />
			<div class="linkholder">
				<a href="report/quarter_3.pdf">NYSERDA PON1772 third progress report
					(FINAL).pdf</a>
			</div>
			<div class="cleaner"></div>
		</div>
		<div class="resource_file">
			<img src="image/pdf_icon.jpg" />
			<div class="linkholder">
				<a href="report/quarter_4.pdf">NYSERDA PON1772 forth progress
					report.pdf</a>
			</div>
			<div class="cleaner"></div>
		</div>
		<?php }else{// Display Login Forms?>
		<form method="POST" action="resource.php">
			<label for="pass">Please input password to view resources.</label> <input
				id="pass" type="password" name="pass" /> <input type="submit" />
		</form>
		<?php };?>

	</div>
	<?php include 'footer.php'?>
</body>
</html>
