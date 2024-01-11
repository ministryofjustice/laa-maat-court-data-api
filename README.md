## laa-maat-court-data-api

This is a Java based Spring Boot Application which will be hosted on AWS Environment. The application is being deployed on to the AWS ECS container service. This is a Facade application to the existing LAA legacy Applications MAAT/MLRA. laa-maat-court-data-api orchestrates the traffics between Court Data Adaptor/Common Platform and MAAT/MLRA.

This Application mainly supports 4 user Journeys. The Details of the journeys can be found on the following links.

1. Linking Cases between MAAT and Common Platform  - https://dsdmoj.atlassian.net/wiki/spaces/LAACP/pages/1758822646/Search+and+Link+Sequence+v2
2. Unlinking a Linked case from Common Platform - https://dsdmoj.atlassian.net/wiki/spaces/LAACP/pages/1858994261/Unlinking+Sequence+Diagrams
3. LAA Status Update . - https://dsdmoj.atlassian.net/wiki/spaces/LAACP/pages/1711210653/Rep+Order+Results+Sequence+Diagram
4. Hearing Notifications Resulted - https://dsdmoj.atlassian.net/wiki/spaces/LAACP/pages/1660387451/MLRA+MAAT+-+CDA+Integration


## Developer setup

* [MAAT Court Data API Deployment Requirements](https://dsdmoj.atlassian.net/wiki/spaces/LAACP/pages/1889992851/MAAT+Court+Data+API+Deployment+Requirements)

* [Developer Starter Guild](https://dsdmoj.atlassian.net/wiki/spaces/LAA/pages/1391460702/New+Hire+Check+List) 


### Pre-requisites

1. Docker
2. SSH
3. An editor/IDE of some sort - preferably Intellij/Eclipse
4. Grade
5. AWS CLI
6. kubectl
7. Java 17

We're using [Gradle](https://gradle.org/) to build the application. This also includes plugins for generating IntelliJ configuration.


### Decrypting docker-compose.override.yml
The `docker-compose.override.yml` is encrypted using [git-crypt](https://github.com/AGWA/git-crypt). 

To run the app locally you need to be able to decrypt this file.

See the Confluence page [GPG and git-crypt](https://dsdmoj.atlassian.net/wiki/spaces/ASLST/pages/3761963077/Java+Project+Setup+with+CircleCI+and+Helm+on+Cloud+Platform#GPG-and-git-crypt) for details.
In summary, you will need to generate a GPG key, publish your key to a key server and have a member of the dev team who already has git-crypt setup for this repo.

Once the steps have been completed you can decrypt your local copy of the repository by running `git-crypt unlock`. 

### DB Configuration


This application connects to MAAT DB which is hosted on RDS instances within LAA AWS Environment (Legacy Account) 

You will need to have the relevant database accessible on port 1521 locally. This can be provided by an SSH tunnel to an RDS instance in AWS.  
The steps required to configure access via Bastions are listed [here](https://dsdmoj.atlassian.net/wiki/spaces/aws/pages/4584865935/AWS+-+).  
The command to create an SSH tunnel (with port forwarding for 1521) to Dev is included below.  
*Remember to replace _**astone**_ with your own Bastion username.

```sh
ssh -vvv -L 1521:rds.maat.aws.tst.legalservices.gov.uk:1521 astone@ssmbastion-non-prod-lz
```


### Application Set up

Clone Repository

```sh
git clone git@github.com:ministryofjustice/laa-maat-court-data-api.git

cd maat-court-data-api
```
Make sure that all tests are passing by running the following ‘gradle’ command  

```sh
./gradlew clean test
```
 You will need to build the artifacts for the source code, using `gradle`.

```sh
./gradlew clean build
```
The apps should then startup cleanly if you run

```sh
docker-compose build
docker-compose up
```

laa-maat-court-data-api application will be running on http://localhost:8090 


### Cloud Platform Set Up 

MAAT API speaks to AWS SQS which exists on cloud platform. Due to recent changes we have moved away from using long lived access keys to authenticate with the queue in favour of using the IAM role attached to the ECS task MAAT API is running in.
As such we are currently not able to run the SQS queue listeners in MAAT locally. [LASB-2405](https://dsdmoj.atlassian.net/browse/LASB-2405) is a spike that will investigate the best method of running MAAT API locally with the SQS listeners and once a method has been determine the documentation here will be updated.
For now there is a `ENABLE_SPRING_CLOUD_SQS` that is set to `false` in the `docker-compose.override.yml` file when running MAAT API locally and will default to `true` in all other conditions.

### Deployment 

The Deployment requirements and all integrations of laa-maat-court-data-api can be found here. https://dsdmoj.atlassian.net/wiki/spaces/LAACP/pages/1889992851/MAAT+Court+Data+API+Deployment+Requirements

We practice CI/CD approach and this is being done using AWS Code Pipeline Service.

laa-maat-court-data-api  is being deployed on AWS Environment (Legacy Account) & the infrastructure and pipelines are created using AWS Cloud Formation templates.

Cloud formation scripts can be found at laa-maat-court-data-api /aws

### MAAT Open API
We have implemented the Open API (with Swagger 3). The web link provides a details Rest API with a schema definition. The link can only from local or from dev environment. The swagger link can be found from [here](http://localhost:8090/open-api/docs.html)  
  
  

### Debugging Application

Speak to one of the team member and get the docker-compose-debug.yml which will have  relevant credentials  to run the application on remote Debug Mode.

Run the following command
  
```sh
 docker-compose -f docker-compose-debug.yml up
```

Make sure Remote Debug Option is set up on your preferred Editor.


### Application Monitoring and Logs 

The LAA-MAAT-API has been configured to send the application logs to both AWS Cloud Watch and Sentry. 

####Cloud Watch Logs: 
To see the Cloud watch logs, you need to have the right user groups and permission. More details about this available here. (link) The application logs are configured with the followings log groups (names). 
The application deployed as a Docker container, and the logs can also be found from the AWS ECS logs. 

####Sentry 
Sentry is a 3rd party application logging and monitoring platform. The platform provides easier searching based on meta-data as well as application monitoring. You can learn more about ['how we have integrated Sentry to improve application logging and monitoring'](https://dsdmoj.atlassian.net/wiki/spaces/LAACP/pages/2139914261/Integrate+Sentry+to+improve+application+logging+and+monitoring)
There are several alert rules configured on Sentry that will push notification to both email and Slack channel. We have created a dedicated slack channel (named 'laa-crime-apps-logs-alert'). Sentry will push the alert to this channel for a specific type of exceptions. The configuration for Slack alert can be change from a [Sentry dashboard](https://sentry.io/settings/ministryofjustice/projects/laa-maat-court-data-api/alerts/).  

### Mutation PI testing 

Mutation testing providing test coverage for Java applications.
Faults (or mutations) are automatically seeded into the code, then your tests are run. If your tests fail then the mutation is killed, if your tests pass then the mutation lived.
Here are some of the key benefits for this type of testing. 
* High coverage of testing
* New kinds of test scenarios
* Validate unit test scripts

Once we build the project then run the following command. This will generate a test report under build/reports/pitest/ 
We want to make sure that the Mutation Coverage for new classes are covered properly 
```sh
./gradlew pitest
```

### Running SQS Integration Tests

To ensure the SqsIntegrationTests run as part of the build or individually:
* Ensure at least JDK 11 is installed (OpenJDK 11.0.19 tested working)
* Ensure Gradle is installed (Gradle 8.1.1 tested working)
* Ensure Docker is running with Docker Desktop (Docker Desktop 4.20.1 tested working)
* Pull localstack/localstack image to your local Docker registry with the command:
```sh
docker pull localstack/localstack
```

### Further reading
 

Here are some links that might be useful to learn more about the project. 

* [MLRA/MAAT - CDA Integration](https://dsdmoj.atlassian.net/wiki/spaces/LAACP/pages/1660387451/MLRA+MAAT+-+CDA+Integration) 

* [MAAT Court Data API Deployment Requirements](https://dsdmoj.atlassian.net/wiki/spaces/LAACP/pages/1889992851/MAAT+Court+Data+API+Deployment+Requirements) 
  
* [Diagrams for LAA and the common platform](https://dsdmoj.atlassian.net/wiki/spaces/LAACP/pages/1513128006/Diagrams)

* [New Starter Guild](https://dsdmoj.atlassian.net/wiki/spaces/LAA/pages/1391460702/New+Hire+Check+List)




