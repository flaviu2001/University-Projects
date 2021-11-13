using System;
using Lab4.Parser;
using System.Collections.Generic;

namespace Lab4
{
    class Program
    {
        private static readonly List<string> Urls = new()
        {
            "www.serbanology.com/",
            "www.dspcluj.ro/HTML/CORONAVIRUS/incidenta.html",
            "www.apache.org/",
            
        };

        static void Main()
        {
            Console.WriteLine("1. Callback Parser");
            Console.WriteLine("2. Task Parser");
            Console.WriteLine("3. Async Await Parser");
            string choice = Console.ReadLine();
            switch (choice)
            {
                case "1":
                    new CallbackSolution(Urls);
                    break;
                case "2":
                    new TaskSolution(Urls);
                    break;
                case "3":
                    new AsyncAwaitSolution(Urls);
                    break;
                default:
                    Console.WriteLine("Invalid choice");
                    break;
            }
        }
    }
}
