<?php
header("Access-Control-Allow-Headers: *");
header("Access-Control-Allow-Origin: *");
require_once "utils/configuration.php";
global $connection;
if (isset($_POST['id']) && !empty(trim($_POST['id']))) {
    $id = $_POST['id'];
    $sql_query = "delete from user where userID = '$id';";
    $result = mysqli_query($connection, $sql_query);
} else {
    $postdata = file_get_contents("php://input");
    $request = json_decode($postdata);
    $id = $request->id;
    $sql_query = "delete from user where userID = '$id';";
    $result = mysqli_query($connection, $sql_query);
}
mysqli_close($connection);
