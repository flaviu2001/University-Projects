namespace ray_tracer
{
    public class Line
    {
        public Vector X0 { get; set; }
        public Vector Dx { get; set; }

        public Line(Vector x0, Vector x1)
        {
            X0 = new Vector(x0);
            Dx = new Vector(x1 - x0);
            Dx.Normalize();
        }

        public Vector CoordinateToPosition(double t)
        {
            return new Vector(Dx * t + X0);
        }
    }
}