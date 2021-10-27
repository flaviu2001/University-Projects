using System;

namespace ray_tracer
{
    class RayTracer
    {
        private Geometry[] geometries;
        private Light[] lights;

        public RayTracer(Geometry[] geometries, Light[] lights)
        {
            this.geometries = geometries;
            this.lights = lights;
        }

        private double ImageToViewPlane(int n, int imgSize, double viewPlaneSize)
        {
            var u = n * viewPlaneSize / imgSize;
            u -= viewPlaneSize / 2;
            return u;
        }

        private Intersection FindFirstIntersection(Line ray, double minDist, double maxDist)
        {
            var intersection = new Intersection();

            foreach (var geometry in geometries)
            {
                var intr = geometry.GetIntersection(ray, minDist, maxDist);

                if (!intr.Valid || !intr.Visible) continue;

                if (!intersection.Valid || !intersection.Visible)
                {
                    intersection = intr;
                }
                else if (intr.T < intersection.T)
                {
                    intersection = intr;
                }
            }

            return intersection;
        }

        private bool IsLit(Vector point, Light light)
        {
            var line = new Line(light.Position, point);
            var intersection = FindFirstIntersection(line, 0, 1000000);
            if (!intersection.Valid || !intersection.Visible)
                return true;
            return intersection.T > (light.Position - point).Length()-0.001;
        }

        public void Render(Camera camera, int width, int height, string filename)
        {
            var background = new Color();
            var image = new Image(width, height);

            for (var i = 0; i < width; i++)
            {
                for (var j = 0; j < height; j++)
                {
                    var pointOnViewPlane = camera.Position + camera.Direction * camera.ViewPlaneDistance + 
                                           (camera.Up ^ camera.Direction) * ImageToViewPlane(i, width, camera.ViewPlaneWidth) + 
                                           camera.Up * ImageToViewPlane(j, height, camera.ViewPlaneHeight);
                    var ray = new Line(camera.Position, pointOnViewPlane);
                    var intersection = FindFirstIntersection(ray, camera.FrontPlaneDistance, camera.BackPlaneDistance);
                    if (intersection.Valid && intersection.Visible)
                    {
                        var color = new Color();
                        foreach (var light in lights)
                        {
                            var colorFromLight = new Color();
                            colorFromLight += intersection.Geometry.Material.Ambient * light.Ambient;
                            if (IsLit(intersection.Position, light))
                            {
                                var v = intersection.Position;
                                var e = (camera.Position - v).Normalize();
                                var n = ((Sphere) intersection.Geometry).Normal(intersection.Position);
                                var t = (light.Position - v).Normalize();
                                var r = (n * (n * t) * 2 - t).Normalize();
                                if (n * t > 0)
                                    colorFromLight += intersection.Geometry.Material.Diffuse * light.Diffuse * (n * t);
                                if (e * r > 0)
                                    colorFromLight += intersection.Geometry.Material.Specular * light.Specular *
                                                      Math.Pow(e * r, intersection.Geometry.Material.Shininess);
                                colorFromLight *= light.Intensity;
                            }
                            color += colorFromLight;
                        }
                        image.SetPixel(i, j, color);
                    }
                    else image.SetPixel(i, j, background);
                }
            }

            image.Store(filename);
        }
    }
}