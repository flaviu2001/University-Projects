using UnityEngine;
using UnityEngine.UI;

namespace Behaviours
{
    public class Pause : MonoBehaviour
    {
        public static bool Paused;
        public Text text;

        private void Update()
        {
            if (Input.GetKeyDown(KeyCode.Space))
                Paused ^= true;
            text.text = Paused ? "Relax, the game is paused." : "";
        }
    }
}
