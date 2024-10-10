## What

[Link to story](https://dsdmoj.atlassian.net/browse/AP-XXX)

Describe what you did and why.

## Checklist

Before you ask people to review this PR:

- [ ] Tests should be passing: `./gradlew test`
- [ ] Github should not be reporting conflicts; you should have recently run `git rebase main`.
- [ ] Avoid mixing whitespace changes with code changes in the same commit. These make diffs harder to read and conflicts more likely.
- [ ] You should have looked at the diff against main and ensured that nothing unexpected is included in your changes.
- [ ] You should have checked that the commit messages say why the change was made.

## Additional checks

- Don’t forget to [run](https://github.com/ministryofjustice/laa-crimeapps-maat-functional-tests/actions/workflows/ExecuteUiTests.yaml) the MAAT functional test suite after deploying your changes to the DEV or TEST environments to ensure your changes haven’t broken any of the functional tests.