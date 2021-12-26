using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LookPlayer : MonoBehaviour
{
    private Vector2 _rotation = Vector2.zero;
    public float speed = 10;
    
    private void Start()
    {
        Cursor.lockState = CursorLockMode.Locked;    
    }
    
    private void Update()
    {
        _rotation.y += Input.GetAxis("Mouse X");
        transform.eulerAngles = _rotation * speed;
    }
}
