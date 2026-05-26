# Hotel Pricing System Case Study

## Design purpose

This is a greenfield system that replaces an existing system. The design purpose is to make initial architectural decisions that support building the replacement system from scratch.

## Primary functionality

- HPS-1 Log In: a commercial or administrator user provides credentials, the system validates them against a user identity service, and the system grants access only to authorized hotel data and functions.
- HPS-2 Change Prices: an authorized user selects a hotel and dates, changes either a base rate or a fixed rate, runs simulation before applying changes, and publishes final prices to the Channel Management System so external systems can query them.
- HPS-3 Query Prices: a user or an external system queries prices for a given hotel through the UI or a query API.
- HPS-4 Manage Hotels: an administrator adds, changes, or modifies hotel information, including tax rates, available rates, and room types.
- HPS-5 Manage Rates: an administrator adds, changes, or modifies rates, including the business rules used to calculate rates.
- HPS-6 Manage Users: an administrator changes permissions for a given user.

## Quality attributes

- QA-1 Performance: when a base rate price changes for a specific hotel and date during normal operation, all prices for all rates and room types for the hotel must be published and ready for query in less than 100 ms.
- QA-2 Reliability: when a user performs multiple price changes on a hotel, 100 percent of price changes must be published successfully and must also be received by the Channel Management System.
- QA-3 Availability: pricing query uptime SLA must be 99.9 percent outside maintenance windows.
- QA-4 Scalability: the system initially supports at least 100,000 API price queries per day and can scale to 1,000,000 without average latency degrading by more than 20 percent.
- QA-5 Security: user credentials are validated against the User Identity Service and each user only sees authorized functions.
- QA-6 Modifiability: a non-REST price query endpoint, such as gRPC, can be added without changing core components.
- QA-7 Deployability: the application can move between non-production environments without code changes.
- QA-8 Monitorability: operators can collect 100 percent of performance and reliability measures for price publication.
- QA-9 Testability: 100 percent of the system and its elements support integration testing independently of external systems.

## Architectural concerns

- CRN-1 Establish an overall initial system structure.
- CRN-2 Leverage the team's knowledge about Java technologies, the Angular framework, and Kafka.
- CRN-3 Allocate work to members of the development team.
- CRN-4 Avoid introducing technical debt.
- CRN-5 Set up a continuous deployment infrastructure.

## Constraints

- CON-1 Users must interact with the system through a web browser on multiple platforms and devices.
- CON-2 Manage users through a cloud provider identity service and host resources in the cloud.
- CON-3 Code must be hosted on a proprietary Git-based platform already used by the company.
- CON-4 The initial release must be delivered in six months and an MVP must be demonstrated within two months.
- CON-5 The system must initially integrate with existing systems through REST APIs but may later support other protocols.
- CON-6 A cloud-native approach should be favored.
