pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
        jdk   'JDK-17'
    }

    parameters {
        choice(name: 'BROWSER',
               choices: ['chrome', 'firefox'],
               description: 'Browser to run tests on')
        choice(name: 'ENV',
               choices: ['staging', 'prod'],
               description: 'Target environment')
        string(name: 'TAGS',
               defaultValue: '@Regression',
               description: 'Cucumber tag expression e.g. @Login or @Regression and not @Wip')
    }

    environment {
        HEADLESS   = 'true'
        REPORT_DIR = 'target/ExtentReports'
    }

    stages {

        stage('Checkout') {
            steps {
                echo "Checking out branch: ${env.GIT_BRANCH}"
                git branch: 'master',
                    url: 'https://github.com/hemanthreddy321/saucedemo-automation.git',
                    credentialsId: 'github-creds'
            }
        }

        stage('Compile') {
            steps {
                echo 'Compiling project...'
                bat 'mvn clean compile -q'
            }
        }

        stage('Run Tests') {
            steps {
                echo "Running tests with tags: ${params.TAGS} on ${params.BROWSER}"
                bat """
                    mvn test \\
                      -Dbrowser=${params.BROWSER} \\
                      -Dheadless=${env.HEADLESS} \\
                      -Denv=${params.ENV} \\
                      -Dcucumber.filter.tags="${params.TAGS}"
                """
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Publish Extent Report') {
            steps {
                publishHTML(target: [
                    allowMissing         : false,
                    alwaysLinkToLastBuild: true,
                    keepAll              : true,
                    reportDir            : "${env.REPORT_DIR}",
                    reportFiles          : 'index.html',
                    reportName           : 'Extent Report'
                ])
            }
        }

        stage('Publish Cucumber Report') {
            steps {
                cucumber buildStatus                : 'UNSTABLE',
                         reportTitle                : 'SauceDemo Cucumber Report',
                         fileIncludePattern         : '**/cucumber.json',
                         trendsLimit                : 10,
                         classifications            : [
                             [key: 'Branch', value: "${env.GIT_BRANCH}"],
                             [key: 'Browser', value: "${params.BROWSER}"],
                             [key: 'Environment', value: "${params.ENV}"]
                         ]
            }
        }
    }

    post {
        success {
            echo "BUILD PASSED - Report: ${env.BUILD_URL}Extent_Report/"
            slackSend(color: 'good',
                      message: "PASSED: ${env.JOB_NAME} #${env.BUILD_NUMBER} | ${env.BUILD_URL}Extent_Report/")
        }
        failure {
            emailext(
                subject: "FAILED: ${env.JOB_NAME} Build #${env.BUILD_NUMBER}",
                body: """
                    <h2>Build Failed</h2>
                    <p><b>Job:</b> ${env.JOB_NAME}</p>
                    <p><b>Build #:</b> ${env.BUILD_NUMBER}</p>
                    <p><b>Branch:</b> ${env.GIT_BRANCH}</p>
                    <p><b>Browser:</b> ${params.BROWSER}</p>
                    <p><b>Tags:</b> ${params.TAGS}</p>
                    <p><b>Console:</b> <a href="${env.BUILD_URL}console">${env.BUILD_URL}console</a></p>
                    <p><b>Report:</b> <a href="${env.BUILD_URL}Extent_Report/">${env.BUILD_URL}Extent_Report/</a></p>
                """,
                mimeType: 'text/html',
                to: 'hemanthreddy12773@gmail.com'
            )
            slackSend(color: 'danger',
                      message: "FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER} | ${env.BUILD_URL}console")
        }
        unstable {
            echo "BUILD UNSTABLE - Some tests failed"
        }
        always {
            archiveArtifacts artifacts: 'target/ExtentReports/**',      fingerprint: true
            archiveArtifacts artifacts: 'target/cucumber-reports/**',   fingerprint: true
            archiveArtifacts artifacts: 'target/screenshots/**',        fingerprint: true, allowEmptyArchive: true
            cleanWs()
        }
    }
}
