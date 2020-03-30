# MAAT CD API Infrastructure & Application Pipeline

## Deployment and Configuration

Each Codepipeline requires a configuration file in aws/pipeline/parameters (name: [environment]-${LAA_APP}-${LAA_ENV}.json).

Each environment requires a configuration file in aws/application/parameters (name: [environment]-${LAA_APP}-pipeline.json).

Create the Code pipeline by running the following. It also sets up Codebuild projects. Note that LAA_APP designates a Code pipeline instance - there could be more than one and therefore more than one set of application stacks per application.

```sh
gitdir="$( git rev-parse --show-toplevel )"
## Shared Services
LAA_APP=maat-cd-api-deployment-pipeline
LAA_ENV=shared-services
LAA_ACCOUNT=902837325998
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
          "Key": "source-code",
          "Value": "${LAA_REPO}"
      }
  ]
JSON
)

aws cloudformation validate-template --template-body "file://${gitdir}/aws/pipeline/maat-cd-api-deployment-pipeline.template" --profile "laa-${LAA_ENV}-lz"

aws cloudformation package --template-file "${gitdir}/aws/pipeline/maat-cd-api-deployment-pipeline.template" --s3-bucket "laa-cfn-${LAA_ACCOUNT}-eu-west-2" --s3-prefix "${LAA_APP}/${LAA_ENV}" --output-template-file "${gitdir}/aws/pipeline/${LAA_APP}.packaged" --profile "laa-${LAA_ENV}-lz"

# Initial create
aws cloudformation create-change-set --change-set-name "LAA-${LAA_APP}" --stack-name "LAA-${LAA_APP}" --template-body "file://${gitdir}/aws/pipeline/${LAA_APP}.packaged" --parameters "file://${gitdir}/aws/pipeline/parameters/${LAA_APP}-shared-services.json" --tags "${LAA_TAGS}" --capabilities CAPABILITY_NAMED_IAM --region eu-west-2 --profile "laa-${LAA_ENV}-lz" --change-set-type CREATE

# To update
aws cloudformation create-change-set --change-set-name "LAA-${LAA_APP}" --stack-name "LAA-${LAA_APP}" --template-body "file://${gitdir}/aws/pipeline/${LAA_APP}.packaged" --parameters "file://${gitdir}/aws/pipeline/parameters/${LAA_APP}-shared-services.json" --tags "${LAA_TAGS}" --capabilities CAPABILITY_NAMED_IAM --region eu-west-2 --profile "laa-${LAA_ENV}-lz" --change-set-type UPDATE

aws cloudformation execute-change-set --change-set-name "LAA-${LAA_APP}" --stack-name "LAA-${LAA_APP}" --region eu-west-2 --profile "laa-${LAA_ENV}-lz"

aws cloudformation delete-stack --stack-name LAA-${LAA_APP} --region eu-west-2 --profile laa-${LAA_ENV}-lz
```
