namespace ray_tracer
{
    public class Intersection
    {
        public bool Valid{ get; set; }
        public bool Visible{ get; set; }
        public double T{ get; set; }
        public Vector Position{ get; set; }
        public Geometry Geometry{ get; set; }
        public Line Line{ get; set; }

        public Intersection() {
            Geometry = null;
            Line = null;
            Valid = false;
            Visible = false;
            T = 0;
            Position = null;
        }

        public Intersection(bool valid, bool visible, Geometry geometry, Line line, double t) {
            Geometry = geometry;
            Line = line;
            Valid = valid;
            Visible = visible;
            T = t;
            Position = Line.CoordinateToPosition(t);
        }
    }
}