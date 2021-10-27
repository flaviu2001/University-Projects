using System;
using System.IO;
using System.Threading.Tasks;

namespace ray_tracer
{
    public class Program
    {
        public static void Main(string[] args)
        {
            // Cleanup
            const string frames = "frames";
            if (Directory.Exists(frames))
            {
                var d = new DirectoryInfo(frames);
                foreach (var file in d.EnumerateFiles("*.png")) {
                    file.Delete();
                }
            }
            Directory.CreateDirectory(frames);

            // Scene
            var geometries = new Geometry[]
            {
                new Sphere(
                    new Vector(-50.0, -25.0, 175.0),
                    30.0,
                    new Material(
                        new Color(0.1, 0.0, 0.0, 1.0),
                        new Color(0.3, 0.0, 0.0, 1.0),
                        new Color(0.5, 0.0, 0.0, 1.0),
                        10
                    ),
                    new Color(0.8, 0.0, 0.0, 1.0)),
                new Sphere(
                    new Vector(-10.0, 0.0, 100.0),
                    10.0,
                    new Material(
                        new Color(0.1, 0.1, 0.0, 1.0),
                        new Color(0.3, 0.3, 0.0, 1.0),
                        new Color(0.5, 0.5, 0.0, 1.0),
                        10
                    ),
                    new Color(0.8, 0.8, 0.0, 1.0)
                ),
                new Sphere(
                    new Vector(0.0, 0.0, 200.0),
                    40.0,
                    new Material(
                        new Color(0.0, 0.1, 0.0, 1.0),
                        new Color(0.0, 0.3, 0.0, 1.0),
                        new Color(0.0, 0.5, 0.5, 1.0),
                        10
                    ),
                    new Color(0.0, 0.8, 0.0, 1.0)
                ),
                new Sphere(new Vector(0.0, -50.0, 200.0),
                    10.0,
                    new Material(
                        new Color(0.1, 0.1, 0.1, 1.0),
                        new Color(0.3, 0.3, 0.3, 1.0),
                        new Color(0.5, 0.5, 0.5, 1.0),
                        10
                    ),
                    new Color(0.8, 0.8, 0.8, 1.0)
                ),
                new Sphere(
                    new Vector(10.0, 0.0, 20.0),
                    5.0,
                    new Material(
                        new Color(0.0, 0.1, 0.1, 1.0),
                        new Color(0.0, 0.3, 0.3, 1.0),
                        new Color(0.0, 0.5, 0.5, 1.0),
                        10
                    ),
                    new Color(0.0, 0.8, 0.8, 1.0)
                ),
                new Sphere(
                    new Vector(-70.0, 0.0, 100.0),
                    10.0,
                    new Material(
                        new Color(0.1, 0.0, 0.1, 1.0),
                        new Color(0.3, 0.0, 0.3, 1.0),
                        new Color(0.5, 0.0, 0.5, 1.0),
                        10
                    ),
                    new Color(0.8, 0.0, 0.8, 1.0)
                ),
                new Sphere(
                    new Vector(50.0, 25.0, 75.0),
                    50.0,
                    new Material(
                        new Color(0.0, 0.0, 0.1, 1.0),
                        new Color(0.0, 0.0, 0.3, 1.0),
                        new Color(0.0, 0.0, 0.5, 1.0),
                        10
                    ),
                    new Color(0.0, 0.0, 0.8, 1.0)
                ),
                new Sphere(
                    new Vector(-75.0, 15.0, -75.0),
                    5.0,
                    new Material(
                        new Color(0.07, 0.07, 0.07, 1.0),
                        new Color(0.2, 0.2, 0.2, 1.0),
                        new Color(0.3, 0.4, 0.4, 1.0),
                        10
                    ),
                    new Color(0.07, 0.07, 0.07, 1.0)
                )
            };

            var lights = new Light[]
            {
                new Light(
                    new Vector(-50.0, 0.0, 0.0),
                    new Color(0.8, 0.8, 0.8, 1.0),
                    new Color(0.8, 0.8, 0.8, 1.0),
                    new Color(0.8, 0.8, 0.8, 1.0),
                    1.0
                ),
                new Light(
                    new Vector(20.0, 20.0, 0.0),
                    new Color(0.8, 0.8, 0.8, 1.0),
                    new Color(0.8, 0.8, 0.8, 1.0),
                    new Color(0.8, 0.8, 0.8, 1.0),
                    1.0
                ),
                new Light(
                    new Vector(0.0, 0.0, 300.0),
                    new Color(0.8, 0.8, 0.8, 1.0),
                    new Color(0.8, 0.8, 0.8, 1.0),
                    new Color(0.8, 0.8, 0.8, 1.0),
                    1.0
                )
            };
            var rt = new RayTracer(geometries, lights);

            const int width = 800;
            const int height = 600;

            // Go around an approximate middle of the scene and generate frames
            var middle = new Vector(0.0, 0.0, 100.0);
            var up = new Vector(-Math.Sqrt(0.125), -Math.Sqrt(0.75), Math.Sqrt(0.125)).Normalize();
            var first = (middle ^ up).Normalize();
            const double dist = 150.0;
            const int n = 90;
            const double step = 360.0 / n;

            var tasks = new Task[n];
            for (var i = 0; i < n; i++)
            {
                var ind = new[]{i};
                tasks[i] = Task.Run(() =>
                {
                    var k = ind[0];
                    var a = (step * k) * Math.PI / 180.0;
                    var ca =  Math.Cos(a);
                    var sa =  Math.Sin(a);

                    var dir = first * ca + (up ^ first) * sa + up * (up * first) * (1.0 - ca);

                    var camera = new Camera(
                        middle + dir * dist,
                        dir * -1.0,
                        up,
                        65.0,
                        160.0,
                        120.0,
                        0.0,
                        1000.0
                    );

                    var filename = frames+"/" + $"{k + 1:000}" + ".png";

                    rt.Render(camera, width, height, filename);
                    Console.WriteLine($"Frame {k+1}/{n} completed");
                });
            }

            Task.WaitAll(tasks);
        }
    }
}