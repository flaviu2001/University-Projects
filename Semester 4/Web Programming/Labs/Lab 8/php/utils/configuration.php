<?php
$dbServerName = "localhost";
$dbUsername = "root";
$dbPassword = "";
$dbName = "enterprise";
$connection = mysqli_connect($dbServerName, $dbUsername, $dbPassword, $dbName);
if ($connection === false) {
    die("ERROR: Could not connect." . mysqli_connect_error());
}
