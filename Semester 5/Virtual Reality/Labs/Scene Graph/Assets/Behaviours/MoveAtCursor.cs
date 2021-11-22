using System;
using UnityEngine;

public class MoveAtCursor: MonoBehaviour
{
    private Camera _camera;
    
    private void Start()
    {
        _camera = Camera.main;
    }

    private void Update()
    {
        var mouse = Input.mousePosition;
        var fieldOfView = _camera.fieldOfView;
        const float viewPlaneWidthHalved = 0.5f;
        var up = (float)Math.Sin(Math.PI/180*(90-fieldOfView/2));
        var down = (float)Math.Sin(Math.PI/180*(fieldOfView/2));
        mouse.z = viewPlaneWidthHalved * up / down;
        var world = _camera.ScreenToWorldPoint(mouse);
        transform.position = world;
    }
}
