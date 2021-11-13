using System.Collections.Generic;
using System.Threading.Tasks;
using Lab4.Socket;

namespace Lab4.Parser
{
    internal class AsyncAwaitSolution : Common
    {
        protected override string ParserType => "Async/Await";

        public AsyncAwaitSolution(List<string> urls) : base(urls)
        {
        }

        protected override void Run()
        {
            var tasks = Map((index, url) => Task.Run(() =>
                Start(SocketHandler.Create(url, index))));

            Task.WhenAll(tasks).Wait();
        }

        private async Task Start(SocketHandler socket)
        {
            await socket.BeginConnectAsync();
            LogConnected(socket);

            var numberOfSentBytes = await socket.BeginSendAsync();
            LogSent(socket, numberOfSentBytes);

            await socket.BeginReceiveAsync();
            LogReceived(socket);

            socket.ShutdownAndClose();
        }
    }
}
