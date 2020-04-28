@Library('github.com/connexta/cx-pipeline-library@master') _
@Library('github.com/connexta/github-utils-shared-library@master') __

pipeline {
    agent {
        node {
            label 'linux-small'
            customWorkspace "/jenkins/workspace/${JOB_NAME}/${BUILD_NUMBER}"
        }
    }
    options {
        buildDiscarder(logRotator(numToKeepStr:'25'))
        disableConcurrentBuilds()
        timestamps()
        skipDefaultCheckout()
    }
    triggers {
        /*
          Restrict nightly builds to master branch, all others will be built on change only.
          Note: The BRANCH_NAME will only work with a multi-branch job using the github-branch-source
        */
        cron(BRANCH_NAME == "master" ? "@weekly" : "")
		pollSCM("H/10 * * * *")
    }
    environment {
        LARGE_MVN_OPTS = '-Xmx8192M -Xss128M -XX:+CMSClassUnloadingEnabled -XX:+UseConcMarkSweepGC '
        LINUX_MVN_RANDOM = '-Djava.security.egd=file:/dev/./urandom'
        GITHUB_USERNAME = 'codice'
        GITHUB_TOKEN = credentials('github-api-cred')
        GITHUB_REPONAME = 'imaging-nitf'
    }
    stages {
        stage('Setup') {
            steps {
                dockerd {}
                slackSend color: 'good', message: "STARTED: ${JOB_NAME} ${BUILD_NUMBER} ${BUILD_URL}"
                postCommentIfPR("Internal build has been started, your results will be available at build completion.", "${GITHUB_USERNAME}", "${GITHUB_REPONAME}", "${GITHUB_TOKEN}")
            }
        }

        // Checkout the repository
        stage('Checkout repo') {
            steps {
                retry(3) {
                    checkout([$class: 'GitSCM', branches: [[name: "refs/heads/${BRANCH_NAME}"]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'WipeWorkspace'], [$class: 'LocalBranch', localBranch: "${BRANCH_NAME}"], [$class: 'CloneOption', timeout: 30, shallow: true]], submoduleCfg: [], userRemoteConfigs: scm.userRemoteConfigs ])
                }
            }
        }

        stage('Full Build except itest') {
            when {
                expression { env.CHANGE_ID == null } 
            }
            options {
                timeout(time: 1, unit: 'HOURS')
            }
            steps {
                withMaven(maven: 'maven-latest', globalMavenSettingsConfig:  '51e52749-c47a-4e11-9c58-0adf485626f5', mavenSettingsConfig: 'feca3f61-1da1-4887-a9ad-dd4a41fd4423', mavenOpts: '${LARGE_MVN_OPTS} ${LINUX_MVN_RANDOM}') {
                    sh 'mvn -P!documentation -P!findbugs -DskipTests=true -Dpmd.skip=true -Djacoco.skip=true -Dfindbugs.skip=true -Dcheckstyle.skip=true clean install'
                }
            }
        }

        stage('Full Build') {
            when {
                expression { env.CHANGE_ID == null } 
            }
            options {
                timeout(time: 1, unit: 'HOURS')
            }
            steps {
                withMaven(maven: 'maven-latest', mavenSettingsConfig: 'feca3f61-1da1-4887-a9ad-dd4a41fd4423', mavenOpts: '${LARGE_MVN_OPTS} ${LINUX_MVN_RANDOM}') {
                    sh 'mvn clean install -T 1C'
                }
            }
        }

        /*
          Deploy stage will only be executed for deployable branches. 
          It will also only deploy in the presence of an environment variable JENKINS_ENV = 'prod'. This can be passed in globally from the jenkins master node settings.
        */
        stage('Deploy') {
            when {
                allOf {
                    expression { env.CHANGE_ID == null }
                    expression { env.BRANCH_NAME ==~ /((?:\d*\.)?\d*\.x|master)/ }
                    environment name: 'JENKINS_ENV', value: 'prod'
                }
            }
            steps{
                withMaven(maven: 'maven-latest', jdk: 'jdk8-latest', globalMavenSettingsConfig: '51e52749-c47a-4e11-9c58-0adf485626f5', mavenSettingsConfig: 'codice-maven-settings', mavenOpts: '${LINUX_MVN_RANDOM}') {
                    sh 'mvn deploy -nsu -DskipTests=true -Dpmd.skip=true -Dfindbugs.skip=true -Dcheckstyle.skip=true -Djacoco.skip=true'
                }
            }
        }
    }
    post {
        always{
            postCommentIfPR("Build ${currentBuild.currentResult} See the job results in [legacy Jenkins UI](${BUILD_URL}) or in [Blue Ocean UI](${BUILD_URL}display/redirect).", "${GITHUB_USERNAME}", "${GITHUB_REPONAME}", "${GITHUB_TOKEN}")
        }
        success {
            slackSend color: 'good', message: "SUCCESS: ${JOB_NAME} ${BUILD_NUMBER}"
        }
        failure {
            slackSend color: '#ea0017', message: "FAILURE: ${JOB_NAME} ${BUILD_NUMBER}. See the results here: ${BUILD_URL}"
        }
        unstable {
            slackSend color: '#ffb600', message: "UNSTABLE: ${JOB_NAME} ${BUILD_NUMBER}. See the results here: ${BUILD_URL}"
        }
        cleanup {
            catchError(buildResult: null, stageResult: 'FAILURE') {
                echo '...Cleaning up workspace'
                cleanWs()
                sh 'rm -rf ~/.m2/repository'
                wrap([$class: 'MesosSingleUseSlave']) {
                    sh 'echo "...Shutting down Jenkins slave: `hostname`"'
                }
            }
        }
    }
}
