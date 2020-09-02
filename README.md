# Generate release notes for a GitLab milestone
Fork from [Spring IO github-release-notes-generator](https://github.com/spring-io/github-release-notes-generator)  

To generate markdown release notes that comprise the list of bugs and issues in a GitLab milestone follow these steps:  

Download a release JAR or use the Docker image.  

Configure the application with the following properties:  

``` yml
releasenotes:
  gitlab:
    access-token: ${gitlabAccessToken}
    name: ${projectName}
    project-id: ${projectID}
    milestoneTitle: ${milestoneTitle}
    path-to-generate-file: ${pathToGenerateFile}
```
