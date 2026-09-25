import groovy.json.JsonOutput

def jobs = [
    [name: 'Terraform AWS',             folder: 'Infrastructure'],
    [name: 'Kubernetes Deployment',     folder: 'Infrastructure'],
    [name: 'EKS Cluster',              folder: 'Infrastructure'],
    [name: 'Network Provisioning',      folder: 'Infrastructure'],

    [name: 'Docker Build',              folder: 'DevOps'],
    [name: 'Jenkins Backup',            folder: 'DevOps'],
    [name: 'Security Scan',             folder: 'DevOps'],
    [name: 'Dependency Update',         folder: 'DevOps'],
    [name: 'Release Pipeline',          folder: 'DevOps'],
]

// Create folders
jobs.findAll { it.folder != null }
    .collect { it.folder }
    .unique()
    .each { folderName ->
        folder(folderName) {
            description("Dummy Jenkins jobs - ${folderName}")
        }
    }


// Create jobs
jobs.each { jobInfo ->

    def jobName = jobInfo.name
    def folder = jobInfo.folder

    def fullJobName = folder ?
        "${folder}/${jobName}" :
        jobName

    pipelineJob(fullJobName) {

        description("""
            Dummy ${jobName} pipeline.
            Automatically generated using Job DSL.
            Random build result: SUCCESS / UNSTABLE / FAILURE / ABORTED.
        """.stripIndent())

        logRotator {
            numToKeep(30)
            artifactNumToKeep(5)
        }

        properties {
            disableConcurrentBuilds()
        }

        triggers {
            cron('* * * * *')
        }

        definition {
            cps {

                script("""
pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out ${jobName}'
                sleep 1
            }
        }

        stage('Build') {
            steps {
                echo 'Building ${jobName}'
                sleep 2
            }
        }

        stage('Test') {
            steps {
                echo 'Running tests for ${jobName}'
                sleep 2
            }
        }

        stage('Random Result') {
            steps {

                script {

                    def result = new Random().nextInt(4)

                    echo "======================================"
                    echo "Job       : ${jobName}"
                    echo "Result    : \${result}"
                    echo "======================================"

                    switch (result) {

                        case 0:
                            echo 'SUCCESS'
                            currentBuild.result = 'SUCCESS'
                            break

                        case 1:
                            echo 'UNSTABLE'
                            unstable(
                                "Randomly generated unstable build"
                            )
                            break

                        case 2:
                            echo 'FAILURE'
                            error(
                                "Randomly generated failure"
                            )
                            break

                        case 3:
                            echo 'ABORTED'

                            currentBuild.result = 'ABORTED'

                            throw new org.jenkinsci.plugins.workflow.steps.FlowInterruptedException(
                                hudson.model.Result.ABORTED
                            )
                    }
                }
            }
        }
    }

    post {
        always {
            echo "Final build status: \${currentBuild.currentResult}"
        }
    }
}
                """.stripIndent())

                sandbox()
            }
        }
    }
}