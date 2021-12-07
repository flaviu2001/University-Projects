using UnityEngine;

public class WallGenerator : MonoBehaviour
{
    // Start is called before the first frame update
    private void Start()
    {
        var blueBrick = Resources.Load("Brick") as GameObject;
        var redBrick = Resources.Load("Other Brick") as GameObject;
        for (var i = -5; i < 5; i++)
        {
            for (var j = 0; j < 10; j++)
            {
                var initBrick = (i + j) % 2 == 0 ? Instantiate(redBrick) : Instantiate(blueBrick);
                var transformPosition = transform.position;
                initBrick.transform.position = new Vector3(transformPosition.x + i*1f,transformPosition.y +j*1f, transformPosition.z*1f);
            }
        }        
    }
}
