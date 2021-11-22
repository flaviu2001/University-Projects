using System;
using UnityEngine;

public class PointToCursor : MonoBehaviour{

    private Camera _camera;
    // Start is called before the first frame update
    private void Start()
    {
        _camera = Camera.main;
    }

    // Update is called once per frame
    private void Update()
    {
        var mouse = Input.mousePosition;
        var fieldOfView = _camera.fieldOfView;
        const float viewPlaneWidthHalved = 0.5f;
        var numerator = (float)Math.Sin(Math.PI/180*(90-fieldOfView/2));
        var denominator = (float)Math.Sin(Math.PI/180*(fieldOfView/2));
        mouse.z = viewPlaneWidthHalved * numerator / denominator;
        var world = _camera.ScreenToWorldPoint(mouse);
        transform.LookAt(world);
    }
}
