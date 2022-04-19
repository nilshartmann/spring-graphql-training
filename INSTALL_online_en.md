# Spring GraphQL Training

Here you will find everything you need to know to prepare for the GraphQL training.

## Prerequisites

**For your laptop/PC**

On your laptop/PC you should have installed:

- Git (for cloning the workspace)
- Java JDK (at least version 11)
- Docker
- A browser
- A Java IDE, for example [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) (evaluation version is sufficient)
- For trainings via **Zoom**: please install the Zoom **Client** (and do not use the web version of Zoom). You _do not_ need a Zoom account.

**During the training**

- As we may need to install updates before and during the training, please make sure that internet access is working (of course, online training 🙃) on your computer during the training as well **for git, docker and gradle** (consider proxies!).
- **I'm happy if you have your camera on** during the training so that we can see each other 🎥. Please only turn on the microphone if you want to say something or ask a question (which you are of course allowed to do at any time!).
- W-LAN is convenient, but especially for (long) streaming sessions, a wired network is more stable than W-LAN, so if in doubt, plug in the cable (and deactivate W-LAN) 😊

## Installation and preparation of the workspace for training

To save time during the workshop, it would be great if you can install the workspace before.

## Ports

The application requires the following ports:

- 8442 (Docker/Postgres)
- 8090 (Backend for the GraphQL API)
- 8091 (UserService)

Please make sure that these ports are not in use.

## Step 1: Clone the repository

1. clone the repository:

```
git clone https://github.com/nilshartmann/spring-graphql-training
```

## Step 2: Start Docker

In the `workspace` directory of the repository, start the database with `docker-compose`:

```
cd workspace

docker-compose up -d
```

Starting the database for the first time may take a moment, as the sample data has to be imported.

You can use `docker logs publy_db_graphql_training` to find lines containing `database system was shut down`
and then `database system is ready to accept connections`. Then postgres is running.

For example:

```shell
$ docker logs publy_db_graphql_training

2022-04-19 09:28:35.924 UTC [56] LOG:  database system was shut down at 2022-04-19 09:28:35 UTC
2022-04-19 09:28:35.950 UTC [1] LOG:  database system is ready to accept connections
```

## Step 3: Test building the workspace

1. run the Gradle build in the **workspace** directory of this repository. That will also download all the required modules from the maven repositories:

```
cd workspace

./gradlew build
```

## Step 4: Open in the IDE

If you want, you can open the `workspace` directory in your IDE. Two (Gradle) projects should be detected and compiled there: `publy-backend` and `publy-userservice`.

You do _not_ need to open the other directories in this repository in the IDE. We work exclusively in the `workspace` directory.

**That's all!**

If you have any questions or problems, feel free to contact me.

# The finished application

In the directory `app` you will find a complete GraphQL application. A small part of this application, or rather its API, we will build together in the course of the
together during the training.

It is not necessary that you run or use the finished application. I will show it to you on my computer.

If you want to run the application, you can find more information in the `README` file in the `app` directory. Attention! For this application you need Java 17.
