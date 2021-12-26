using UnityEngine;
using UnityEngine.UI;

namespace Behaviours
{
    public class Win : MonoBehaviour
    {
        public Text text;

        private void OnCollisionEnter(Collision collision)
        {
            if (!collision.gameObject.name.Equals("Robot"))
                return;
            text.text = "YOU WON!";
            ReduceLight.Won = true;
            ReduceLight.Finished = true;
            Destroy(gameObject);
            ReduceLight.CurrentLight += 1f;
        }
    }
}
