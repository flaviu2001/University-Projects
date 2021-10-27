namespace ray_tracer
{
    public class Light
    {
        public Vector Position { get; set; }
        public Color Ambient { get; set; }
        public Color Diffuse { get; set; }
        public Color Specular { get; set; }
        public double Intensity { get; set; }

        public Light(Vector position, Color ambient, Color diffuse, Color specular, double intensity)
        {
            Position = new Vector(position);
            Ambient = new Color(ambient);
            Diffuse = new Color(diffuse);
            Specular = new Color(specular);
            Intensity = intensity;
        }
    }
}