using UnityEngine;

public class Cannon : MonoBehaviour
{
    [SerializeField] public Rigidbody projectile;
    private Camera _camera;
    // Start is called before the first frame update


    private void Start()
    {
        _camera = Camera.main;
    }
    
    // Update is called once per frame
    private void Update()
    {
        if (!Input.GetMouseButton(0)) return;
        var ray = _camera.ScreenPointToRay(Input.mousePosition);
        if (Physics.Raycast(ray, out var hit))
            FireAtPoint(hit.point);
    }

    private void FireAtPoint(Vector3 point)
    {
        var transformPosition = transform.position;
        var velocity = (point - transformPosition)*5;
        Debug.Log(velocity);
        projectile.transform.position = transformPosition;
        projectile.velocity = velocity;
    }
}
