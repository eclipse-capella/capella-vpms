pipeline {
    agent any
    stages {
	stage ('Generate Targetplatform'){
	    steps {
		// must use mvn install so the addon build can find the .target
		bat 'mvn --batch-mode --activate-profiles generate-target -f capella-releng-parent/tp/capella-default-addon-target/pom.xml clean install'
	    }
	}
	stage ('Build') {
	    steps {
		bat 'mvn --batch-mode clean verify'
	    }
	}
    }
}
