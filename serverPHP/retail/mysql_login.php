<?php

$mysql_host="localhost";
$mysql_user="root";
$mysql_pass="password";
$mysql_db="retail";

function isUserExpired($conn,$username) {
    $sql = "SELECT * FROM employee WHERE username='".$username."' AND token_expire_time>NOW()";
    $result = mysqli_query($conn, $sql) or die("null"."\n");

    return (mysqli_num_rows($result)==0);
}

function exitEchoNull(){
    die("null"."\n");
}

?>
