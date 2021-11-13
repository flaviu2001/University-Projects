using System;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace Lab4.Socket
{
    internal class SocketHandler : System.Net.Sockets.Socket
    {
        public int Id { get; }

        public string BaseUrl { get; }

        public string UrlPath { get; }

        private IPEndPoint EndPoint { get; }

        private StringBuilder ResponseContent { get; } = new();

        private const int DefaultHttpPort = 80;
        private const int BufferSize = 1024;

        public static SocketHandler Create(string url, int id)
        {
            var index = url.IndexOf('/');
            var baseUrl = index < 0 ? url : url[..index];
            var urlPath = index < 0 ? "/" : url[index..];

            var ipHostInformation = Dns.GetHostEntry(baseUrl);
            var ipAddress = ipHostInformation.AddressList[0];

            return new SocketHandler(baseUrl, urlPath, ipAddress, id);
        }

        private SocketHandler(string baseUrl, string urlPath, IPAddress ipAddress, int id):
            base(ipAddress.AddressFamily, SocketType.Stream, ProtocolType.Tcp)
        {
            Id = id;
            BaseUrl = baseUrl;
            UrlPath = urlPath;
            EndPoint = new IPEndPoint(ipAddress, DefaultHttpPort);
        }

        public void BeginConnect(Action<SocketHandler> onConnected)
        {
            BeginConnect(EndPoint, asyncResult => {
                EndConnect(asyncResult);
                onConnected(this);
            }, null);
        }

        public void BeginSend(Action<SocketHandler, int> onSent)
        {
            var stringToSend = $"GET {UrlPath} HTTP/1.1\r\n" +
                $"Host: {BaseUrl}\r\n" +
                "Content-Length: 0\r\n\r\n";
            var encodedString = Encoding.ASCII.GetBytes(stringToSend);

            BeginSend(encodedString, 0, encodedString.Length, SocketFlags.None, asyncResult => {
                var numberOfSentBytes = EndSend(asyncResult);
                onSent(this, numberOfSentBytes);
            }, null);
        }

        public void BeginReceive(Action<SocketHandler> onReceived)
        {
            var buffer = new byte[BufferSize];
            ResponseContent.Clear();

            BeginReceive(buffer, 0, BufferSize, SocketFlags.None, asyncResult =>
                HandleReceiveResult(asyncResult, buffer, onReceived), null);
        }

        public Task BeginConnectAsync() => Task.Run(() =>
        {
            var taskCompletion = new TaskCompletionSource();

            BeginConnect(_ => { taskCompletion.TrySetResult();});

            return taskCompletion.Task;
        });

        public Task<int> BeginSendAsync() => Task.Run(() =>
        {
            var taskCompletion = new TaskCompletionSource<int>();

            BeginSend((_, numberOfSentBytes) =>
                taskCompletion.TrySetResult(numberOfSentBytes));

            return taskCompletion.Task;
        });

        public Task BeginReceiveAsync() => Task.Run(() =>
        {
            var taskCompletion = new TaskCompletionSource();

            BeginReceive(_ => taskCompletion.TrySetResult());

            return taskCompletion.Task;
        });

        public void ShutdownAndClose()
        {
            Shutdown(SocketShutdown.Both);
            Close();
        }

        public string GetResponseContent => ResponseContent.ToString();
        
        private void HandleReceiveResult(
            IAsyncResult asyncResult,
            byte[] buffer,
            Action<SocketHandler> onReceived)
        {
            var numberOfReadBytes = EndReceive(asyncResult);
            ResponseContent.Append(Encoding.ASCII.GetString(buffer, 0, numberOfReadBytes));
            if (!ResponseContent.ToString().Contains("</html>"))
            {
                BeginReceive(buffer, 0, BufferSize, SocketFlags.None, asyncResult2 =>
                    HandleReceiveResult(asyncResult2, buffer, onReceived), null);
                return;
            }

            onReceived(this);
        }
    }
}
