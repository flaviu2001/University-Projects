<?php
header("Access-Control-Allow-Headers: *");
header("Access-Control-Allow-Origin: *");
require_once "utils/configuration.php";
if (isset($_POST['name']) && !empty(trim($_POST['name']))) {
    $userID = $_POST['id'];
    $name = $_POST['name'];
    $username = $_POST['username'];
    $password = $_POST['password'];
    $age = $_POST['age'];
    $role = $_POST['role'];
    $email = $_POST['email'];
    $webpage = $_POST['webpage'];
    $sql_query = "update user set name='$name', username = '$username', password = '$password', age = $age, role = '$role', email = '$email', webpage = '$webpage' where userID = $userID";
    global $connection;
    $result = mysqli_query($connection, $sql_query);
    mysqli_close($connection);
} else {
    $postdata = file_get_contents("php://input");
    $request = json_decode($postdata);
    $userID = $request->id;
    $name = $request->name;
    $username = $request->username;
    $password = $request->password;
    $age = $request->age;
    $role = $request->role;
    $email = $request->email;
    $webpage = $request->webpage;
    $sql_query = "update user set name='$name', username = '$username', password = '$password', age = $age, role = '$role', email = '$email', webpage = '$webpage' where userID = $userID";
    global $connection;
    $result = mysqli_query($connection, $sql_query);
    mysqli_close($connection);
}