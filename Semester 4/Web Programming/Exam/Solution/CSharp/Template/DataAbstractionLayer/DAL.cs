using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using Template.Models;

namespace Template.DataAbstractionLayer
{
    public class DAL
    {
        private static String myConnectionString = "server=localhost;uid=root;pwd=;database=practic;";

        public DAL()
        {
            String statement = "create table if not exists Category (id int primary key, name varchar(100))"; //TODO: Create Master table
            executeStatement(statement);
            statement = "create table if not exists Auction (id int primary key, categoryID int references Master(id), user varchar(100), description varchar(100), date bigint)"; //TODO: Create Slave table
            executeStatement(statement);
        }

        public static void executeStatement(String statement)
        {
            MySqlConnection connection;
            try
            {
                connection = new MySqlConnection
                {
                    ConnectionString = myConnectionString
                };
                connection.Open();

                MySqlCommand cmd = new MySqlCommand
                {
                    Connection = connection,
                    CommandText = statement
                };
                cmd.ExecuteNonQuery();
            }
            catch (MySqlException ex)
            {
                Debug.Write(ex.Message);
            }
        }

        private static Master masterFromDataReader(MySqlDataReader dataReader)
        {
            return new Master(dataReader.GetInt32("id"), dataReader.GetString("name")); //TODO: Complete constructor call
        }

        private static Slave slaveFromDataReader(MySqlDataReader dataReader)
        {
            return new Slave(dataReader.GetInt32("id"), dataReader.GetInt32("categoryID"), dataReader.GetString("user"), dataReader.GetString("description"), dataReader.GetInt64("date")); //TODO: Complete constructor call
        }

        public static List<Slave> getSlavesFromStatement(String statement)
        {
            List<Slave> result = new List<Slave>();
            MySqlConnection connection;
            try
            {
                connection = new MySqlConnection
                {
                    ConnectionString = myConnectionString
                };
                connection.Open();

                MySqlCommand cmd = new MySqlCommand
                {
                    Connection = connection,
                    CommandText = statement
                };
                MySqlDataReader dataReader = cmd.ExecuteReader();
                while (dataReader.Read())
                {
                    Slave slave = slaveFromDataReader(dataReader);
                    result.Add(slave);
                }
                dataReader.Close();
            }
            catch (MySqlException ex)
            {
                Debug.Write(ex.Message);
            }
            return result;
        }

        public static List<Master> getMastersFromStatement(String statement)
        {
            List<Master> result = new List<Master>();
            MySqlConnection connection;
            try
            {
                connection = new MySqlConnection
                {
                    ConnectionString = myConnectionString
                };
                connection.Open();

                MySqlCommand cmd = new MySqlCommand
                {
                    Connection = connection,
                    CommandText = statement
                };
                MySqlDataReader dataReader = cmd.ExecuteReader();
                while (dataReader.Read())
                {
                    Master master = masterFromDataReader(dataReader);
                    result.Add(master);
                }
                dataReader.Close();
            }
            catch (MySqlException ex)
            {
                Debug.Write(ex.Message);
            }
            return result;
        }
    }
}
