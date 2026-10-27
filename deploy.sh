./gradlew war
../glassfish7/bin/asadmin --port 32033 start-domain domain1
../glassfish7/bin/asadmin --port 32033 deploy --force=true --contextroot / build/libs/ROOT.war
