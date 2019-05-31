#!/usr/bin/env groovy

def default_java="openjdk-1.8"
def default_os="rhel7"
def java_axes=["openjdk-1.8", "openjdk11"]
def os_axes=["rhel7", "win10"]

def axes=[:]
os_axes.each {
    os -> java_axes.each {
        java -> 
            if (!(os.equalsIgnoreCase( default_os ) && java.equalsIgnoreCase( default_java ))) {
                axes.put(os + ' ' + java, stageClosure(os, java))
            }
    }
}

def runCommand(String command) {
    if (isUnix()) {
        shell "${command}"
    } else {
        bat "${command}"
    }
}

def stageClosure(String os_label, String java) {
    return {
        node(os_label) {
            try {
                def description = "${os_label} ${java}"
                stage("${description}") {
                    
                    withEnv(["JAVA_HOME=${tool java}", "MAVEN=${tool 'maven3-latest'}/bin"]) {
                        unstash 'source'
                        runCommand "java -version"
                        runCommand "which java"
                        runCommand("${MAVEN}/mvn -version")
                        runCommand("${MAVEN}/mvn clean install -fae -B")
                        runCommand("${MAVEN}/mvn verify -B -Pintegration-tests -DskipTests=true -Dmaven.test.failure.ignore=true")
                    }
                }
            } catch (e) {
                echo 'Running maven commands failed'
                echo "${e.toString()}"
                echo "${e.getMessage()}"
                // Since we're catching the exception in order to report on it,
                // we need to re-throw it, to ensure that the build is marked as failed
                throw e
            } finally {
                def currentResult = currentBuild.result ?: 'SUCCESS'
                // if (currentResult == 'UNSTABLE') {
                //     echo 'This will run only if the run was marked as unstable'
                // }
        
                // def previousResult = currentBuild.getPreviousBuild()?.result
                // if (previousResult != null && previousResult != currentResult) {
                //     echo 'This will run only if the state of the Pipeline has changed'
                //     echo 'For example, if the Pipeline was previously failing but is now successful'
                // }
        
                // echo 'This will always run'
                junit '**/surefire-reports/*.xml'
                archiveArtifacts '**/integration-tests/target/surefire-reports/*,**/tests/**/target/surefire-reports/*'
            }

        }
    }
}

pipeline {
    
	agent { label 'rhel7-micro' }
	
	options {
        timeout(time: 2, unit: 'HOURS')
    }

	tools {
		maven 'maven3-latest'
		jdk 'openjdk-1.8'
	}
	
	stages {
        stage('Checkout SCM') {
			steps {
				deleteDir()
				git url: "https://github.com/${params.FORK}/rsp-server.git", branch: params.BRANCH
				stash includes: '**', name: 'source'
			}
        }
        stage ('Running parallel builds & tests') {
            parallel {
                stage ('Java 8 runtime') {
    	            
    	            agent { label 'rhel7' }
    	            
    	            stages {
    
                		stage('Build & unit tests') {
                			steps {
                				unstash 'source'
                				sh 'mvn clean install -fae -B'
                				archiveArtifacts 'distribution/distribution*/target/org.jboss.tools.rsp.distribution*.zip,api/docs/org.jboss.tools.rsp.schema/target/*.jar'
                				stash includes: 'distribution/distribution*/target/org.jboss.tools.rsp.distribution*.zip,api/docs/org.jboss.tools.rsp.schema/target/*.jar', name: 'zips'
                        	}
                		}
                		
                        stage('Integration tests') {
                			steps {
                				sh 'mvn verify -B -Pintegration-tests -DskipTests=true -Dmaven.test.failure.ignore=true'
                			}
                        }
                		
                		stage('SonarCloud Report') {
                		    when {
                		        expression { params.SONAR }
                		    }
                		    steps {
                			    sh 'mvn -B -P sonar sonar:sonar -Dsonar.login=${SONAR_TOKEN}'
                		    }
                		}
                	
    	            }
                	post {
                	    always {
                	        junit '**/surefire-reports/*.xml'
                	        archiveArtifacts '**/integration-tests/target/surefire-reports/*,**/tests/**/target/surefire-reports/*'
                	    }
                	}
    	        }
        	    stage('Running parallel integration tests') {
            	    steps {
                        script {
            	            parallel axes
                        }
                    }
        	    }
                
            }
        }
	}
	post {
	    success {
			script {
			    unstash 'zips'
				def filesToPush = findFiles(glob: '**/*.zip')
				for (i = 0; i < filesToPush.length; i++) {
					sh "rsync -Pzrlt --rsh=ssh --protocol=28 ${filesToPush[i].path} ${UPLOAD_LOCATION}/snapshots/rsp-server/"
				}
			}
	    }
	}
}
