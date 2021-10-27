# Lab 1

* Define the features of your apps (write them in README)
* Implement a master-detail user interface
* Use a REST service to fetch data
* Use web sockets to receive server-side notifications 

# Lab 2

Assignments

* Use the local storage for storing data fetched from server
* Use lists with pagination (infinite scrolling)
* Authenticate user using JWT
* Use secured REST services
* Use secured web sockets

Assessment

* Show the network status (online/offline), 1p
* Authenticate users, 1p
  * After login, app stores the auth token in local storage
  * When app starts, the login page is not opened if the user is authenticated
  * App allows users to logout
* Link the resource instances to the authenticated user, 1p
  * REST services return only the resources linked to the authenticated user
  * Web socket notifications are sent only if the modified resources are linked to the authenticated user
* Online/offline behaviour, 2p
  * In online mode, the app tries first to use the REST services when new items are created/updated
  * In offline mode or if the REST calls fail, the app stores data locally
  * Inform user about the items not sent to the server
* When entering the online mode, the app automatically tries to send data to the server, 1p
* Use pagination, 2p
* Use search & filter, 1p