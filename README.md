# Database synchronization with kafka

Web application for synchronization of databases of the main branch and external branches of the company.

The premise of the project is to ensure consistency and synchronization of data between branches and uninterrupted operation despite the lack of communication. The solution provides asynchronous communication taking into account possible failures and problems in communication. 
Apache Kafka was used for this project, which allowed sending events about data modifications and, thanks to the registry, restoring system operation despite the interruption in communication.

