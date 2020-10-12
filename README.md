# REDIS-based Player Replication System
A simple system to replicate players between several Minecraft (Bukkit) servers, using the Redis pub/sub system.

This project was designed for the specific needs of the Kubithon event, in which we required to replicate some players over many instances of Minecraft servers. This way, content creators that partenered with the event were visible on all the game servers by all the players, allowing everyone to interact with them.

This project uses a REDIS-based architecture for dispatching the data between the different servers. A custom communication protocol was engineered for this purpose, as simply redirecting the default game-packets (which had to be reverse-engineered) generated too much traffic. Game packets are therefore intercepted, parsed, re-written for the custom protocol and sent over to REDIS, which will then handle dispatching those custom packets. They are then read by the other instances of this program on other servers, which will simulate a Minecraft player on their corresponding server.
