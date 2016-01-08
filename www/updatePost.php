<?php
	//Authenticate login
	require "checkCookies.php";

	if(!$loggedin){
		header("Location: login.php");
	}else{
		//select latest post
		$files = glob("files/posts/*.htm");
		if(empty($files)){
			$newfilenum = 0;
		} else{
			$files = array_combine($files, array_map("filemtime", $files));
			arsort($files);
			$latestPost = key($files);
			//get the number of the latest file
				// get index of last "/"
			for($i = strlen($latestPost)-1; $i >=0; $i--){
				if( $latestPost[$i] == '/') break;
			}
			//strip the extension and path, get the value
			$oldfilenum = intval(substr($latestPost, $i+1, -4));
			if($oldfilenum >=9)
				$newfilenum = 0;
			else 
				$newfilenum = $oldfilenum + 1;
		}	
		
		$dir = "files/posts";
		$filename = $dir."/".strval($newfilenum).".htm";
		$file = fopen($filename, 'w');
		fwrite($file, date("m/d/Y")."\n");
		fwrite($file, $_POST['title']."\n");
		fwrite($file, $_POST['content']);
		fclose($file);
		// make a copy of the latest post for app users
		$filename = "files/latest.htm";
		$file = fopen($filename, 'w');
		fwrite($file, date("m/d/Y")."\n");
		fwrite($file, $_POST['title']."\n");
		fwrite($file, $_POST['content']);
		fclose($file);

		header("Location: index.php");
	}
?>
