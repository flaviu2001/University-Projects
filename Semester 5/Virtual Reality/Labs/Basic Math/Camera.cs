namespace ray_tracer
{
    public class Camera
    {
        public Vector Position {get; set;}
        public Vector Direction {get; set;}
        public Vector Up {get; set;}
        
        public double ViewPlaneDistance {get; set;}
        public double ViewPlaneWidth {get; set;}
        public double ViewPlaneHeight {get; set;}
        
        public double FrontPlaneDistance {get; set;}
        public double BackPlaneDistance {get; set;}

        public Camera(Vector position, Vector direction, Vector up, double viewPlaneDistance, double viewPlaneWidth, double viewPlaneHeight, double frontPlaneDistance, double backPlaneDistance)
        {
            Position = position;
            Direction = direction;
            Up = up;
            ViewPlaneDistance = viewPlaneDistance;
            ViewPlaneWidth = viewPlaneWidth;
            ViewPlaneHeight = viewPlaneHeight;
            FrontPlaneDistance = frontPlaneDistance;
            BackPlaneDistance = backPlaneDistance;
        }

        public void Normalize()
        {
            Direction.Normalize();
            Up.Normalize();
            Up = (Direction ^ Up) ^ Direction;
        }
    }
}