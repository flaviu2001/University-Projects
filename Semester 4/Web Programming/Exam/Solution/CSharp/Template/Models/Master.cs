using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Template.Models
{
    public class Master
    {
        public int id { get; set; }

        public string name { get; set; }

        public Master(int id, string name)
        {
            this.id = id;
            this.name = name;
        }

        public Master()
        {
        }
    }
}
