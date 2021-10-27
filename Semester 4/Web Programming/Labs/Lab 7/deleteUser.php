<!DOCTYPE html>

<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Delete User</title>
    <link rel="stylesheet" type="text/css" href="deleteUser.css">
</head>

<body>
<h1>Delete User</h1>

<div class="container">
    <p><b>Are you sure you want to delete this user?</b></p>

    <form action="deleteUserBackend.php" method="post">
        <input type="hidden" name="id" value="<?php echo trim($_GET['id']); ?>">
        <button type="submit" class="yes">YES</button>
    </form>
    <button class="no" onclick="window.location.href='showUsers.html'">
        NO
    </button>
</div>
</body>

</html>