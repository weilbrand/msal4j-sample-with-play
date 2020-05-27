# msal4j-sample-with-play

Disclaimer: This code is not meant for production!

## Projects

The repo contains two projects:

#### foobar-api

This project is based on the Play framework (Scala) and shows one approach how to handle JWT and extracting claims. Here you can also verify the token if needed.

#### TelemetryClient

This project is a Java console application and acts as a deamon service. It uses msal4j to authenticate on his own behalf agains Azure AD and calls the endpoint of foobar-api.
