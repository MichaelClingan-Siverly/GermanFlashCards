<?php
$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE);
$w1 = $input['gWord'];
$w2 = $input['eWord'];
prepend($w1, $w2);

//https://stackoverflow.com/a/3332403
function prepend($gWords, $eWords){
    $cache_new = $gWords . "\n" . $eWords; // this gets prepended
    $file = "../not_public/wordList.txt"; // the file to which $cache_new gets prepended
    
    if(filesize($file) > 0){
        $cache_new .= "\n";
    }

    $handle = fopen($file, "r+");
    $len = strlen($cache_new);
    $final_len = filesize($file) + $len;
    $cache_old = fread($handle, $len);
    rewind($handle);
    $i = 1;
    while (ftell($handle) < $final_len) {
    fwrite($handle, $cache_new);
    $cache_new = $cache_old;
    $cache_old = fread($handle, $len);
    fseek($handle, $i * $len);
    $i++;
    }
}
?>