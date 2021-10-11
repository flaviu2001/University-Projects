using Lab10Core.DataAbstractionLayer;
using Lab10Core.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Diagnostics;

namespace Lab10Core.Controllers
{
    public class HomeController : Controller
    {
        private readonly ILogger<HomeController> _logger;

        public HomeController(ILogger<HomeController> logger)
        {
            _logger = logger;
        }

        [HttpGet("Home/GetUsers")]
        public IActionResult GetUsers(string role, string name)
        {
            DAL dal = new();
            if (role == null)
                role = "";
            if (name == null)
                name = "";
            List<User> users = dal.GetUsers();
            List<User> filteredUsers = new List<User>();
            foreach (User user in users)
                if (user.role.Contains(role) && user.name.Contains(name))
                    filteredUsers.Add(user);
            return Ok(filteredUsers);
        }

        [HttpGet("Home/GetUser")]
        public IActionResult GetUser(int id)
        {
            DAL dal = new();
            List<User> users = dal.GetUsers();
            User toReturn = null;
            foreach (User user in users)
                if (user.id == id)
                    toReturn = user;
            return Ok(toReturn);
        }

        [HttpPost]
        public IActionResult AddUser([FromBody] User user)
        {
            DAL dal = new();
            dal.addUser(user);
            return Ok();
        }

        [HttpDelete]
        public IActionResult DeleteUser(int id)
        {
            DAL dal = new();
            dal.deleteUser(id);
            return Ok();
        }

        [HttpPut]
        public IActionResult UpdateUser([FromBody] User user)
        {
            DAL dal = new();
            dal.updateUser(user);
            return Ok();
        }

        [HttpPost]
        public IActionResult LoginUser([FromBody] User loginUser)
        {
            DAL dal = new();
            List<User> users = dal.GetUsers();
            User toReturn = null;
            foreach (User user in users)
                if (user.username == loginUser.username && user.password == loginUser.password)
                    toReturn = user;
            return Ok(toReturn);
        }
    }
}
