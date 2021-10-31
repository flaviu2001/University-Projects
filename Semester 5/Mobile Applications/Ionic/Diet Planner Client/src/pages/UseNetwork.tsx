import { useEffect, useState } from 'react';
import { NetworkStatus, Plugins } from '@capacitor/core';

const { Network } = Plugins;

const initialState = {
  connected: false,
  connectionType: 'unknown',
}

export const useNetwork = () => {
  const [networkStatus, setNetworkStatus] = useState(initialState)
  useEffect(() => {
    const handler = Network.addListener('networkStatusChange', handleNetworkStatusChange);
    Network.getStatus().then(handleNetworkStatusChange);
    let canceled = false;
    return () => {
      canceled = true;
      handler.remove();
    }

    function handleNetworkStatusChange(status: NetworkStatus) {
      if (!canceled) {
        setNetworkStatus(status);
      }
    }
  }, [])
  return { networkStatus };
};
