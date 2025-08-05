pipeline {
	agent any

	tools {
		jdk 'jdk17'
	}
	environment{
		GRADLE_OPTS = "-Dorg.gradle.jvmargs=-Xmx1024m"
	}
	stages {
		stage('Checkout'){
			steps{
				git 'https://github.com/patryklorbiecki1/appointment-booking.git'
			}
		}
		stage('Build') {
			steps{
				sh 'chmod +x gradlew'
				sh './gradlew clean build --no-deamon'
			}
		}
		stage('Test'){
			steps{
				sh './gradlew test --no-deamon'
			}
		}
	}
	post{
		always{
			junit '**/build/test-results/test/*.xml'
		}
	}
}