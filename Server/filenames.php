<?php
$dir = "sheetfiles";
$arr=array();
// Open a directory, and read its contents
if (is_dir($dir)){
  if ($dh = opendir($dir)){
    while (($file = readdir($dh)) !== false){

        if(!(($file == "..")||($file== ".")))
        {
            $info=new stdClass();
            $info->name=$file;
            array_push($arr,$info);

        }
      
    }
    closedir($dh);
  }
}
echo json_encode($arr);
?>