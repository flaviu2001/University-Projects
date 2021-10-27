<?php
require_once "utils/configuration.php";
global $connection;
if (isset($_POST['id']) && !empty(trim($_POST['id']))) {
    $id = $_POST['id'];
    $sql_query = "delete from user where userID = '$id';";
    $result = mysqli_query($connection, $sql_query);
    if ($result) {
        echo "Your user was deleted successfully!";
        header("Location: showUsers.html");
    } else {
        echo "Oops! Something went wrong and your user cannot be deleted! Please try again later.";
    }
}
mysqli_close($connection);
