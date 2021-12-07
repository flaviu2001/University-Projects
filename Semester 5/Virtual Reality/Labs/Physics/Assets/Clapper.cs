using UnityEngine;

public class Clapper : MonoBehaviour
{
    private AudioSource _source;

    private void Start()
    {
        _source = GetComponent<AudioSource>();
    }
    
    private void OnCollisionEnter(Collision collision)
    {
        _source.Play();
    }
}
