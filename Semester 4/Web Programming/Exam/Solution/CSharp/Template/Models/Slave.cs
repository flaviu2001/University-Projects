using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Template.Models
{
    public class Slave
    {
        public int id { get; set; }
        public int categoryID { get; set; }
        public string user { get; set; }

        public string description { get; set; }

        public long date { get; set; }

        public Slave(int id, int categoryID, string user, string description, long date)
        {
            this.id = id;
            this.categoryID = categoryID;
            this.user = user;
            this.description = description;
            this.date = date;
        }

        public Slave()
        {
        }
    }
}
