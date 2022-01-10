using UnityEngine;

namespace Behaviours
{
    public class LookPlayer : MonoBehaviour
    {
        private Vector2 _rotation = Vector2.zero;
        private Vector2 _cameraRotation = Vector2.zero;
        private Camera _camera;
        public float speed = 5;
    
        private void Start()
        {
            Cursor.lockState = CursorLockMode.Locked;
            _camera = Camera.main;
        }
    
        private void Update()
        {
            _rotation.y += Input.GetAxis("Mouse X");
            _cameraRotation.x -= Input.GetAxis("Mouse Y");
            if (_cameraRotation.x*speed < -60.0f)
                _cameraRotation.x = -60.0f/speed;
            if (_cameraRotation.x*speed > 60.0f)
                _cameraRotation.x = 60.0f/speed;
            _cameraRotation.y = _rotation.y;
            transform.eulerAngles = _rotation * speed;
            _camera.transform.eulerAngles = (_cameraRotation * speed);
        }
    }
}
