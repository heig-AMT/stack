# StackUnderflow

[![Build and publish Docker image](https://github.com/heig-AMT/stack/workflows/Build%20and%20publish%20Docker%20image/badge.svg)](https://github.com/heig-AMT/stack/actions)
[![Heroku App Status](http://heroku-shields.herokuapp.com/heig-amt-stackunderflow)](https://heig-amt-stackunderflow.herokuapp.com)
[![Run tests](https://github.com/heig-AMT/stack/workflows/Run%20tests/badge.svg?branch=dev)]((https://github.com/heig-AMT/stack/actions))

This repository contains our version of the first project of the AMT class of HEIG-VD. Information related to gamification can be found at the bottom of this `README.md.

## Structure

+ The `docker` folder contains deployment information regarding our images and topology.
+ The `e2e` folder contains our end-to-end testing pipeline.
+ The `references` folder contains some documentation.
+ The `src` folder is the real deal - our Java EE app !

## Team

| Name                                   |                                  |
|----------------------------------------|----------------------------------|
| Matthieu Burguburu 					 | matthieu.burguburu@heig-vd.ch    |
| David Dupraz                           | david.dupraz@heig-vd.ch          |
| Alexandre Piveteau 				     | alexandre.piveteau@heig-vd.ch    |
| Guy-Laurent Subri                      | guy-laurent.subri@heig-vd.ch     |

## Running the app (locally)

Assuming you have Docker installed locally, you can run the following scripts to get the app running on your `8080`
port:

```bash
sh ./build-image.sh
sh ./run-locally.sh
```

Please note that this will only work if you have the `gamify-openjdk` image available locally ! To build that image, [follow these instructions first](https://github.com/heig-AMT/gamify#running-the-service-locally). We decided that, since folks usually prefer running specific development versions of Gamify during Stack development, it makes more sense to let you use a local `gamify-openjdk` image rather than always pull GitHub's one.

This will launch:
* a postgres image with a service named "database";
* our gamification server with a service named "gamify"; and
* our stackunderflow server with a service named "openliberty".

| Container | Inside port | Outside port |
|-----------|-------------|--------------|
| database | 5432 | 5432 |
| gamify | 1234 | 8081 |
| openliberty | 1234 | 8080 |

To run the e2e tests, run

```bash
sh ./build-image.sh
sh ./run-locally.sh "e2e"
```

The same setup will be available, but in addition, the selenium image will be running too.

## Mockups

Our prototypes are visible on [Figma](https://www.figma.com/file/gR04fKmQQZCZzwVC8SAbx3/Web?node-id=1%3A117).

## Deployments

A live version of our site is available on [Heroku](https://heig-amt-stackunderflow.herokuapp.com). We're using a free
plan, so it may need a few seconds to start up if the instance was previously paused :smile:


## Addentum : Project 3 (Gamification)

This project is integrated with the Gamify API. To achieve this, we've performed the following steps:

+ Adding the Gamify API spec in `src/resources/api-spec.yaml`. This specification will then automatically generate the code related to managing HTTP connections to the Gamify backend.
+ Adding a new `GamificationRepository`. Rather than calling the API directly from our facades, we think that gamification should be considered a "business model feature". This means that its exact implementation details should be hidden from the facades, so it can be mocked during unit testing.
+ Creating some business-level entities for events, rules, categories and badges. Again, this will be exposed by our `GamificationRepository`, whose implementation details should remain hidden.

## Gamification business rules

We've defined the following enumerations and entities in the `ch.heigvd.amt.stack.domain.gamification` package :

| Interface name | Role | Gamify API mapping |
|---|---|---|
| `GamificationBadge` | A badge that is displayed to the user | End-user badges from the `/badges` endpoints |
| `GamificationCategory` | A leaderboard that we're interested in. We create leaderboards for questions, answers and comments | Categories for the app from the `/categories` endpoints |
| `GamificationEvent` | The kinds of events that we want to handle when it comes to gamification | What's posted to `/events` |
| `GamificationRank` | The rank of a specific user in a leaderboard | The results of the `/leaderboards/{id}` and `/users/{id}` endpoints |
| `GamificationRule` | The real deal. Rules that make a mapping between `GamificationEvent` and some points attributed in a `GamificationCategory` | Rules as managed by the `/rules` endpoints |

Having enumerations allows us to ensure type-safety when accessing the API. Indeed, **we're not posting stringly-typed events, but well-known types**. This provides us with some compile-time guarantees that we're using the Gamification API correctly.

Additionally, this also gives use the ability to mock the `GamificationRepository` and replace it with a no-op implementation during unit testing. This would not be possible if we didn't have a domain-level repository.

A last benefit of this approach is that it lets us add, remove, and change rules, badges and events with ease. It's just a matter of adding an enum case ! At some point, we may even be able to dynamically edit the applied badges, rules, to load them dynamically at runtime.
