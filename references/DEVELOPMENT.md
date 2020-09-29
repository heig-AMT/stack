# Running the project locally

You can use the `mvn liberty:dev` goal to run the application locally with a hot reloading server, instead of building and running Docker containers for every build. There are a few things to pay attention to:

- you need to have a `PORT` environment variable in the scope where the goal is run, with a value like `8080`. This is the port that will be used when the app is run on your machine. Please export it in your shell or [in your IDE](https://www.jetbrains.com/help/idea/maven-runner.html).