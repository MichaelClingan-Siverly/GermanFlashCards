<?php
    session_start();
        if(isset($_SESSION["login"]) && $_SESSION["login"] === true){
            header("Location: admin_page.php");
        }
        else if(isset($_POST["pass"])){
            $hash = "$2y$10$2linWRdokr/t7RVPfEsXn.Z5UAvNnRi41dUrqErUifFyEr66GFEIi";
            $v1 = password_verify ($_POST["pass"] , $hash );
            if($v1){
                $_SESSION["login"] = true;
                header("Location: admin_page.php");
            }
            else
                echo "password incorrect";
        }
?>

<!DOCTYPE html>
<html>
    <!--return to this page but with the new POST headers-->
    <form action="" method = "post">
        <div class="name_display">
            <header>
                <h1>Admin Login</h1>
            </header>
        </div>

        <div class="container">
            <label for="pass"><b>Password</b></label>
            <input type="password" placeholder="Enter Password" name="pass" required>
            <button type="submit">Login</button>
        </div>
    </form>
</html>