# StackUnderflow

[![Build and publish Docker image](https://github.com/heig-AMT/stack/workflows/Build%20and%20publish%20Docker%20image/badge.svg)](https://github.com/heig-AMT/stack/actions)
[![Heroku App Status](http://heroku-shields.herokuapp.com/heig-amt-stackunderflow)](https://heig-amt-stackunderflow.herokuapp.com)
[![Run tests](https://github.com/heig-AMT/stack/workflows/Run%20tests/badge.svg?branch=dev)]((https://github.com/heig-AMT/stack/actions))

This repository contains our version of the first project of the AMT class of HEIG-VD.

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

Assuming you have Docker installed locally, you can run the following scripts to get the app running on your `8080` port :

```bash
sh ./build-image.sh
sh ./run-image.sh
```