# QWiZZ [Play here!](https://wichat.pablordgz.es)

[![Actions Status](https://github.com/arquisoft/wichat_en3a/workflows/CI%20for%20wichat_en3a/badge.svg)](https://github.com/arquisoft/wichat_en3a/actions)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Arquisoft_wichat_en3a&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Arquisoft_wichat_en3a)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Arquisoft_wichat_en3a&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Arquisoft_wichat_en3a)

 

 

<p float="left">
 

<img src="https://img.icons8.com/color/512/spring-logo.png" height="100">
 

<img src="https://cdn-icons-png.flaticon.com/512/226/226777.png" height="100">
 

</p>

## Participants
- **Laura Labrada Campos**: uo277510@uniovi.es
- **Ana Castro Álvarez**: uo293693@uniovi.es
- **Paula Díaz Álvarez**: uo294067@uniovi.es
- **Samuel de la Calle Fernández**: uo295358@uniovi.es
- **Pablo Rodríguez García**: uo293973@uniovi.es
- **Manuel Méndez Fernández**: uo294186@uniovi.es
- **Turabi Yıldırım**: uo311884@uniovi.es

## Quick start guide

First, clone the project:

```git clone git@github.com:arquisoft/wichat_en3a.git```

### LLM API key configuration

In order to communicate with the LLM integrated in this project, we need to set up an API key.
We have to create a .env file in the hintservice directory with this format:
HINTSERVICE_API_KEY=YOUR_API_KEY

Note that this file must NOT be uploaded to the github repository (they are excluded in the .gitignore).

An extra configuration for the LLM to work in the deployed version of the app is to include it as a repository secret (HINTSERVICE_API_KEY). This secret will be used by GitHub Action when building and deploying the application.

### Launching Using docker
For launching the propotype using docker compose, just type:
```docker compose --profile dev up --build```

### Monitoring
There is a monitoring and logging solution using Prometheus, Grafana and Loki. To use this you must launch the application using Docker. You must add a variable GRAFANA_ADMIN_PASSWORD to the .env, just like with the LLM API key. The new line should look like this:

GRAFANA_ADMIN_PASSWORD=YOUR_PASSWORD

Just like the LLM, for the deployment via GitHub actions, this password is set in the repository secrets.

The username to Grafana is admin.

Grafana is accessible on port 3001.

### Component by component start
First, start the database. Either install and run Mongo or run it using docker:

```docker run -d -p 27017:27017 --name=my-mongo mongo:latest```

You can use also services like Mongo Altas for running a Mongo database in the cloud.

You have to change the spring properties file of each service to use localhost URLs instead of Docker ones.

Then you have to launch all the services manually (using some IDE or Maven and Java from the command line)

After all the components are launched, the app should be available in localhost in port 8000.

## Acceptance tests
For acceptance tests to work, all services must be running and able to communicate between them. There is an application-e2e.properties file in some of the projects so that localhost is used to accesss services instead of the Docker service's host, this is for the acceptance tests to run in GitHub Actions. The acceptance tests are configured to run in headless mode so they work in GitHub actions, but you may remove the headless parameter when running them locally.

## Deployment
We target deployment on Azure, which has been tested and documented, but, for cost purposes, permanent deployment is done on a free Oracle VPS. Requirements for the machine for deployment to work are specified in the section below.

### Machine requirements for deployment
The machine for deployment can be created in services like Microsoft Azure or Amazon AWS. These are in general the settings that it must have:

- Linux machine with Ubuntu > 20.04 (the recommended is 24.04).
- An x86-64 (also known as AMD64 or Intel64) or ARM64 architecture
- Docker installed.
- Open ports for the applications installed (in this case, port 8000 for the webapp, and 3001 for Grafana, if you want to use it).

Once you have the virtual machine created, you can install **docker** using the following instructions:

```ssh
sudo apt update
sudo apt install apt-transport-https ca-certificates curl software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable"
sudo apt update
sudo apt install docker-ce
sudo usermod -aG docker ${USER}
```

### Continuous delivery (GitHub Actions)
Once we have our machine ready, we could deploy by hand the application, taking our docker-compose file and executing it in the remote machine. In this repository, this process is done automatically using **GitHub Actions**. The idea is to trigger a series of actions when some condition is met in the repository. The precondition to trigger a deployment is going to be: "create a new release". The actions to execute are the following:

![image](https://github.com/user-attachments/assets/96d80f32-8f29-4ee1-82a9-288b6cf97b99)



As you can see, unitary tests of each module and e2e tests are executed before pushing the docker images and deploying them. Using this approach we avoid deploying versions that do not pass the tests.

The deploy action is the following:

```yml
  deploy:
    name: Deploy over SSH
    runs-on: ubuntu-latest
    needs: [docker-push-userservice,docker-push-hintservice,docker-push-wikidataservice,docker-push-webapp]
    steps:
      - name: Deploy over SSH
        uses: fifsky/ssh-action@master
        with:
          host: ${{ secrets.DEPLOY_HOST }}
          user: ${{ secrets.DEPLOY_USER }}
          key: ${{ secrets.DEPLOY_KEY }}
          command: |
            wget https://raw.githubusercontent.com/arquisoft/wichat_en3a/master/docker-compose.yml -O docker-compose.yml
            echo "HINTSERVICE_API_KEY=${{ secrets.LLM_API_KEY }}" > .env
            docker compose --profile prod down
            docker compose --profile prod up -d --pull always
```

This action uses three secrets that must be configured in the repository:
- DEPLOY_HOST: IP of the remote machine.
- DEPLOY_USER: user with permission to execute the commands in the remote machine.
- DEPLOY_KEY: key to authenticate the user in the remote machine.

Note that this action logs in the remote machine and downloads the docker-compose file from the repository and launches it. Obviously, previous actions have been executed which have uploaded the docker images to the GitHub Packages repository.
