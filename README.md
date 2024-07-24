Here are some required things more project:

1. Creating clients for Microservice (customer-service, payment-service, order-service, product-service);
2. Enabling service-account credentials for them 
3. Configuring all client-id and client-secret in application.yml files for each project I have mentioned above;
4. Giving manage-users realm-access for customer-service client, so it can work as a ADMIN 
5. By default, user 'admin' is created with username and password both with value "admin";
6. In notification service, it is necessary to create gmail 2-step verification password and configure it in application.yml 