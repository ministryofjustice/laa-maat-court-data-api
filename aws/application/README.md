# Background

Infrastructure as Code for MAAT CD API stack that can deploy to landing zone-based environments i.e. development, test and production accounts.

## Deployment

To create the stack with the AWS CLI, for the below accounts set environment variables accordingly and then run the commands.

```sh
gitdir="$( git rev-parse --show-toplevel )
## Development
LAA_ENV=development
LAA_ACCOUNT=411213865113

LAA_APP=maat-cd-api
LAA_ACCOUNT=411213865113
LAA_OWNER=laa-crime-apps@digital.justice.gov.uk
LAA_INFRA=laa-role-sre@digital.justice.gov.uk
LAA_REPO=https://github.com/ministryofjustice/laa-maat-court-data-api
LAA_TAGS=$(
cat <<-JSON
  [
      {
          "Key": "business-unit",
          "Value": "LAA"
      },
      {
          "Key": "application",
          "Value": "${LAA_APP}"
      },
      {
          "Key": "component",
          "Value": "ebs"
      },
      {
          "Key": "environment-name",
          "Value": "${LAA_ENV}"
      },
      {
          "Key": "owner",
          "Value": "${LAA_OWNER}"
      },
      {
          "Key": "infrastructure-support",
          "Value": "${LAA_INFRA}"
      },
      {
          "Key": "runbook",
          "Value": ""
      },
      {
          "Key": "source-code",
          "Value": "${LAA_REPO}"
      }
  ]
JSON
)
```
And for the other accounts:
```sh
## Test
LAA_ENV=test
LAA_ACCOUNT=013163512034
```
```sh
## UAT
LAA_ENV=uat
LAA_ACCOUNT=140455166311
```
```sh
## Staging
LAA_ENV=staging
LAA_ACCOUNT=484221692666
```
```sh
## Production
LAA_ENV=production
LAA_ACCOUNT=842522700642
```
```sh
aws cloudformation validate-template --template-body "file://${gitdir}/aws/application/application.template --profile laa-${LAA_ENV}-lz"

aws cloudformation package --template-file "${gitdir}/aws/application/application.template --s3-bucket laa-cfn-${LAA_ACCOUNT}-eu-west-2 --s3-prefix ${LAA_APP}/${LAA_ENV} --output-template-file ${gitdir}/aws/application/application.packaged --profile laa-${LAA_ENV}-lz"

aws cloudformation create-stack --stack-name LAA-"${LAA_APP}"-"${LAA_ENV}" --template-body "file://${gitdir}/aws/application/application.packaged" --parameters "file://${gitdir}/aws/application/parameters/${LAA_APP}-${LAA_ENV}.json" --tags "${LAA_TAGS}" --capabilities CAPABILITY_NAMED_IAM --region eu-west-2 --profile laa-"${LAA_ENV}"-lz

aws cloudformation create-change-set --change-set-name LAA-"${LAA_APP}"-"${LAA_ENV}" --stack-name LAA-"${LAA_APP}"-"${LAA_ENV}" --template-body "file://${gitdir}/aws/application/application.packaged" --parameters "file://${gitdir}/aws/application/parameters/${LAA_APP}-${LAA_ENV}.json" --tags "${LAA_TAGS}" --capabilities CAPABILITY_NAMED_IAM --region eu-west-2 --profile laa-"${LAA_ENV}"-lz

aws cloudformation execute-change-set --change-set-name LAA-"${LAA_APP}"-"${LAA_ENV}" --stack-name LAA-"${LAA_APP}"-"${LAA_ENV}" --region eu-west-2 --profile laa-"${LAA_ENV}"-lz

aws cloudformation update-stack --stack-name LAA-"${LAA_APP}"-"${LAA_ENV}" --template-body "file://${gitdir}/aws/application/application.packaged" --parameters "file://${gitdir}/aws/application/parameters/${LAA_APP}-${LAA_ENV}.json" --tags "${LAA_TAGS}" --capabilities CAPABILITY_NAMED_IAM --region eu-west-2 --profile laa-"${LAA_ENV}"-lz

aws cloudformation delete-stack --stack-name LAA-"${LAA_APP}"-"${LAA_ENV}" --region eu-west-2 --profile laa-"${LAA_ENV}"-lz
```
