<?php
$file = fopen("../not_public/wordList.txt", 'r');
$first_line = trim(next_line($file));
$clients_line = null;

//If an argument was supplied, and the first (uncommented) line of the file
//is identical to it, then there are no new words to send to the client
if(isset($_GET["last_word"]) && $first_line === ($clients_line = urldecode($_GET["last_word"]))){
    echo 'false';
}
else{
    send_response($first_line, $clients_line, $file);
}
fclose($file);

function next_line($file){
    //eat any comment lines until a valid one is found
    do{
        $line = trim(fgets($file));
    }while("#" === substr($line, 0, 1));
    return $line;
}

//given the first (alread read) line, builds and sends the rest of the uncommented lines
function send_response($str, $client_line, $file) {
    $line_count_good = false;
    //stops adding pairs when a german word is found thats the same as the one given by client
    while(($line = next_line($file)) != null && (!$line_count_good || $client_line !== $line)){
        $str .= "\n" . $line;
        $line_count_good = !$line_count_good;
    }
    
    //if there's a malformed pair, I'll just let it fail (so at least I'll notice somethings wrong)
    if(!$line_count_good){
        echo 'false';
    }
    else{
        echo $str;
    }
}
?>