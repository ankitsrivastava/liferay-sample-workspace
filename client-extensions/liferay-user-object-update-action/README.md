# liferay-user-object-update-action

Object action client extensions define external action handlers that bind to an object definition’s action events. Your microservice’s handler code is executed when the object action occurs.

This example demonstrates user object action, and after it is deployed, you can utilise the object action UI to connect it to user object add/update events. The written logic will be carried out each time a user object is modified. For instance, it will assign a role through Headless API to the user. You have the option to modify the roleId or create custom logic that will be carried out for your use case.

