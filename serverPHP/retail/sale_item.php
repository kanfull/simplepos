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

  $sql = "insert into sale_item VALUES (NULL,".$_POST['sale_id']
  ." ,".$_POST['product_id'].",".$_POST['price_id'] 
  ." ,".$_POST['promotion_id'].",".$_POST['sale_price']
  .",".$_POST['unit']
  .",NOW(),NOW())";

  if (!$conn->query($sql) == TRUE) {
    echo "Error: " . $sql . "<br>" . $conn->error;
  }
  echo $conn->insert_id."\n";

  $conn->close();
?>