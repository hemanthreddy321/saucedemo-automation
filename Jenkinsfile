pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
        jdk   'JDK-17'
    }

    parameters {
        choice(name: 'BROWSER', choices: ['chrome', 'firefox'], description: 'Browser to run tests on')
        choice(name: 'ENV', choices: ['staging', 'prod'], description: 'Target environment')
        choice(name: 'TAGS', choices: ['@Regression', '@Login','@Inventory', '@Checkout'], description: 'Cucumber tag expression')
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
                bat 'mvn clean compile -q'
            }
        }

        stage('Run Tests') {
            steps {
                bat """
                    mvn test ^
                      -Dbrowser=${params.BROWSER} ^
                      -Dheadless=${env.HEADLESS} ^
                      -Denv=${params.ENV} ^
                      -Dcucumber.filter.tags="${params.TAGS}"
                """
            }
        }
    }

    post {
        always {
            // 1. Record JUnit Results
            junit '**/target/surefire-reports/*.xml'

            // 2. Publish Reports (Moved to always so they run on failure)
            publishHTML(target: [
                allowMissing         : false,
                alwaysLinkToLastBuild: true,
                keepAll              : true,
                reportDir            : "${env.REPORT_DIR}",
                reportFiles          : 'index.html',
                reportName           : 'Extent Report'
            ])

            cucumber buildStatus: 'UNSTABLE',
                     reportTitle: 'Cucumber_Report',
                     fileIncludePattern: '**/cucumber.json',
                     trendsLimit: 10,
                     sortingMethod: 'ALPHABETICAL',
                     classifications: [
                         [key: 'Browser', value: "${params.BROWSER}"],
                         [key: 'Environment', value: "${params.ENV}"]
                     ]

            // 3. Archive Artifacts
            archiveArtifacts artifacts: 'target/ExtentReports/**', allowEmptyArchive: true
            archiveArtifacts artifacts: 'target/cucumber-reports/**', allowEmptyArchive: true

            // 4. Cleanup
            cleanWs()
        }

        success {
            emailext(
                subject: "SUCCESS: ${env.JOB_NAME} Build #${env.BUILD_NUMBER}",
                body: """
                    <html><body>
<p>Hello Team,</p>
                              <p>The latest Jenkins build has completed successfully.</p>
                              <p><b>Project Name:</b> ${env.JOB_NAME}</p>
                              <p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
                              <p><b>Build Status:</b> <span style="color: green;"><b>SUCCESS ✅</b></span></p>
                              <p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                              <hr>
                              <p><b>Detailed QA Reports:</b></p>
                      <ul>
                          <li><b>Extent Report:</b> <a href="${env.BUILD_URL}artifact/target/ExtentReports/index.html">View Online</a></li>
                          <li><b>Cucumber Report:</b> <a href="${env.BUILD_URL}artifact/target/cucumber-reports/report.html">View Online</a></li>
                      </ul>
                    </body></html>
                """,
                mimeType: 'text/html',
                to: 'hemanthreddy12773@gmail.com'
            )
            withCredentials([string(credentialsId: 'slack-webhook', variable: 'SLACK_URL')]) {

                powershell """
                    \$payload = @{
                        text = "✅ SUCCESS: ${env.JOB_NAME} [#${env.BUILD_NUMBER}]`nExtent Report: ${env.BUILD_URL}artifact/target/ExtentReports/index.html"
                    } | ConvertTo-Json

                    Invoke-RestMethod `
                        -Uri "\$env:SLACK_URL" `
                        -Method Post `
                        -Body \$payload `
                        -ContentType 'application/json'
                """
            }
        }

        failure {
            emailext(
                subject: "FAILED: ${env.JOB_NAME} Build #${env.BUILD_NUMBER}",
                body: """
                    <html><body>
                      <p>Hello Team,</p>
                                                    <p>The latest Jenkins build has completed successfully.</p>
                                                    <p><b>Project Name:</b> ${env.JOB_NAME}</p>
                                                    <p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
                                                    <p><b>Build Status:</b> <span style="color: green;"><b>SUCCESS ✅</b></span></p>
                                                    <p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                                                    <hr>
                                                    <p><b>Detailed QA Reports:</b></p>
                      <ul>
                          <li><b>Extent Report:</b> <a href="${env.BUILD_URL}artifact/target/ExtentReports/index.html">View Online</a></li>
                          <li><b>Cucumber Report:</b> <a href="${env.BUILD_URL}artifact/target/cucumber-reports/report.html">View Online</a></li>
                      </ul>
                    </body></html>
                """,
                mimeType: 'text/html',
                to: 'hemanthreddy12773@gmail.com'
            )
            withCredentials([string(credentialsId: 'slack-webhook', variable: 'SLACK_URL')]) {

                powershell """
                    \$payload = @{
                        text = "❌ FAILED: ${env.JOB_NAME} [#${env.BUILD_NUMBER}]`nConsole: ${env.BUILD_URL}console"
                    } | ConvertTo-Json

                    Invoke-RestMethod `
                        -Uri "\$env:SLACK_URL" `
                        -Method Post `
                        -Body \$payload `
                        -ContentType 'application/json'
                """
            }
        }
    }
}
