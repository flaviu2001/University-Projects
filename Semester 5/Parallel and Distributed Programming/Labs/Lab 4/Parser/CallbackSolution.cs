using System.Collections.Generic;
using System.Threading;
using Lab4.Socket;

namespace Lab4.Parser
{
    internal class CallbackSolution : Common
    {
        protected override string ParserType => "Callback";

        public CallbackSolution(List<string> urls) : base(urls)
        {
        }

        protected override void Run()
        {
            ForEach((index, url) => Start(SocketHandler.Create(url, index)));
        }

        private void Start(SocketHandler socket)
        {
            socket.BeginConnect(HandleConnected);
            do
            {
                Thread.Sleep(100);
            }
            while (socket.Connected);
        }

        private void HandleConnected(SocketHandler socket)
        {
            LogConnected(socket);
            socket.BeginSend(HandleSent);
        }

        private void HandleSent(SocketHandler socket, int numberOfSentBytes)
        {
            LogSent(socket, numberOfSentBytes);
            socket.BeginReceive(HandleReceived);
        }

        private void HandleReceived(SocketHandler socket)
        {
            LogReceived(socket);
            socket.ShutdownAndClose();
        }
    }
}
