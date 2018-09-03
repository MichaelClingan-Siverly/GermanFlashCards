<?php
session_start();

if(!isset($_SESSION["login"]) || $_SESSION["login"] === false){
    header("LOCATION: addWords.php");
    exit();
}
else if(isset($_POST["logout"])){
    $_SESSION["login"] = false;
    header("LOCATION: addWords.php");
    exit();
}
$valid = $_SESSION["login"];
?>


<!DOCTYPE html>
<html>
    <!--I used a lot of this (and documentation) for the html: https://stackoverflow.com/a/43397264-->
    
    <div class="name_display">
        <header>
            <h1 id="meh">Admin Page</h1>
        </header>
    </div>
    <form action="" method = "post">
        <div class="container">
            <button type="submit" name = logout>Log Out</button>
        </div>
    </form>
    <br>
    <br>
    <b><p id="lastWord"></p></b>
    <i><text>enter each possible word on a new line 
            (ex: der, die, das are all on different lines)</tex></i>
    <div class="container">
        <textarea id="gWords" name="message" rows="4" cols="30"
                placeholder="German Word" required></textarea>
        <textarea id="eWords" name="message" rows="4" cols="30"
                placeholder="English Word" required></textarea>
        <br>
        <button type="submit" name="submit" onClick="submitWord()">Submit New Word</button>
    </div>
</html>

<script>
    function submitWord() {
        var gTextArea = document.getElementById("gWords");
        var eTextArea = document.getElementById("eWords");
        var germanWords = checkForWord(gTextArea);
        var englishWords = checkForWord(eTextArea);
        
        if(germanWords != "" && englishWords != ""){
            window.alert("new word added");
            gTextArea.value="";
            eTextArea.value="";
            addWords(germanWords, englishWords);
        }
    }
    
    function checkForWord(textArea){
        var arrayOfLines = textArea.value.split("\n");
        var str = arrayOfLines[0];
        var i;
        for(i = 1; i < arrayOfLines.length; i++){
            str += ";" + arrayOfLines[i];
        }
        return str;
    }
    
    /* Tried using AJAX, this, everything else I could find.
     * Everything said I should be able to get the words in $_POST
     * It only worked with file_get_contents('php://input')
     * so...yea...I don't know whats going on there. I don't know this kind of stuff at all
     */
    function addWords(germanWords, englishWords){
        var data = {gWord: germanWords, eWord: englishWords};
        fetch('/newWord.php', {
            method: "POST",
            headers:{'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        });
    }
</script>