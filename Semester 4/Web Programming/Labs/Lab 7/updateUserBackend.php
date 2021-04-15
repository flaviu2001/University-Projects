<?php
require_once "utils/configuration.php";
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
if ($result) {
    echo "Your user was updated successfully!";
    header("Location: showUsers.html");
} else {
    echo "Oops!Something went wrong and your document cannot be added!Please try again later.";
}
mysqli_close($connection);