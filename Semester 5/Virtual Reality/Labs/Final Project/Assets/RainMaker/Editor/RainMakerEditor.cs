//
// Rain Maker (c) 2015 Digital Ruby, LLC
// http://www.digitalruby.com
//

using System;
using RainMaker.Prefab;
using UnityEngine;
using UnityEditor;

namespace DigitalRuby.RainMaker
{
    public class RainMakerEditor : Editor
    {
        private Texture2D logo;

        public override void OnInspectorGUI()
        {
            if (logo == null)
            {
                string[] guids = AssetDatabase.FindAssets("RainMakerLogo");
                foreach (string guid in guids)
                {
                    string path = AssetDatabase.GUIDToAssetPath(guid);
                    logo = AssetDatabase.LoadMainAssetAtPath(path) as Texture2D;
                    if (logo != null)
                    {
                        break;
                    }
                }
            }
            if (logo != null)
            {
                const float maxLogoWidth = 430.0f;
                EditorGUILayout.Separator();
                float w = EditorGUIUtility.currentViewWidth;
                Rect r = new Rect();
                r.width = Math.Min(w - 40.0f, maxLogoWidth);
                r.height = r.width / 2.7f;
                Rect r2 = GUILayoutUtility.GetRect(r.width, r.height);
                r.x = ((EditorGUIUtility.currentViewWidth - r.width) * 0.5f) - 4.0f;
                r.y = r2.y;
                GUI.DrawTexture(r, logo, ScaleMode.StretchToFill);
                if (GUI.Button(r, "", new GUIStyle()))
                {
                    Application.OpenURL("https://www.assetstore.unity3d.com/en/#!/content/60955?aid=1011lGnL");
                }
                EditorGUILayout.Separator();
            }

            DrawDefaultInspector();
        }
    }

    [CustomEditor(typeof(RainScript))]
    public class RainMakerEditor3D : RainMakerEditor
    {
    }

    [CustomEditor(typeof(RainScript2D))]
    public class RainMakerEditor2D : RainMakerEditor
    {
    }
}