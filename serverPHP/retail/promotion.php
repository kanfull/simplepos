<?php
  require 'validate_token.php';
  require 'mysql_login.php';

  $token=$_POST["jwt"];

  $username=validate_key($token);

  if($username==null){
    die("null"."\n");
  }

  $conn = new mysqli($mysql_host, $mysql_user, $mysql_pass, $mysql_db);
    
  if ($conn->connect_error) {
    die("null"."\n");
  } 

  if(isUserExpired($conn,$username)){
    die("null"."\n");
  }

  $sql = "SELECT promotion_id,product_id,promotion_price,valid,UNIX_TIMESTAMP(NOW()) as update_time,UNIX_TIMESTAMP(NOW()) as created_time FROM promotion WHERE update_time >= ".intval($_POST["dt"]);
  $result = mysqli_query($conn, $sql) or die("Error in Selecting " . mysqli_error($conn));

  $array = array();
  while($row =mysqli_fetch_assoc($result))
  {
      $array[] = $row;
  }
  echo json_encode($array);

  $conn->close();
?>

