
<?php

require 'validate_token.php';
require 'mysql_login.php';

$conn = new mysqli($mysql_host, $mysql_user, $mysql_pass, $mysql_db);

if ($conn->connect_error) {
  exitEchoNull();
} 

if(isset($_POST["jwt"])){
  $jwt = $_POST["jwt"];
  $username=validate_key($jwt);
  
  if($username==null){
    exitEchoNull();
  }
  if(isUserExpired($conn,$username)){
    exitEchoNull();
  }
}else{
  $user=$_POST['user'];
  $sql = "SELECT * FROM employee WHERE username='".$user."' AND password='".$_POST['pass']."'";
  $result = mysqli_query($conn, $sql) or exitEchoNull();

  if(mysqli_num_rows($result)==0){
    exitEchoNull();
  }

  $jwt = encode_user($user);

  $sql = "UPDATE employee SET token_expire_time=(NOW() + INTERVAL 7 DAY) WHERE username='".$user."'";
  $result = mysqli_query($conn, $sql) or exitEchoNull();
}

echo json_encode(
  array(
      "jwt" => $jwt
  )
)."\n";

$conn->close();
?>
