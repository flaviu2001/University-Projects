using System;
using System.Collections.Generic;
using System.Diagnostics;
using Lab10Core.Models;
using MySql.Data.MySqlClient;

namespace Lab10Core.DataAbstractionLayer
{
    public class DAL
    {
        public List<User> GetUsers() {
            MySqlConnection conn;
            string myConnectionString;

            myConnectionString = "server=localhost;uid=root;pwd=;database=enterprise;";
            List<User> userList = new List<User>();

            try {
                conn = new MySqlConnection {
                    ConnectionString = myConnectionString
                };
                conn.Open();

                MySqlCommand cmd = new MySqlCommand {
                    Connection = conn,
                    CommandText = "select * from appuser"
                };
                MySqlDataReader myreader = cmd.ExecuteReader();

                while (myreader.Read()) {
                    User user = new()
                    {
                        id = myreader.GetInt32("userID"),
                        name = myreader.GetString("name"),
                        username = myreader.GetString("username"),
                        password = myreader.GetString("password"),
                        age = myreader.GetInt32("age"),
                        role = myreader.GetString("role"),
                        email = myreader.GetString("email"),
                        webpage = myreader.GetString("webpage")
                    };
                    userList.Add(user);
                }
                myreader.Close();
            }
            catch (MySqlException ex) {
                Console.Write(ex.Message);
            }
            return userList;
        }

        public void addUser(User user)
        {
            MySqlConnection conn;
            string myConnectionString;

            myConnectionString = "server=localhost;uid=root;pwd=;database=enterprise;";

            try
            {
                conn = new MySqlConnection
                {
                    ConnectionString = myConnectionString
                };
                conn.Open();

                MySqlCommand cmd = new MySqlCommand
                {
                    Connection = conn,
                    CommandText = "insert into appuser(name, username, password, age, role, email, webpage) values ('" + user.name + "', '" + user.username + "', '" + user.password + "', " + user.age + ", '" + user.role + "', '" + user.email + "', '" + user.webpage + "')"
                };
                cmd.ExecuteNonQuery();
            }
            catch (MySqlException ex)
            {
                System.Diagnostics.Debug.WriteLine(ex.Message);
            }
        }

        public void deleteUser(int id)
        {
            MySqlConnection conn;
            string myConnectionString;

            myConnectionString = "server=localhost;uid=root;pwd=;database=enterprise;";

            try
            {
                conn = new MySqlConnection
                {
                    ConnectionString = myConnectionString
                };
                conn.Open();

                MySqlCommand cmd = new MySqlCommand
                {
                    Connection = conn,
                    CommandText = "delete from appuser where userID = " + id
                };
                cmd.ExecuteNonQuery();
            }
            catch (MySqlException ex)
            {
                System.Diagnostics.Debug.WriteLine(ex.Message);
            }
        }

        public void updateUser(User user)
        {
            MySqlConnection conn;
            string myConnectionString;

            myConnectionString = "server=localhost;uid=root;pwd=;database=enterprise;";

            try
            {
                conn = new MySqlConnection
                {
                    ConnectionString = myConnectionString
                };
                conn.Open();

                MySqlCommand cmd = new()
                {
                    Connection = conn,
                    CommandText = "update appuser set name='" + user.name + "', username = '" + user.username + "', password = '" + user.password + "', age = " + user.age + ", role = '" + user.role + "', email = '" + user.email + "', webpage = '" + user.webpage + "' where userID = " + user.id
                };
                Debug.WriteLine(cmd.CommandText);
                cmd.ExecuteNonQuery();
            }
            catch (MySqlException ex)
            {
                System.Diagnostics.Debug.WriteLine(ex.Message);
            }
        }

        public User getUserByUsername(string username)
        {
            if (username == null)
                return null;
            List<User> users = GetUsers();
            foreach (User user in users)
                if (user.username == username)
                    return user;
            return null;
        }
    }
}
