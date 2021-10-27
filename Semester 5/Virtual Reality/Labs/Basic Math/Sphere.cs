using System;

namespace ray_tracer
{
    public class Sphere : Geometry
    {
        private Vector Center { get; set; }
        private double Radius { get; set; }

        public Sphere(Vector center, double radius, Material material, Color color) : base(material, color)
        {
            Center = center;
            Radius = radius;
        }

        public override Intersection GetIntersection(Line line, double minDist, double maxDist)
        {
            var a = line.Dx * line.Dx;
            var b = (line.Dx * line.X0) * 2;
            b -= (line.Dx * Center) * 2;
            var c = (line.X0 * line.X0) + (Center * Center) - (Radius * Radius) - (line.X0 * Center) * 2;
            var discriminant = (b * b) - (a * c * 4.0);
            if (discriminant < 0.001)
                return new Intersection(false, false, this, line, 0);
            var t1 = -b - Math.Sqrt(discriminant);
            var t2 = -b + Math.Sqrt(discriminant);
            t1 /= 2.0 * a;
            t2 /= 2.0 * a;
            var validT1 = t1 >= minDist && t1 <= maxDist;
            var validT2 = t2 >= minDist && t2 <= maxDist;
            if (!validT1 && !validT2)
                return new Intersection(false, false, this, line, 0);
            if (validT1 && !validT2)
                return new Intersection(true, true, this, line, t1); 
            if (!validT1)
                return new Intersection(true, true, this, line, t2);
            var mn = t1;
            if (t2 < mn)
                mn = t2;
            return new Intersection(true, true, this, line, mn);
        }

        public override Vector Normal(Vector v)
        {
            var n = v - Center;
            n.Normalize();
            return n;
        }
    }
}