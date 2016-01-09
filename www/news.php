<?php
	//Authenticate login
	require "checkCookies.php";
?>
<!DOCTYPE HTML>
<html>
<head>
	<meta charset="UTF-8">
	<title>News - Masjid Umar</title>
	<link rel="stylesheet" href="css/style.css" type="text/css">
</head>
<body>
	<?php include "header.html"; ?>
	<div id="contents">
		<div class="main">
			<h1 id ="newsHead">News</h1>
			<ul class="news">
				<?php
					$files = glob("files/posts/*.htm");
					$files = array_combine($files, array_map("filemtime", $files));
					arsort($files);
					foreach($files as $file => $accesst){
						//get date from the post						
						$handle = fopen($file, 'r');
						if($handle){
							$date = fgets($handle);
							$date = explode("-", $date);
							$month = $date[0];
							$day = $date[1];
							$year = $date[2];
							$title = fgets($handle);
							$content = fgets($handle);
							fclose($handle);
							echo <<<EOT
							<li>
								<div class="date">
									<p>
										<span>$month/$day</span>
										$year
									</p>
								</div>
								<h2>$title</h2>
								<p>
									$content
								</p>

EOT;
							if($loggedin) echo"<p id=\"deletepost\"><a href=\"deletePost.php?filename=$file\">Delete</a></p>";								
							echo "</li>";
						}						
					}
				?>
			</ul>
		</div>		
	</div>
	<?php include "footer.html"; ?>
</body>
</html>