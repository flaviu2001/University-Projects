namespace ray_tracer
{
    public class Material
    {
        public Color Ambient { get; set; }
        public Color Diffuse { get; set; }
        public Color Specular { get; set; }
        public int Shininess { get; set; }

        public Material(Color ambient, Color diffuse, Color specular, int shininess)
        {
            Ambient = new Color(ambient);
            Diffuse = new Color(diffuse);
            Specular = new Color(specular);
            Shininess = shininess;
        }
    }
}