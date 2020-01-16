## Overview
This project was developed to understand how websocket works with IAM and integrated with Spring Security.

## Goals
The main goal is create one chat that handle an millions simultaneous connections, distributed and spread on multiples 
servers using stateless authentication with JWT.

Once this project implemeted new ideas for stateless connection using websocket could be used.

## Requirements
An minimum Spring Boot knowledgement.

## Stack
* Keycloak (IAM)
* Java 11
* Spring Boot
* Spring Security
* Spring Data Redis
* Spring WebSocket with STOMP Protocol
* RabbitMQ with STOMP Plugin
* Redis