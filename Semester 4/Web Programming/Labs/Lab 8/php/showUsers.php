<?php
header("Access-Control-Allow-Origin: *");
require_once 'utils/configuration.php';
$sql_query = "SELECT * FROM user;";
global $connection;
$result = mysqli_query($connection, $sql_query);

if ($result) {
    $number_of_rows = mysqli_num_rows($result);
    $requested_users = array();
    $role = $_GET["role"];
    $name = $_GET["name"];
    for ($i = 0; $i < $number_of_rows; $i++) {
        $row = mysqli_fetch_array($result);
        if (str_contains($row["role"], $role) && str_contains($row["name"], $name))
            array_push($requested_users, array(
                "id" => (int)$row['userID'],
                "name" => $row['name'],
                "username" => $row['username'],
                "age" => (int)$row['age'],
                "role" => $row['role'],
                "email" => $row['email'],
                "webpage" => $row['webpage']));
    }
    mysqli_free_result($result);
    echo json_encode($requested_users);
}
mysqli_close($connection);
