<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Add New User</title>
    <style>
        <?php include "addUser.css" ?>
    </style>
</head>

<body>
<div class="container">
    <h1>Create User</h1>
    <p><b>Please fill this form and submit to add the user to the database.</b></p>

    <form action="addUserBackend.php" method="post">
        <input type="text" name="name" placeholder="Name:"> <br>
        <input type="text" name="username" placeholder="Username:"> <br>
        <input type="password" name="password" placeholder="Password:"> <br>
        <input type="number" name="age" placeholder="Age:"> <br>
        <input type="text" name="role" placeholder="Role:"> <br>
        <input type="text" name="email" placeholder="Email:"> <br>
        <input type="text" name="webpage" placeholder="Webpage:"> <br>
        <div class="button_container">
            <button type="submit">Add User</button>
            <button type="reset" onclick="window.location.href='showUsers.html'">Cancel</button>
        </div>
    </form>
</div>
</body>

</html>