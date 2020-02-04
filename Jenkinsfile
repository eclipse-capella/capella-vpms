pipeline {

    agent { label "migration" }
    tools {
	maven "apache-maven-latest"
	jdk "oracle-jdk8-latest"
    }

    stages {
	stage ('Generate Targetplatform'){
	    steps {
		// must use mvn install so the addon build can find the .target
		sh 'mvn --batch-mode --activate-profiles generate-target -f capella-releng-parent/tp/capella-default-addon-target/pom.xml clean install'
	    }
	}
	stage ('Build') {
	    steps {
		sh 'mvn --batch-mode clean verify'
	    }
	}
    }
}
