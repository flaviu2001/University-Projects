using UnityEngine;

namespace Behaviours
{
    public class Absorb : MonoBehaviour
    {
        private void OnCollisionEnter(Collision collision)
        {
            Destroy(gameObject);
            if (!ReduceLight.Finished)
                ReduceLight.CurrentLight += 1f;
        }
    }
}
