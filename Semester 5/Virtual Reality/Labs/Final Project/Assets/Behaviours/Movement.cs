using UnityEngine;

namespace Behaviours
{
    public class Movement : MonoBehaviour
    {
        public CharacterController controller;
    
        public float speed = 10f;

        private void Update()
        {
            if (ReduceLight.Finished && !ReduceLight.Won) return;
            var x = Input.GetAxis("Horizontal");
            var z = Input.GetAxis("Vertical");
            var transformRef = transform;
            var move = transformRef.right * x + transformRef.forward * z;
            controller.Move(move * speed * Time.deltaTime);
        }
    }
}
