pipeline {

    //agent { label "migration" }
    agent any
    // tools {
    //  maven "apache-maven-latest"
    //  jdk "oracle-jdk8-latest"
    // }

    stages {
	stage ('Clone repos'){
	    steps {
		checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'capella-releng-parent']], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/felixdo/capella-releng-parent.git']]])
		checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'addon']], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/felixdo/capella-vpms.git']]])
	    }
	}

	stage ('Generate Targetplatform'){
	    steps {
		// must use mvn install so the addon build can find the .target
		bat 'mvn --batch-mode --activate-profiles generate-target -f capella-releng-parent/tp/capella-default-addon-target/pom.xml clean install'
	    }
	}
	stage ('Build') {
	    steps {
		bat 'mvn --batch-mode -f addon/pom.xml clean verify'
	    }
	}
    }
}
