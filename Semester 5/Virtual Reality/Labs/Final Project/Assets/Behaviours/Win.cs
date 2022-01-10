using RainMaker.Prefab;
using UnityEngine;
using UnityEngine.UI;

namespace Behaviours
{
    public class Win : MonoBehaviour
    {
        public Text text;

        private void OnCollisionEnter(Collision collision)
        {
            if (!collision.gameObject.name.Equals("Player"))
                return;
            if (!ReduceLight.Finished)
            {
                ReduceLight.Won = true;
                ReduceLight.Finished = true;   
            }
            Destroy(gameObject);
        }
    }
}
