using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System.Collections.Generic;
using System.Diagnostics;
using Template.DataAbstractionLayer;
using Template.Models;

namespace Template.Controllers
{
    public class HomeController : Controller
    {
        private readonly ILogger<HomeController> _logger;

        public HomeController(ILogger<HomeController> logger)
        {
            _logger = logger;
        }

        [HttpGet("login")]
        public IActionResult Login(string name)
        {
            DAL dal = new DAL();
            List<string> names = new List<string>();
            names.Add("john");
            names.Add("jack");
            names.Add("jill");
            if (names.Contains(name))
                return Ok(1);
            return Ok(1); // Allow them anyway
        }

        [HttpGet("slave")]
        public IActionResult GetSlave()
        {
            DAL dal = new DAL();
            List<Slave> slaves = DAL.getSlavesFromStatement("select * from Auction"); //TODO: Write select
            return Ok(slaves);
        }

        [HttpPost("slave")]
        public IActionResult PostSlave(string categoryName, [FromBody] Slave auction)
        {
            DAL dal = new DAL();
            List<Master> masters = DAL.getMastersFromStatement("select * from Category where name = '" + categoryName + "'");
            int id = 0;
            if (masters.Count == 0)
            {
                List<Master> masters2 = DAL.getMastersFromStatement("select * from Category");
                foreach (Master master in masters2)
                    if (master.id + 1 > id)
                        id = master.id + 1;
                DAL.executeStatement("insert into Category values (" + id + ", '" + categoryName + "')");
            }
            else id = masters[0].id;
            int aid = 0;
            List<Slave> slaves = DAL.getSlavesFromStatement("select * from Auction");
            foreach (Slave slave in slaves)
                if (slave.id + 1 > aid)
                    aid = slave.id + 1;
            DAL.executeStatement("insert into Auction values (" + aid + ", " + id + ", '" + auction.user + "', '" + auction.description + "', " + auction.date + ")"); //TODO: Write insert
            return Ok(1);
        }

        [HttpPut("slave")]
        public IActionResult PutSlave(string name, [FromBody] Slave auction)
        {
            DAL dal = new DAL();
            DAL.executeStatement("update Auction set categoryID = " + auction.categoryID + ", description = '" + auction.description + "', date = " + auction.date + " where id = " + auction.id + " and user = '" + name + "'"); //TODO: Write update
            return Ok(1);
        }
    }
}
