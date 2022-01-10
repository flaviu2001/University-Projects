using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace DigitalRuby.RainMaker
{
    public class DemoScriptStartRainOnSpaceBar : MonoBehaviour
    {
        public BaseRainScript RainScript;

        private void Start()
        {
            if (RainScript == null)
            {
                return;
            }
            RainScript.EnableWind = false;
        }

        private void Update()
        {
            if (RainScript == null)
            {
                return;
            }
            else if (Input.GetKeyDown(KeyCode.Space))
            {
                RainScript.RainIntensity = (RainScript.RainIntensity == 0.0f ? 1.0f : 0.0f);
                RainScript.EnableWind = !RainScript.EnableWind;
            }
        }
    }
}