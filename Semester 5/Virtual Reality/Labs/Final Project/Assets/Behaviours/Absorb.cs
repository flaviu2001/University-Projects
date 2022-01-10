using RainMaker.Prefab;
using UnityEngine;

namespace Behaviours
{
    public class Absorb : MonoBehaviour
    {
        public RainScript rs;
        private void OnCollisionEnter()
        {
            Destroy(gameObject);
            if (ReduceLight.Finished)
                return;
            if (Random.value < 0.5f)
                rs.RainIntensity += 0.25f;
            else
                ReduceLight.CurrentLight += 1f;
        }
    }
}
