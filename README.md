Here are some required things more project:

1. Creating clients for Microservice (customer_service, payment_service, order_service, product_service);
2. Enabling service-account credentials for them
3. For each client, creating a role with name suitable to client name like (customer_service client with customer_service role) and assigning these roles for client as Service account role   
4. Configuring all client-id and client-secret in application.yml files for each project I have mentioned above;
5. Giving manage-users realm-access for customer-service client, so it can work as a ADMIN 
6. By default, user 'admin' is created with username and password both with value "admin";
7. In notification service, it is necessary to create gmail 2-step verification password and configure it in application.yml 