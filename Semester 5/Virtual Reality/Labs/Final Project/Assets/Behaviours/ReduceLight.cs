using UnityEngine;
using UnityEngine.UI;

namespace Behaviours
{
    public class ReduceLight : MonoBehaviour
    {
        public Text text;
        public static float CurrentLight = 1;
        public static bool Finished = false;
        public static bool Won = false;
        public float speed1 = 0.1f;
        public float speed2 = 0.05f;
        public float speed3 = 0.01f;
        [SerializeField] public Light playerLight;

        private void Update()
        {
            if (Finished && Won)
            {
                playerLight.intensity = 1f;
                return;
            }
            var speed = speed1;
            if (CurrentLight < 0.1)
                speed = speed3;
            else if (CurrentLight < 0.5)
                speed = speed2;
            CurrentLight -= Time.deltaTime * speed;
            if (CurrentLight < 0)
            {
                text.text = "YOU LOST";
                CurrentLight = 0;
                Finished = true;
            }
            if (CurrentLight > 1)
                CurrentLight = 1;
            playerLight.intensity = CurrentLight;
        }
    }
}
