using System;
using System.Collections.Generic;
using System.Linq;
using Lab4.Socket;

namespace Lab4.Parser
{
    internal abstract class Common
    {
        private List<string> Urls { get; }

        protected abstract string ParserType { get; }

        protected Common(List<string> urls)
        {
            Urls = urls;
            Run();
        }

        protected void ForEach(Action<int, string> action)
        {
            var count = 0;
            Urls.ForEach(url => action(count++, url));
        }

        protected List<T> Map<T>(Func<int, string, T> mapper)
        {
            var count = 0;
            return Urls.Select(url => mapper(count++, url)).ToList();
        }

        protected void LogConnected(SocketHandler socket)
        {
            Console.WriteLine($"{ParserType}-{socket.Id}: Socket connected to {socket.BaseUrl} ({socket.UrlPath})");
        }

        protected void LogSent(SocketHandler socket, int numberOfSentBytes)
        {
            Console.WriteLine($"{ParserType}-{socket.Id}: Sent {numberOfSentBytes} bytes to server.");
        }

        protected void LogReceived(SocketHandler socket)
        {
            Console.WriteLine(socket.GetResponseContent);
        }

        protected abstract void Run();
    }
}
