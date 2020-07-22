<?php

require 'validate_token.php';
require 'mysql_login.php';

$token=$_POST["jwt"];

$username=validate_key($token);

if($username==null){
  exitEchoNull();
}

$conn = new mysqli($mysql_host, $mysql_user, $mysql_pass, $mysql_db);
  
if ($conn->connect_error) {
  exitEchoNull();
} 

if(isUserExpired($conn,$username)){
  exitEchoNull();
}

$dtstring = date('Y-m-d H:i:s', intval($_POST["dt"])); 

$sql = "SELECT product_id,product_name,UNIX_TIMESTAMP(NOW()) as update_time,UNIX_TIMESTAMP(NOW()) as created_time FROM product WHERE update_time >= '".$dtstring."'";
$result = mysqli_query($conn, $sql) or die("Error in Selecting " . mysqli_error($conn));

$array = array();
while($row =mysqli_fetch_assoc($result))
{
    $array[] = $row;
}
echo json_encode($array)."\n";

$conn->close();

?>
