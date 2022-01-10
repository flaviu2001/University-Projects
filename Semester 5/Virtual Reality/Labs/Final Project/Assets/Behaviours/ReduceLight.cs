using UnityEngine;
using UnityEngine.UI;

namespace Behaviours
{
    public class ReduceLight : MonoBehaviour
    {
        public Text text;
        public static float CurrentLight = 1;
        public static bool Finished;
        public static bool Won = false;
        private const float Speed1 = 0.05f;
        private const float Speed2 = 0.02f;
        private const float Speed3 = 0.005f;
        private const float Threshold1 = 0.5f;
        private const float Threshold2 = 0.2f;
        private const float LossLight = 0.05f;
        [SerializeField] public Light playerLight;

        private void Update()
        {
            if (Pause.Paused)
            {
                Debug.Log(1);
                playerLight.intensity = 1;
                return;
            }
            if (Finished && Won)
            {
                playerLight.intensity = CurrentLight;
                return;
            }
            var speed = Speed1;
            if (CurrentLight < Threshold2)
                speed = Speed3;
            else if (CurrentLight < Threshold1)
                speed = Speed2;
            CurrentLight -= Time.deltaTime * speed;
            if (CurrentLight < LossLight)
            {
                CurrentLight = LossLight;
                Finished = true;
            }
            if (CurrentLight > 1)
                CurrentLight = 1;
            Debug.Log(CurrentLight);
            playerLight.intensity = CurrentLight;
        }
    }
}
