## laa-maat-court-data-api

This is a Java based Spring Boot Application which will be hosted on AWS Environment. This is a Facade application to the existing legacy Applications MAAT/MLRA. laa-maat-court-data-api orchestrates the traffic between Court Data Adaptor/Common Platform and MAAT/MLRA.


## Developer setup

### Pre-requisites

1. Docker
1. SSH
1. An editor/IDE of some sort - preferably Intellij/Ecilipse 
1. Gradle

We're using [Gradle](https://gradle.org/) to build the application. This also includes plugins for generating IntelliJ configuration.


### Decrypting docker-compose.override.yml
The `docker-compose.override.yml` is encrypted using [git-crypt](https://github.com/AGWA/git-crypt). 

To run the app locally you need to be able to decrypt this file.

You will first need to create a GPG key. See [Create a GPG Key](https://docs.publishing.service.gov.uk/manual/create-a-gpg-key.html) for details on how to do this with `GPGTools` (GUI) or `gpg` (command line).

Once you have done this, a team member who already has access can add your key by running `git-crypt add-gpg-user USER_ID`* and creating a pull request to this repo.

Once this has been merged you can decrypt your local copy of the repository by running `git-crypt unlock`.

*`USER_ID` can be your key ID, a full fingerprint, an email address, or anything else that uniquely identifies a public key to GPG (see "HOW TO SPECIFY A USER ID" in the gpg man page).

### Configuration

1. Database  needs to be up and running before the application runs

Database: You will need to have the relevant database accessible on port 1521 locally. This can be provided by an SSH tunnel to an RDS instance in AWS. Here is the command to tunnel to Dev (add your user Bastion user name):

```sh
ssh -L 1521:rds.maat.aws.dev.legalservices.gov.uk:1521 <username>@35.176.251.101 -i ~/.ssh/id_rsa
```

2. Clone Repository

```sh
git clone git@github.com:ministryofjustice/laa-maat-court-data-api.git

cd maat-court-data-api
```
3. You will need to build the artifacts for the source code, using `gradle`.

```sh
./gradlew clean build
```
4. The apps should then startup cleanly if you run

```sh
docker-compose build
docker-compose up
```

5. laa-maat-court-data-api application will be running on http://localhost:8080 

