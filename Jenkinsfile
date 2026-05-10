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
               description: 'Cucumber tag expression e.g. @Login or @Regression')
    }

    environment {
        HEADLESS   = 'true'
        REPORT_DIR = 'target/ExtentReports'
    }

    stages {
        stage('Checkout') {
            steps {
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
                // Using caret (^) for Windows multi-line command
                bat """
                    mvn test ^
                      -Dbrowser=${params.BROWSER} ^
                      -Dheadless=${env.HEADLESS} ^
                      -Denv=${params.ENV} ^
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
                    reportFiles          : 'index.html', // Fixed: spark report is usually index.html or SparkReport.html
                    reportName           : 'Extent_5fReport' // Fixed: Changed to match your Extent_5fReport URL
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
                             [key: 'Branch', value: "master"],
                             [key: 'Browser', value: "${params.BROWSER}"],
                             [key: 'Environment', value: "${params.ENV}"]
                         ]
            }
        }
    }

    post {
                success {
                    // Send Email
                    emailext(
                        subject: "SUCCESS: ${env.JOB_NAME} Build #${env.BUILD_NUMBER}",
                        body: """
                            <html>
                            <body>
                              <p>Hello Team,</p>
                              <p>The latest Jenkins build has completed successfully.</p>
                              <p><b>Project Name:</b> ${env.JOB_NAME}</p>
                              <p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
                              <p><b>Build Status:</b> <span style="color: green;"><b>SUCCESS ✅</b></span></p>
                              <p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                              <hr>
                              <p><b>Detailed QA Reports:</b></p>
                              <ul>
                                <li><b>Extent Report:</b> <a href="${env.BUILD_URL}Extent_5fReport/">View Online</a></li>
                                <li><b>Cucumber Report:</b> <a href="${env.BUILD_URL}Cucumber_5fReport/">View Online</a></li>
                              </ul>
                              <p>Best regards,<br><b>Automation Team</b></p>
                            </body>
                            </html>
                        """,
                        mimeType: 'text/html',
                        to: 'hemanthreddy12773@gmail.com'
                    )
                    // Send Slack - Simplified because Global Config has the Webhook URL
                    slackSend(
                        color: 'good',
                        channel: '#all-jenkins',
                        message: "✅ SUCCESS: ${env.JOB_NAME} [${env.BUILD_NUMBER}]\nReport: ${env.BUILD_URL}Extent_5fReport/"
                    )
                }
                failure {
                    // Send Email
                    emailext(
                        subject: "FAILED: ${env.JOB_NAME} Build #${env.BUILD_NUMBER}",
                        body: """
                            <html>
                            <body>
                              <p>Hello Team,</p>
                              <p>The latest Jenkins build has <b style="color: red;">FAILED</b>.</p>
                              <p><b>Project Name:</b> ${env.JOB_NAME}</p>
                              <p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
                              <p><b>Build Status:</b> <span style="color: red;"><b>FAILED ❌</b></span></p>
                              <p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                              <p><b>Console Logs:</b> <a href="${env.BUILD_URL}console">View Failure Details</a></p>
                              <hr>
                              <p><b>Detailed QA Reports:</b></p>
                              <ul>
                                <li><b>Extent Report:</b> <a href="${env.BUILD_URL}Extent_5fReport/">View Online</a></li>
                                <li><b>Cucumber Report:</b> <a href="${env.BUILD_URL}Cucumber_5fReport/">View Online</a></li>
                              </ul>
                              <p>Best regards,<br><b>Automation Team</b></p>
                            </body>
                            </html>
                        """,
                        mimeType: 'text/html',
                        to: 'hemanthreddy12773@gmail.com'
                    )
                    // Send Slack - Simplified
                    slackSend(
                        color: 'danger',
                        channel: '#all-jenkins',
                        message: "❌ FAILED: ${env.JOB_NAME} [${env.BUILD_NUMBER}]\nConsole: ${env.BUILD_URL}console"
                    )
                }

        always {
            archiveArtifacts artifacts: 'target/ExtentReports/**',      fingerprint: true, allowEmptyArchive: true
            archiveArtifacts artifacts: 'target/cucumber-reports/**',   fingerprint: true, allowEmptyArchive: true
            archiveArtifacts artifacts: 'target/screenshots/**',        fingerprint: true, allowEmptyArchive: true
            cleanWs()
        }
    }
}
