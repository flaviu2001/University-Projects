using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Lab10Core.Models
{
    public class User
    {
        public int id { get; set; }

        public string name { get; set; }

        public string username { get; set; }

        public string password { get; set; }

        public int age { get; set; }

        public string role { get; set; }

        public string email { get; set; }

        public string webpage { get; set; }
    }
}