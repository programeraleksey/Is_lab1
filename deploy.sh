./gradlew war
../glassfish7/bin/asadmin --port 32033 start-domain domain1
../glassfish7/bin/asadmin --port 32033 deploy --force=true --contextroot / build/libs/ROOT.war

# Mojarra кеширует WebsocketSessionManager в static-поле, и после redeploy f:websocket перестаёт получать push.
../glassfish7/bin/asadmin --port 32033 restart-domain domain1
